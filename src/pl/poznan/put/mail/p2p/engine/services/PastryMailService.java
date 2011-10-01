/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.poznan.put.mail.p2p.engine.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import pl.poznan.put.mail.p2p.engine.messages.Mail;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pl.poznan.put.mail.p2p.engine.messages.MailAddress;
import pl.poznan.put.mail.p2p.engine.pastry.MailApplication;
import pl.poznan.put.mail.p2p.engine.pastry.MailMessageHeaderListPastContent;
import pl.poznan.put.mail.p2p.engine.pastry.MailMessagePastContent;
import pl.poznan.put.mail.p2p.engine.pastry.MailMessagePastContentFactory;
import pl.poznan.put.mail.p2p.engine.pastry.MessageAndHeader;
import pl.poznan.put.mail.p2p.engine.pastry.MessageHeader;
import pl.poznan.put.mail.p2p.engine.services.MailStorringInterruptedException;
import rice.Continuation;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.past.PastContentHandle;
import rice.p2p.scribe.Scribe;
import rice.p2p.scribe.ScribeClient;
import rice.p2p.scribe.ScribeContent;
import rice.p2p.scribe.Topic;
import rice.pastry.NodeIdFactory;
import rice.pastry.commonapi.PastryIdFactory;

/**
 *
 * @author artur
 */
public class PastryMailService implements MailService {

    private static final Logger logger = LogManager.getLogger(PastryMailService.class);
    private MailApplication mailApplication;
    private MailMessagePastContentFactory mailMessagePastContentFactory;
    private final MailAddress address;
    private final Map<Mail, Id> map = new ConcurrentHashMap<Mail, Id>();
    private Set<NewMailListener> mailListener = new HashSet<NewMailListener>();
    private final MailNotificationTopicFactory topicFactory;

    public PastryMailService(MailApplication mailApplication,
            MailMessagePastContentFactory mailMessagePastContentFactory,
            MailAddress address) {
        this.mailApplication = mailApplication;
        this.mailMessagePastContentFactory = mailMessagePastContentFactory;
        this.address = address;
        topicFactory = new MailNotificationTopicFactoryImpl(mailApplication.getNode().getEnvironment());
    }

    protected void registerToScribe() {
        Scribe scribe = mailApplication.getScribe();
        scribe.subscribe(topicFactory.topicFor(address.toString()), new ScribeClient() {

            public boolean anycast(Topic topic, ScribeContent sc) {
                deliver(topic, sc);
                return true;
            }

            public void deliver(Topic topic, ScribeContent sc) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void childAdded(Topic topic, NodeHandle nh) {
            }

            public void childRemoved(Topic topic, NodeHandle nh) {
            }

            public void subscribeFailed(Topic topic) {
                logger.error("subscribe failed for" + topic);
            }
        });
    }

    public Future<MailSendingResult> sendEmail(Mail mail) {
        final MessageAndHeader messageAndHeader = mailMessagePastContentFactory.create(mail);
        final MailSendingResult result = new MailSendingResult();
        final FutureResult<MailSendingResult> futureResult = new FutureResult<MailSendingResult>();
        // insert the data
        mailApplication.getPast().insert(messageAndHeader.getMail(), new Continuation() {
            // the result is an Array of Booleans for each insert

            public void receiveResult(Object response) {
                logger.trace("insert mail " + response);
                try {
                    Boolean[] results = ((Boolean[]) response);
                    int numSuccessfulStores = 0;
                    for (int ctr = 0; ctr < results.length; ctr++) {
                        if (results[ctr].booleanValue()) {
                            numSuccessfulStores++;
                        }
                    }
                    logger.info(messageAndHeader.getMail() + " successfully stored at "
                            + numSuccessfulStores + " locations.");

                    result.setSendContentSuccess(true);
                    if (result.isSendContentFinished() && result.isSendHeaderFinished()) {
                        futureResult.set(result);
                    }
                } catch (Exception ex) {
                    logger.error("Unexpected error on receiveResult continuation", ex);
                }
            }

            public void receiveException(Exception ex) {
                logger.error("Error storing ", ex);

                result.setSendContentException(ex);
                if (result.isSendContentFinished() && result.isSendHeaderFinished()) {
                    futureResult.set(result);
                }
            }
        });

        // insert the data
        mailApplication.getPast().insert(messageAndHeader.getHeader(), new Continuation() {
            // the result is an Array of Booleans for each insert

            public void receiveResult(Object responses) {
                logger.info("insert receiveResult " + responses);
                try {
                    Boolean[] results = ((Boolean[]) responses);
                    int numSuccessfulStores = 0;
                    for (int ctr = 0; ctr < results.length; ctr++) {
                        if (results[ctr].booleanValue()) {
                            numSuccessfulStores++;
                        }
                    }
                    logger.info(messageAndHeader.getHeader() + " successfully stored at "
                            + numSuccessfulStores + " locations.");

                    result.setSendHeaderSuccess(true);
                    if (result.isSendContentFinished() && result.isSendHeaderFinished()) {
                        futureResult.set(result);
                    }
                } catch (Exception ex) {
                    logger.error("Unexpected error on receiveResult continuation", ex);
                }
            }

            public void receiveException(Exception ex) {
                logger.error("Error storing header", ex);
                result.setSendHeaderException(ex);
                if (result.isSendContentFinished() && result.isSendHeaderFinished()) {
                    futureResult.set(result);
                }
            }
        });
        return futureResult;
    }

    public void reciveHeaders(final Continuation<Collection<MessageHeader>, Exception> continuation) {
        Id id = mailMessagePastContentFactory.getIdFor(address);
        mailApplication.getPast().lookupHandles(id, 10, new Continuation<PastContentHandle[], Exception>() {

            public void receiveResult(PastContentHandle[] handles) {
                logger.trace("received handle" + handles);
                int count = 0;
                for (PastContentHandle handle : handles) {
                    if (handle != null) {
                        count++;
                    }
                }
                final Set<MessageHeader> set = new HashSet();
                final Countdown countdown = new Countdown(count, new Continuation() {

                    public void receiveResult(Object r) {
                        continuation.receiveResult(set);

                    }

                    public void receiveException(Exception e) {
                        logger.trace("receive error" + e);
                        continuation.receiveException(e);
                    }
                });
                for (PastContentHandle handle : handles) {
                    if (handle == null) {
                        continue;
                    }
                    mailApplication.getPast().fetch(handle, new Continuation() {

                        public void receiveResult(Object r) {
                            MailMessageHeaderListPastContent msg = (MailMessageHeaderListPastContent) r;
                            synchronized(set) {
                                set.addAll(msg.getHeaders());
                            }
                            countdown.down();
                        }

                        public void receiveException(Exception e) {
                            countdown.down();
                        }
                    });
                }
            }

            public void receiveException(Exception e) {
                logger.trace("receive error" + e);
                continuation.receiveException(e);
            }
        });
        /*
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        mailApplication.getPast().lookup(id, new Continuation() {

            public void receiveResult(Object r) {
                logger.trace("received" + r);
                MailMessageHeaderListPastContent msg = (MailMessageHeaderListPastContent) r;
                continuation.receiveResult(msg.getHeaders());

            }

            public void receiveException(Exception e) {
                logger.trace("receive error" + e);
                continuation.receiveException(e);
            }
        });
         *
         */
    }

    public void reciveMessage(MessageHeader header, final Continuation<Mail, Exception> continuation) {
        mailApplication.getPast().lookup(header.getId(), new Continuation() {

            public void receiveResult(Object r) {
                logger.trace("reciveMessage  result" + r);
                try {
                    MailMessagePastContent content = (MailMessagePastContent) r;
                    continuation.receiveResult(content.getMail());
                } catch (Exception e) {
                    continuation.receiveException(e);
                }
            }

            public void receiveException(Exception e) {
                logger.trace("reciveMessage  exception", e);
                continuation.receiveException(e);
            }
        });
    }

    public void reciveMessagesAsync(final Continuation<Collection<Mail>, Exception> continuation) {
        reciveHeaders(new Continuation<Collection<MessageHeader>, Exception>() {

            public void receiveResult(Collection<MessageHeader> headers) {
                final ArrayList<Mail> mails = new ArrayList<Mail>();
                final Countdown countdown = new Countdown(headers.size(), new Continuation() {

                    public void receiveResult(Object r) {
                        continuation.receiveResult(mails);
                    }

                    public void receiveException(Exception e) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                });
                for (MessageHeader header : headers) {

                    reciveMessage(header, new Continuation<Mail, Exception>() {

                        public void receiveResult(Mail r) {
                            logger.trace("mail received");
                            synchronized (mails) {
                                mails.add(r);
                                countdown.down();
                            }

                        }

                        public void receiveException(Exception e) {
                            logger.error("error! ", e);
                            countdown.down();
                        }
                    });
                }
            }

            public void receiveException(Exception e) {
                logger.error("Error while reciveMessages", e);
                continuation.receiveException(e);
            }
        });
    }

    public Future<Collection<Mail>> reciveMessages() {
        final FutureResult<Collection<Mail>> future = new FutureResult<Collection<Mail>>();
        reciveMessagesAsync(new Continuation<Collection<Mail>, Exception>() {

            public void receiveResult(Collection<Mail> r) {
                logger.trace("recive Mail collection" + r);
                future.set(r);
            }

            public void receiveException(Exception e) {
                logger.error("error",e);
            }
        });
        return future;
    }

    public void remove(Mail mail) {

        
    }

    public void addMailListener(NewMailListener listener) {
        mailListener.add(listener);
    }

    public void removeMailListener(NewMailListener listener) {
        mailListener.remove(listener);
    }


    class Countdown {

        int count;
        Continuation continuation;

        public Countdown(int count, Continuation continuation) {
            this.count = count;
            this.continuation = continuation;


            if (count == 0) {
                continuation.receiveResult(null);
            }
        }

        public synchronized void down() {
            if (--count == 0) {
                continuation.receiveResult(null);
            }
        }
    }
}

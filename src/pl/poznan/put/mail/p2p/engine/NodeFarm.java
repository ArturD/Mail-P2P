/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.poznan.put.mail.p2p.engine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pl.poznan.put.mail.p2p.engine.messages.Mail;
import pl.poznan.put.mail.p2p.engine.messages.MailAddress;
import pl.poznan.put.mail.p2p.engine.pastry.MailApplication;
import pl.poznan.put.mail.p2p.engine.pastry.MailApplicationFactory;
import pl.poznan.put.mail.p2p.engine.pastry.MailMessagePastContentFactory;
import pl.poznan.put.mail.p2p.engine.services.PastryMailService;
import rice.Continuation;
import rice.environment.Environment;
import rice.pastry.NodeIdFactory;
import rice.pastry.standard.RandomNodeIdFactory;

/**
 *
 * @author artur
 */
public class NodeFarm {

    private static final Logger logger = LogManager.getLogger(NodeFarm.class);

    public static void main(String[] args) {
        int count = Integer.parseInt(args[0]);
        logger.info("Starting farm, for" + count + " logger works.");
        final Object lock = new Object();
        final MailApplicationFactory factory = new MailApplicationFactory();
        final int port = 9990;
        final InetSocketAddress bootAddress = new InetSocketAddress("192.168.2.104", port);
        final Environment env = new Environment();
        final NodeIdFactory idFactory = new RandomNodeIdFactory(env);
        for (int i = 0; i < count; i++) {
            final int icopy = i;
            Thread th = new Thread(new Runnable() {

                public void run() {
                    MailApplication app = null;
                    try {
                        if (icopy != 0) {
                            Thread.sleep(500);
                        }
                        app = factory.createApplication(bootAddress, port + icopy, idFactory.generateNodeId());
                        app.boot(bootAddress);
                        if (icopy == 2) {
                            /*
                            app.waitForConnection();
                            Thread.sleep(1500);
                            MailAddress me = new MailAddress("me@me");
                            PastryMailService service = new PastryMailService(app
                            , new MailMessagePastContentFactory(env)
                            , me);
                            Future f = service.sendEmailAsync(
                            new Mail("header", "content", me, me));
                            logger.info(f.get());
                            f = service.sendEmailAsync(
                            new Mail("header2", "content2", me, me));
                            logger.info(f.get());

                            service.reciveMessages(new Continuation<Collection<Mail>, Exception>() {

                            public void receiveResult(Collection<Mail> r) {
                            logger.info("" + r.size() + " Messages");
                            for(Mail mail: r) {
                            logger.info(" mail: " + mail );
                            }
                            }

                            public void receiveException(Exception e) {
                            logger.error("error", e);
                            }
                            });
                             */
                        }
                        synchronized (lock) {
                            while (true) {
                                lock.wait();
                            }
                        }


                    } catch (IOException ex) {
                        logger.error("error in testing thread", ex);
                    } catch (InterruptedException ex) {
                        logger.error("error in testing thread", ex);
                    } finally {
                        logger.info("closing app");
                        if (app != null) {
                            app.getNode().destroy();
                        }
                    }
                }
            });
            th.start();
        }
    }
}

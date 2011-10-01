/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.poznan.put.mail.p2p.engine;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pl.poznan.put.mail.p2p.engine.messages.Mail;
import pl.poznan.put.mail.p2p.engine.messages.MailAddress;
import pl.poznan.put.mail.p2p.engine.pastry.MailApplication;
import pl.poznan.put.mail.p2p.engine.pastry.MailApplicationFactory;
import pl.poznan.put.mail.p2p.engine.pastry.MailMessagePastContentFactory;
import pl.poznan.put.mail.p2p.engine.services.FactoryException;
import pl.poznan.put.mail.p2p.engine.services.MailService;
import pl.poznan.put.mail.p2p.engine.services.PastryMailService;
import pl.poznan.put.mail.p2p.engine.services.PastryMailServiceFactory;
import rice.Continuation;
import rice.environment.Environment;
import rice.pastry.NodeIdFactory;
import rice.pastry.standard.RandomNodeIdFactory;

/**
 *
 * @author artur
 */
public class Main {

    private static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException, FactoryException {
        PastryMailServiceFactory fac = new PastryMailServiceFactory(new MailAddress("me@me")
                ,new InetSocketAddress("192.168.2.104", 9001));
        MailService ms = fac.create();
        logger.info("query for mails");
        Collection<Mail> mails = ms.reciveMessages().get();
        logger.info(mails);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main2(String[] args) {

        logger.info("Starting application, logger works.");
        final Object lock = new Object();
        final MailApplicationFactory factory = new MailApplicationFactory();
        final int port = new Random().nextInt(100) + 13000;
        final InetSocketAddress bootAddress = new InetSocketAddress("192.168.2.104", 9001);
        final Environment env = new Environment();
        final NodeIdFactory idFactory = new RandomNodeIdFactory(env);

        MailApplication app = null;
        try {
            app = factory.createApplication(bootAddress, port, idFactory.generateNodeId());
            app.boot(bootAddress);
            app.waitForConnection();
            Thread.sleep(1500);
            MailAddress me = new MailAddress("me@me");
            PastryMailService service = new PastryMailService(app, new MailMessagePastContentFactory(env), me);
            Future f = service.sendEmail(
                    new Mail("header5", "content", me, me));
            logger.info(f.get());
            f = service.sendEmail(
                    new Mail("header6", "content2", me, me));
            logger.info(f.get());

            service.reciveMessagesAsync(new Continuation<Collection<Mail>, Exception>() {

                public void receiveResult(Collection<Mail> r) {
                    logger.info("" + r.size() + " Messages");
                    for (Mail mail : r) {
                        logger.info(" mail: " + mail);
                    }
                }

                public void receiveException(Exception e) {
                    logger.error("error", e);
                }
            });
            Thread.sleep(100000);

        } catch (ExecutionException ex) {
            logger.error("error in testing thread", ex);
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
}

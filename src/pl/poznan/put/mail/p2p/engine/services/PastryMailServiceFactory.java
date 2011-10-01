/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.poznan.put.mail.p2p.engine.services;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pl.poznan.put.mail.p2p.engine.messages.Mail;
import pl.poznan.put.mail.p2p.engine.messages.MailAddress;
import pl.poznan.put.mail.p2p.engine.pastry.MailApplication;
import pl.poznan.put.mail.p2p.engine.pastry.MailApplicationFactory;
import pl.poznan.put.mail.p2p.engine.pastry.MailMessagePastContentFactory;
import rice.environment.Environment;
import rice.pastry.Id;

/**
 *
 * @author artur
 */
public class PastryMailServiceFactory {

    private static final Logger logger = LogManager.getLogger(PastryMailServiceFactory.class);
    private final MailApplicationFactory mailApplicationFactory = new MailApplicationFactory();
    private InetSocketAddress bootAddress;
    private int port = 14000 + new Random().nextInt(100);
    private MailAddress mailAddress;
    private final MailMessagePastContentFactory mailMessagePastContentFactory;

    public PastryMailServiceFactory(MailAddress mail,InetSocketAddress bootAddress) {
        Environment env = new Environment();
        this.mailAddress = mail;
        mailMessagePastContentFactory = new MailMessagePastContentFactory(env);
        this.bootAddress = bootAddress;
    }

    public MailService create() throws FactoryException {
        logger.trace("creating MailService");
        MailApplication app;
        try {
            
            app = mailApplicationFactory.createApplication(bootAddress, port, generateNodeId(mailAddress));
            logger.info("booting" + bootAddress);
            app.boot(bootAddress);
            app.waitForConnection();
        } catch (IOException ex) {
            logger.error("cannot create MailService", ex);
            throw new FactoryException("cannot create MailService", ex);
        } catch (InterruptedException ex) {
            logger.error("cannot create MailService", ex);
            throw new FactoryException("cannot create MailService", ex);
        }
        return new PastryMailService(app, mailMessagePastContentFactory, mailAddress);
    }

    protected Id generateNodeId(MailAddress mailAddress) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            logger.error("No SHA support!");
            throw new RuntimeException("No SHA support!", e);
        }


        md.update(mailAddress.toString().getBytes());
        byte[] digest = md.digest();

        Id nodeId = Id.build(digest);

        return nodeId;

    }
}

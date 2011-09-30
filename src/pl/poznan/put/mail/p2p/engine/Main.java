/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pl.poznan.put.mail.p2p.engine.pastry.MailApplication;
import pl.poznan.put.mail.p2p.engine.pastry.MailApplicationFactory;

/**
 *
 * @author artur
 */
public class Main {
    private static Logger logger = LogManager.getLogger(Main.class);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        logger.info("Starting application, logger works.");
        final Object lock = new Object();
        final MailApplicationFactory factory = new MailApplicationFactory();
        final int port = 9980;
        final InetSocketAddress bootAddress = new InetSocketAddress("localhost", port);

        for(int i=0;i<3; i++) {
            final int icopy = i;
            Thread th = new Thread(new Runnable() {

                public void run() {
                    MailApplication app = null;
                    try {
                        app = factory.createApplication(bootAddress, port+icopy);
                        app.boot(bootAddress);
                        if(icopy == 0) {
                            app.waitForConnection();
                            Thread.sleep(1000);
                            app.sendRandomMessages(3);
                        }
                        synchronized(lock) {
                            while(true) lock.wait();
                        }

                    } catch (IOException ex) {
                        logger.error("error in testing thread", ex);
                    } catch (InterruptedException ex) {
                        logger.error("error in testing thread", ex);
                    } finally {
                        logger.info("closing app");
                        if(app!=null) app.getNode().destroy();
                    }
                }
            });
            th.start();
        }
    }

}

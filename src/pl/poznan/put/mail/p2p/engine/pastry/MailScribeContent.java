/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.pastry;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pl.poznan.put.mail.p2p.engine.messages.Mail;
import rice.p2p.scribe.ScribeContent;

/**
 *
 * @author artur
 */
public class MailScribeContent implements ScribeContent {
    private static final Logger logger = LogManager.getLogger(MailScribeContent.class);
    private Mail mail;

    public MailScribeContent(Mail mail) {
        this.mail = mail;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.pastry;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pl.poznan.put.mail.p2p.engine.messages.Mail;
import rice.p2p.commonapi.Id;
import rice.p2p.past.ContentHashPastContent;
import rice.p2p.past.PastContent;
import rice.p2p.past.PastException;

/**
 *
 * @author artur
 */
public class MailMessagePastContent extends ContentHashPastContent {
    private static final Logger logger = LogManager.getLogger(MailMessagePastContent.class);
    private Mail mail;

    public MailMessagePastContent(Id myId, Mail mail) {
        super(myId);
        this.mail = mail;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    @Override
    public PastContent checkInsert(Id id, PastContent existingContent) throws PastException {
        MailMessagePastContent existing = (MailMessagePastContent) existingContent;
        if(existing.mail == null || mail == null)
            return null;
        return this;
    }
}

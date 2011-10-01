/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.pastry;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author artur
 */
public class MessageAndHeader {
    private static final Logger logger = LogManager.getLogger(MessageAndHeader.class);
    private MailMessagePastContent mail;
    private MailMessageHeaderListPastContent header;

    public MessageAndHeader(MailMessagePastContent mail, MailMessageHeaderListPastContent header) {
        this.mail = mail;
        this.header = header;
    }

    public MailMessageHeaderListPastContent getHeader() {
        return header;
    }

    public void setHeader(MailMessageHeaderListPastContent header) {
        this.header = header;
    }

    public MailMessagePastContent getMail() {
        return mail;
    }

    public void setMail(MailMessagePastContent mail) {
        this.mail = mail;
    }
}

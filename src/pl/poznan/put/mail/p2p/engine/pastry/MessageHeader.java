/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.pastry;

import java.io.Serializable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pl.poznan.put.mail.p2p.engine.messages.MailAddress;
import rice.p2p.commonapi.Id;

/**
 *
 * @author artur
 */
public class MessageHeader implements Serializable {
    private static final Logger logger = LogManager.getLogger(MessageHeader.class);
    private Id id;
    private MailAddress to;

    public MessageHeader(Id id, MailAddress to) {
        this.id = id;
        this.to = to;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public MailAddress getTo() {
        return to;
    }

    public void setTo(MailAddress to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MessageHeader other = (MessageHeader) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.to != other.to && (this.to == null || !this.to.equals(other.to))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 79 * hash + (this.to != null ? this.to.hashCode() : 0);
        return hash;
    }

    
}

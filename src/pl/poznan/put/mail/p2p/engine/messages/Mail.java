/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.messages;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author artur
 */
public class Mail implements Serializable {
    private static final Logger logger = LogManager.getLogger(Mail.class);
    private String header;
    private String content;
    private Date timestamp;
    private MailAddress from;
    private MailAddress to;

    public Mail(String header, String content, MailAddress from, MailAddress to,Date timestamp) {
        this.header = header;
        this.content = content;
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
    }
    public Mail(String header, String content, MailAddress from, MailAddress to) {
        this(header, content,from,to, Calendar.getInstance().getTime());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MailAddress getFrom() {
        return from;
    }

    public void setFrom(MailAddress from) {
        this.from = from;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public MailAddress getTo() {
        return to;
    }

    public void setTo(MailAddress to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Mail{" +"to=" + to +  "header=" + header + "from=" + from + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mail other = (Mail) obj;
        if ((this.header == null) ? (other.header != null) : !this.header.equals(other.header)) {
            return false;
        }
        if ((this.content == null) ? (other.content != null) : !this.content.equals(other.content)) {
            return false;
        }
        if (this.timestamp != other.timestamp && (this.timestamp == null || !this.timestamp.equals(other.timestamp))) {
            return false;
        }
        if (this.from != other.from && (this.from == null || !this.from.equals(other.from))) {
            return false;
        }
        if (this.to != other.to && (this.to == null || !this.to.equals(other.to))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.header != null ? this.header.hashCode() : 0);
        hash = 89 * hash + (this.content != null ? this.content.hashCode() : 0);
        hash = 89 * hash + (this.timestamp != null ? this.timestamp.hashCode() : 0);
        hash = 89 * hash + (this.from != null ? this.from.hashCode() : 0);
        hash = 89 * hash + (this.to != null ? this.to.hashCode() : 0);
        return hash;
    }


}

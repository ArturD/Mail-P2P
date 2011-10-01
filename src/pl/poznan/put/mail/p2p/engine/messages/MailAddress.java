/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.messages;

import java.io.Serializable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author artur
 */
public class MailAddress implements Serializable {
    private static final Logger logger = LogManager.getLogger(MailAddress.class);
    private String name;
    private String host;

    public MailAddress(String mail) {
        String[] parts = mail.split("@");
        if(parts.length != 2) throw new IllegalArgumentException("Mail (" + mail + ") is illegal");
        name = parts[0];
        host = parts[1];
    }

    public MailAddress(String name, String host) {
        this.name = name;
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + "@" + host;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MailAddress other = (MailAddress) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.host == null) ? (other.host != null) : !this.host.equals(other.host)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 71 * hash + (this.host != null ? this.host.hashCode() : 0);
        return hash;
    }
}

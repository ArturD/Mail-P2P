/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.pastry;

import java.util.HashSet;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pl.poznan.put.mail.p2p.engine.messages.MailAddress;
import rice.p2p.commonapi.Id;
import rice.p2p.past.ContentHashPastContent;
import rice.p2p.past.PastContent;
import rice.p2p.past.PastException;

/**
 *
 * @author artur
 */
public class MailMessageHeaderListPastContent extends ContentHashPastContent {
    private static final Logger logger = LogManager.getLogger(MailMessageHeaderListPastContent.class);
     private HashSet<MessageHeader> headers;

    public MailMessageHeaderListPastContent(Id myId, Id contentId, MailAddress to) {
        super(myId);
        headers = new HashSet();

        headers.add(new MessageHeader(contentId, to));
    }

    public MailMessageHeaderListPastContent(Id myId, HashSet<MessageHeader> headers) {
        super(myId);
        this.headers = headers;
    }

    public HashSet<MessageHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(HashSet<MessageHeader> headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return "MailMessageHeaderListPastContent{" + "headers=" + headers.size() + '}';
    }

    @Override
    public PastContent checkInsert(Id id, PastContent existingContent) throws PastException {
        if(existingContent == null) return this;
        HashSet set = ((MailMessageHeaderListPastContent)existingContent).getHeaders();
        set.addAll(headers);
        return new MailMessageHeaderListPastContent(id,  set);
    }


}

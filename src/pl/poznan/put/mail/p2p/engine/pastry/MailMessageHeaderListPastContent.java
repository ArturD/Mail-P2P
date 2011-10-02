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
     private HashSet<MessageHeader> toAdd;
     private HashSet<MessageHeader> toRemove;

    public MailMessageHeaderListPastContent(Id myId, Id contentId, MailAddress to) {
        super(myId);
        toAdd = new HashSet();

        toAdd.add(new MessageHeader(contentId, to));
    }

    public MailMessageHeaderListPastContent(Id myId, HashSet<MessageHeader> headers) {
        super(myId);
        this.toAdd = headers;
    }

    public HashSet<MessageHeader> getHeaders() {
        return toAdd;
    }

    public void setHeaders(HashSet<MessageHeader> headers) {
        this.toAdd = headers;
    }

    @Override
    public String toString() {
        return "MailMessageHeaderListPastContent{" + "headers=" + toAdd.size() + '}';
    }

    @Override
    public PastContent checkInsert(Id id, PastContent existingContent) throws PastException {
        if(existingContent == null) return this;
        logger.info("merging headers " + this + "  to  " + existingContent);
        HashSet set = ((MailMessageHeaderListPastContent)existingContent).getHeaders();
        set.addAll(toAdd);
        set.remove(((MailMessageHeaderListPastContent)existingContent).toRemove);
        set.removeAll(toRemove);
        return new MailMessageHeaderListPastContent(id,  set);
    }


}

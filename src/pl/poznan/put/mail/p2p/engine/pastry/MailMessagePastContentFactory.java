/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.pastry;

import java.util.UUID;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pl.poznan.put.mail.p2p.engine.messages.Mail;
import pl.poznan.put.mail.p2p.engine.messages.MailAddress;
import rice.environment.Environment;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.IdFactory;
import rice.pastry.commonapi.PastryIdFactory;

/**
 *
 * @author artur
 */
public class MailMessagePastContentFactory {
    private static final Logger logger = LogManager.getLogger(MailMessagePastContentFactory.class);
    private final IdFactory idFactory;

    public MailMessagePastContentFactory(IdFactory idFactory) {
        this.idFactory = idFactory;
    }

    public MailMessagePastContentFactory(Environment env) {
        this.idFactory = new PastryIdFactory(env);
    }



    public MessageAndHeader create(Mail message) {
        Id contentId = idFactory.buildIdFromToString(UUID.randomUUID().toString());
        MailMessagePastContent content = new MailMessagePastContent(
                contentId
                , message);
        MailMessageHeaderListPastContent header =
                new MailMessageHeaderListPastContent(
                    idFactory.buildIdFromToString(message.getTo().toString())
                    , content.getId()
                    , content.getMail().getTo());

        return new MessageAndHeader(content, header);
    }
    public MessageAndHeader createRevoke(Id contentId, MailAddress to) {
        MailMessagePastContent content = new MailMessagePastContent(
                contentId
                , null);
        MailMessageHeaderListPastContent header =
                new MailMessageHeaderListPastContent(
                    idFactory.buildIdFromToString(to.toString())
                    , content.getId()
                    , content.getMail().getTo());

        return new MessageAndHeader(content, header);
    }
    public Id getIdFor(MailAddress address) {
        return idFactory.buildIdFromToString(address.toString());
    }
}

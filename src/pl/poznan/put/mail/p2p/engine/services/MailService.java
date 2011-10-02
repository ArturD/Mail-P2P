/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.services;

import java.util.Collection;
import java.util.concurrent.Future;
import pl.poznan.put.mail.p2p.engine.messages.Mail;
import pl.poznan.put.mail.p2p.engine.pastry.MessageHeader;
import rice.Continuation;

/**
 *
 * @author artur
 */
public interface MailService {

    void reciveMessagesAsync(final Continuation<Collection<Mail>, Exception> continuation);

    Future<Collection<Mail>> reciveMessages();

    Future<MailSendingResult> sendEmail(Mail mail);

    void remove(Mail mail);

    void addMailListener(NewMailListener listener);

    void removeMailListener(NewMailListener listener);

    public void destroy();
}

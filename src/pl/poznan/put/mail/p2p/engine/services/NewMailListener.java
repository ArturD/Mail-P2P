/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.services;

import pl.poznan.put.mail.p2p.engine.messages.Mail;

/**
 *
 * @author artur
 */
public interface NewMailListener {
    void onMail(Mail mail);
}

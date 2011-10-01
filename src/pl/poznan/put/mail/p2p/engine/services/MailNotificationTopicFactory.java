/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.services;

import rice.p2p.scribe.Topic;

/**
 *
 * @author artur
 */
public interface MailNotificationTopicFactory {

    public Topic topicFor(String address);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.services;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import rice.environment.Environment;
import rice.p2p.scribe.Topic;
import rice.pastry.commonapi.PastryIdFactory;

/**
 *
 * @author artur
 */
public class MailNotificationTopicFactoryImpl implements MailNotificationTopicFactory {
    private static final Logger logger = LogManager.getLogger(MailNotificationTopicFactoryImpl.class);
    private final Environment env;


    MailNotificationTopicFactoryImpl(Environment environment) {
        env = environment;
    }

    public Topic topicFor(String address) {
        return new Topic(new PastryIdFactory(env), "MailNotification_" + address);
    }

}

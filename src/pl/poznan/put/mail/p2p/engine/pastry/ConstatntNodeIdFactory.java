/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.pastry;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import rice.environment.Environment;
import rice.pastry.Id;
import rice.pastry.NodeIdFactory;

/**
 *
 * @author artur
 */
class ConstatntNodeIdFactory implements NodeIdFactory {
    private static final Logger logger = LogManager.getLogger(ConstatntNodeIdFactory.class);
    private final Id id;
    public ConstatntNodeIdFactory(Id Id) {
        this.id = Id;
    }

    public Id generateNodeId() {
        return id;
    }

}

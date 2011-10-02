/*******************************************************************************

"FreePastry" Peer-to-Peer Application Development Substrate

Copyright 2002-2007, Rice University. Copyright 2006-2007, Max Planck Institute 
for Software Systems.  All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

- Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.

- Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.

- Neither the name of Rice  University (RICE), Max Planck Institute for Software 
Systems (MPI-SWS) nor the names of its contributors may be used to endorse or 
promote products derived from this software without specific prior written 
permission.

This software is provided by RICE, MPI-SWS and the contributors on an "as is" 
basis, without any representations or warranties of any kind, express or implied 
including, but not limited to, representations or warranties of 
non-infringement, merchantability or fitness for a particular purpose. In no 
event shall RICE, MPI-SWS or contributors be liable for any direct, indirect, 
incidental, special, exemplary, or consequential damages (including, but not 
limited to, procurement of substitute goods or services; loss of use, data, or 
profits; or business interruption) however caused and on any theory of 
liability, whether in contract, strict liability, or tort (including negligence
or otherwise) arising in any way out of the use of this software, even if 
advised of the possibility of such damage.

 *******************************************************************************/
/*
 * Created on Feb 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pl.poznan.put.mail.p2p.engine.pastry;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import rice.environment.Environment;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.Node;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.p2p.past.Past;
import rice.p2p.scribe.Scribe;
import rice.p2p.scribe.ScribeImpl;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.leafset.LeafSet;
import rice.pastry.standard.RandomNodeIdFactory;

/**
 * A very simple application.
 * 
 * @author Jeff Hoye
 */
public class MailApplication implements Application {

    private static final Logger logger = LogManager.getLogger(MailApplication.class);
    protected final Endpoint endpoint;
    private final PastryNode node;
    private final Environment env;
    private final Past past;
    private final Scribe scribe;

    public MailApplication(PastryNode node, Environment env, Past past) {
        this.node = node;
        this.env = env;
        this.past = past;

        this.endpoint = node.buildEndpoint(this, "myinstance");

        this.scribe = new ScribeImpl(node,"p2pMail.ScribeInstance");
        this.endpoint.register();
    }

    /**
     * Called to route a message to the id
     
    public void routeMyMsg(Id id) {
        logger.trace(this + " sending to " + id);
        Message msg = new MyMsg(endpoint.getId(), id);
        endpoint.route(id, msg, null);
    }

    /**
     * Called to directly send a message to the nh
     
    public void routeMyMsgDirect(NodeHandle nh) {
        logger.trace(this + " sending direct to " + nh);
        Message msg = new MyMsg(endpoint.getId(), nh.getId());
        endpoint.route(null, msg, nh);
    }
*/
    /**
     * Called when we receive a message.
     */
    public void deliver(Id id, Message message) {
        logger.trace(this + " received " + message);
    }

    /**
     * Called when you hear about a new neighbor.
     */
    public void update(NodeHandle handle, boolean joined) {
        logger.trace("" + handle + " joined:" + joined);
    }

    /**
     * Called a message travels along your path.
     * Don't worry about this method for now.
     */
    public boolean forward(RouteMessage message) {
        logger.trace("forwarding message" + message);
        return true;
    }

    @Override
    public String toString() {
        return "MailApplication " + endpoint.getId();
    }

    public void boot(InetSocketAddress bootaddress) {
        logger.trace("boot " + bootaddress);
        node.boot(bootaddress);
    }

    public PastryNode getNode() {
        return node;
    }

    public void waitForConnection() throws IOException, InterruptedException {
        logger.trace("waiting to join pastry");
        // the node may require sending several messages to fully boot into the ring
        synchronized (node) {
            while (!node.isReady() && !node.joinFailed()) {
                // delay so we don't busy-wait
                node.wait(500);

                // abort if can't join
                if (node.joinFailed()) {
                    throw new IOException("Could not join the FreePastry ring.  Reason:" + node.joinFailedReason());
                }
            }
        }
        logger.trace("joined pastry");
    }

    public Scribe getScribe() {
        return scribe;
    }
/*
    public void sendRandomMessages(int count) {
        try {
            NodeIdFactory nidFactory = new RandomNodeIdFactory(env);
            // route 10 messages
            for (int i = 0; i < count; i++) {
                // pick a key at random
                Id randId = nidFactory.generateNodeId();
                // send to that key
                this.routeMyMsg(randId);
                // wait a sec
                env.getTimeSource().sleep(1000);
            }
            // wait 10 seconds
            env.getTimeSource().sleep(10000);
            // send directly to my leafset
            LeafSet leafSet = node.getLeafSet();
            // this is a typical loop to cover your leafset.  Note that if the leafset
            // overlaps, then duplicate nodes will be sent to twice
            for (int i = -leafSet.ccwSize(); i <= leafSet.cwSize(); i++) {
                if (i != 0) {
                    // don't send to self
                    // select the item
                    NodeHandle nh = leafSet.get(i);
                    // send the message directly to the node
                    this.routeMyMsgDirect(nh);
                    // wait a sec
                    env.getTimeSource().sleep(1000);
                }
            }
        } catch (InterruptedException ex) {
            logger.error("Error while sending random messages.", ex);
        }
    }
*/
    

    public Past getPast() {
        return past;
    }
}

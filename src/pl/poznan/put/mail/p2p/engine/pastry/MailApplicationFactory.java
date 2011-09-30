/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.poznan.put.mail.p2p.engine.pastry;

import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import rice.environment.Environment;
import rice.p2p.commonapi.*;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;
import rice.pastry.leafset.LeafSet;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.*;

/**
 *
 * @author artur
 */
public class MailApplicationFactory {
    private static final Logger logger = LogManager.getLogger(MailApplication.class);
    private final Environment env;

    public MailApplicationFactory() {
        env = new Environment();

        // disable the UPnP setting (in case you are testing this on a NATted LAN)
        env.getParameters().setString("nat_search_policy","never");
    }


    public MailApplication createApplication(InetSocketAddress bootaddress, int bindport) throws IOException, InterruptedException {
        logger.trace("factoring mail application");
        // Generate the NodeIds Randomly
        NodeIdFactory nidFactory = new RandomNodeIdFactory(env);

        // construct the PastryNodeFactory, this is how we use rice.pastry.socket
        PastryNodeFactory factory = new SocketPastryNodeFactory(nidFactory, bindport, env);
        
        // construct a node
        PastryNode node = factory.newNode();

        // construct a new MyApp
        MailApplication app = new MailApplication(node, env);


        return app;
    }
}

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
import rice.p2p.past.Past;
import rice.p2p.past.PastImpl;
import rice.p2p.scribe.Scribe;
import rice.p2p.scribe.ScribeImpl;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;
import rice.pastry.commonapi.PastryIdFactory;
import rice.pastry.leafset.LeafSet;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.*;
import rice.persistence.LRUCache;
import rice.persistence.MemoryStorage;
import rice.persistence.PersistentStorage;
import rice.persistence.Storage;
import rice.persistence.StorageManagerImpl;

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
        //env.getParameters().setString("nat_search_policy","never");
    }


    public MailApplication createApplication(InetSocketAddress bootaddress, int bindport, rice.pastry.Id nodeId) throws IOException, InterruptedException {
        logger.trace("factoring mail application");
        // Generate the NodeIds Randomly
        NodeIdFactory nidFactory = new ConstatntNodeIdFactory(nodeId);

        // construct the PastryNodeFactory, this is how we use rice.pastry.socket
        PastryNodeFactory factory = new SocketPastryNodeFactory(nidFactory, bindport, env);
        
        // construct a node
        PastryNode node = factory.newNode();

          // used for generating PastContent object Ids.
          // this implements the "hash function" for our DHT
          PastryIdFactory idf = new rice.pastry.commonapi.PastryIdFactory(env);

          // create a different storage root for each node
          String storageDirectory = "./storage"+node.getId().hashCode();

          // create the persistent part
          Storage stor = new PersistentStorage(idf, storageDirectory, 4 * 1024 * 1024, node
              .getEnvironment());
          logger.trace("creating Past");
          Past past = new PastImpl(node, new StorageManagerImpl(idf, stor, new LRUCache(
              new MemoryStorage(idf), 512 * 1024, node.getEnvironment())), 3, "");


        

        // construct a new MyApp
        MailApplication app = new MailApplication(node, env, past);


        return app;
    }
}

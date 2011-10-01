/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.services;

import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author artur
 */
public class FactoryException extends Exception {
    private static final Logger logger = LogManager.getLogger(FactoryException.class);

    public FactoryException(String message, Exception ex) {
        super(message, ex);
    }

}

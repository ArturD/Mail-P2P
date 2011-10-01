/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.services;

import java.util.concurrent.TimeoutException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author artur
 */
class MailStorringInterruptedException extends InterruptedException {
    private static final Logger logger = LogManager.getLogger(MailStorringInterruptedException.class);
    TimeoutException exception;

    public MailStorringInterruptedException(String string, TimeoutException ex) {
        super(string);
        exception = ex;
    }

    @Override
    public Throwable getCause() {
        return exception;
    }
}

package pl.poznan.put.mail.p2p.engine.services;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

class FutureResult<T> implements Future<T> {
    private static final Logger logger = LogManager.getLogger(FutureResult.class);

    private boolean done;
    private T result;

    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return done;
    }

    public synchronized void set(T result) {
        this.result = result;
        notifyAll();
        done = true;
    }

    public T get() throws InterruptedException, ExecutionException {
        try {
            return get(3, TimeUnit.MINUTES);
        } catch (TimeoutException ex) {
            logger.info("timeout", ex);
            throw new MailStorringInterruptedException("interrupted", ex);
        }
    }

    public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long start = System.currentTimeMillis();
        long waitMiliseconds = unit.toMillis(timeout);
        while (System.currentTimeMillis() - start < waitMiliseconds) {
            wait(waitMiliseconds - (System.currentTimeMillis() - start));
            if (done) {
                return result;
            }
        }
        throw new TimeoutException("Waiting for result timeouted.");
    }
}

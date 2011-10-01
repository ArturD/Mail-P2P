/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.services;

/**
 *
 * @author artur
 */
public class MailSendingResult {
    boolean sendContentFinished = false;
    boolean sendHeaderFinished = false;
    boolean sendContentSuccess = false;
    boolean sendHeaderSuccess = false;
    Exception sendHeaderException;
    Exception sendContentException;



    public boolean isSendHeaderSuccess() {
        return sendHeaderSuccess;
    }

    public void setSendHeaderSuccess(boolean sendHeaderSuccess) {
        sendHeaderFinished = true;
        this.sendHeaderSuccess = sendHeaderSuccess;
    }

    public Exception getSendContentException() {
        return sendContentException;
    }

    public void setSendContentException(Exception sendContentException) {
        sendContentFinished = true;
        this.sendContentException = sendContentException;
    }

    public boolean isSendContentSuccess() {
        return sendContentSuccess;
    }

    public void setSendContentSuccess(boolean sendContentSuccess) {
        sendContentFinished = true;
        this.sendContentSuccess = sendContentSuccess;
    }

    public Exception getSendHeaderException() {
        return sendHeaderException;
    }

    public void setSendHeaderException(Exception sendHeaderException) {
        sendHeaderFinished = true;
        this.sendHeaderException = sendHeaderException;
    }

    public boolean isSendContentFinished() {
        return sendContentFinished;
    }

    public boolean isSendHeaderFinished() {
        return sendHeaderFinished;
    }

    @Override
    public String toString() {
        return "MailSendingResult{" + "sendContentFinished=" + sendContentFinished + " sendHeaderFinished=" + sendHeaderFinished + "sendContentSuccess=" + sendContentSuccess + "sendHeaderSuccess=" + sendHeaderSuccess + "sendHeaderException=" + sendHeaderException + "sendContentException=" + sendContentException + '}';
    }


}

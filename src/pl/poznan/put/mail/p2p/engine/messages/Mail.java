/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.poznan.put.mail.p2p.engine.messages;

import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;

/**
 *
 * @author artur
 */
public class Mail implements Message {
  Id from;
  Id to;

  /**
   * Constructor.
   */
  public Mail(Id from, Id to) {
    this.from = from;
    this.to = to;
  }

  public String toString() {
    return "mail from "+from+" to "+to;
  }

  /**
   * Use low priority to prevent interference with overlay maintenance traffic.
   */
  public int getPriority() {
    return Message.LOW_PRIORITY;
  }

}

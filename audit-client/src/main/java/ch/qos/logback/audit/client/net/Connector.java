/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 2006-2011, QOS.ch. All rights reserved.
 * 
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *  
 *   or (per the licensee's choosing)
 *  
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */

package ch.qos.logback.audit.client.net;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.spi.ContextAwareBase;

public class Connector extends ContextAwareBase implements Runnable {

  Logger logger = LoggerFactory.getLogger(Connector.class);
  
  SocketAuditAppender socketAuditAppender;
  boolean closed = false;
  Thread thread;
  
  Connector(SocketAuditAppender socketAuditAppender) {
    this.socketAuditAppender = socketAuditAppender;
  }

  
  public Thread getThread() {
    return thread;
  }

  public void setThread(Thread thread) {
    this.thread = thread;
  }

  public void setClosed(boolean closed) {
    this.closed = closed;
  }


  public void run() {

    addInfo("Entering Connector.run() method.");

    Socket socket;

    while (!closed) {
      try {
        Thread.sleep(socketAuditAppender.getReconnectionDelay());
        // if the connector has been closed while we were sleeping
        // there is no need to attempt a connection to the remote
        // host
        if(closed) {
          break;
        }
        socket = new Socket(socketAuditAppender.getAddress(),
            socketAuditAppender.getPort());
        socketAuditAppender.connect(socket);
        // Connection established. Exiting connector thread.
        break;
      } catch (InterruptedException e) {
        return;
      } catch (java.net.ConnectException e) {
        // continue
      } catch (IOException e) {
        // continue
      }
    }
    addInfo("Exiting Connector.run() method.");
  }

}

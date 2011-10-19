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

package ch.qos.logback.audit.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.audit.AuditEvent;

public class SocketNode implements Runnable {

  final AuditServer auditServer;
  final Socket socket;
  final AuditEventHandler aeh;

  ObjectOutputStream oos;
  ObjectInputStream ois;
  int writeCount = 0;
  boolean closed = false;

  static final int RESET_FREQUENCY = 1000;

  static Logger logger = (Logger) LoggerFactory.getLogger(SocketNode.class);

  public SocketNode(AuditServer auditServer, Socket socket,
      AuditEventHandler aeh) {
    this.auditServer = auditServer;
    this.socket = socket;
    this.aeh = aeh;

    try {
      ois = new ObjectInputStream(socket.getInputStream());
      oos = new ObjectOutputStream(socket.getOutputStream());
    } catch (Exception e) {
      logger.error("Could not open ObjectInputStream to " + socket, e);
    }
  }

  void close() {
    if(closed) {
      return;
    }
    closed = true;
    if (ois != null) {
      try {
        ois.close();
        ois = null;
      } catch (IOException e) {
        logger.warn("While in close method caught: " + e.getMessage());
      }
    }
  }

  public void run() {
    AuditEvent event = null;

    // try {
    while (!closed) {
      // read an event from the wire
      try {
        event = (AuditEvent) ois.readObject();

      } catch (java.io.EOFException e) {
        logger.info("Caught java.io.EOFException closing connection.");
        break;
      } catch (IOException e) {
        logger.info("Caught: "+e.getMessage());
        break;
      } catch (ClassNotFoundException e) {
        logger.error("Unexpected ClassNotFoundException.", e);
        writeResponse(e);
        break;
      }

      Object outgoingObject;
      try {
        aeh.doHandle(event);
        outgoingObject = new Boolean(true);
      } catch (Exception e) {
        logger.error("Problem while handling audit event", e);
        outgoingObject = e;
      }

      logger.trace("Outgoing object is {}", outgoingObject);
      writeResponse(outgoingObject);

    }
    auditServer.socketNodeClosing(this);
  }

  private void writeResponse(Object outgoingObject) {
    try {
      if (writeCount++ >= RESET_FREQUENCY) {
        writeCount = 0;
        System.out.print("r");
        oos.reset();
      }
      oos.writeObject(outgoingObject);
    } catch (IOException e) {
      logger.error("Failed to send acknowledgement", e);
    }
  }

  @Override
  public String toString() {
    if (socket != null) {
      return "SocketNode to " + socket.getRemoteSocketAddress();
    } else {
      return "SocketNode to null socket";
    }
  }
}

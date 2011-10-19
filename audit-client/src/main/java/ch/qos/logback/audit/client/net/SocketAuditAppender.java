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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import ch.qos.logback.audit.AuditEvent;
import ch.qos.logback.audit.AuditException;
import ch.qos.logback.audit.InternalAuditContants;
import ch.qos.logback.audit.client.AuditAppenderBase;

public class SocketAuditAppender extends AuditAppenderBase {

  static final String NO_HOST_URL = InternalAuditContants.CODES_URL
      + "#NO_HOST_URL";

  static final String FAILIED_WRITE_URL = InternalAuditContants.CODES_URL
      + "#FAILIED_WRITE_URL";

  /**
   * The default port number of remote logging server (9630).
   */
  static final int DEFAULT_PORT = 9630;

  private String remoteHost;

  private InetAddress address;
  private int port = DEFAULT_PORT;

  private ObjectOutputStream oos;
  private ObjectInputStream ois;

  private int reconnectionDelay = 2000;

  protected int counter = 0;

  // reset the ObjectOutputStream every RESET_FREQUENCY calls
  private static final int RESET_FREQUENCY = 200;

  Connector connector;

  /**
   * Start this appender.
   */
  public void start() {
    int errorCount = 0;
    if (port == 0) {
      errorCount++;
      addError("No port was configured for appender" + name);
    }

    if (address == null) {
      errorCount++;
      addError("No remote address was configured for appender " + name);
    }

    if (errorCount == 0) {
      connect(address, port);
    }
  }

  @Override
  public void stop() {
    super.stop();
    cleanUp();
    if (connector != null) {
      connector.setClosed(true);
      Thread connectorThread = connector.getThread();
      try {
        connectorThread.join(5000);
      } catch (InterruptedException e) {
        addError("Failed to join connector thread", e);
      }
      connector = null;
    }

  }

  void connect(Socket socket) {
    try {
      // First, close the previous connection if any.
      cleanUp();

      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
      super.started = true;
    } catch (IOException e) {
      String msg = "Failed to open a stream on the socket for remmote logback server at ["
          + address.getHostName() + "] at port " + port;
      addError(msg, e);
    }
  }

  void connect(InetAddress address, int port) {
    try {
      // First, close the previous connection if any.
      cleanUp();
      Socket socket = new Socket(address, port);
      connect(socket);
    } catch (IOException e) {
      String msg = "Could not connect to remote logback server at ["
          + address.getHostName() + "] at port " + port;
      addError(msg, e);
      if (reconnectionDelay > 0) {
        fireConnector();
      }
    }
  }

  /**
   * Drop the connection to the remote host and release the underlying connector
   * thread if it has been created
   */
  public void cleanUp() {
    if (oos != null) {
      try {
        oos.close();
      } catch (IOException e) {
        addError("Could not close oos.", e);
      }
      oos = null;
    }
    if (ois != null) {
      try {
        ois.close();
      } catch (IOException e) {
        addError("Could not close oos.", e);
      }
      ois = null;
    }
  }

  @Override
  protected void append(AuditEvent auditEvent) throws AuditException {

    if(!started) {
      return;
    }
    
    if (auditEvent == null)
      return;

    if (oos != null) {
      try {
        oos.writeObject(auditEvent);
        oos.flush();

        if (++counter >= RESET_FREQUENCY) {
          counter = 0;
          // Failing to reset the object output stream every now and
          // then creates a serious memory leak.
          oos.reset();
        }
      } catch (IOException e) {
        oos = null;
        this.started = false;
        fireConnector();
        throw new AuditException("Failed sending audit event to host \""
            + remoteHost + "\" down. For more information, please visit "
            + FAILIED_WRITE_URL, e);
      }
    }

    try {
      Object incoming = ois.readObject();
      if (incoming instanceof Exception) {
        if (incoming instanceof AuditException) {
          AuditException ae = (AuditException) incoming;
          throw ae;
        } else {
          throw new AuditException("Server incurred an exception",
              (Exception) incoming);
        }
      } else if (incoming instanceof Boolean) {
        Boolean ack = (Boolean) incoming;
        if (ack.booleanValue()) {
          // System.out.println("ACKED");
        } else {
          throw new AuditException("Acknowledgement failure");
        }
      } else {
        throw new AuditException("Incoming object [" + incoming
            + "] outside of protocol");
      }
    } catch (IOException e) {
      throw new AuditException("Failed reading acknowledgement", e);
    } catch (ClassNotFoundException e) {
      throw new AuditException("Unexpecteed object type while acknowledgement",
          e);
    }
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getRemoteHost() {
    return remoteHost;
  }

  protected static InetAddress getAddressByName(String host) {
    try {
      return InetAddress.getByName(host);
    } catch (Exception e) {
      // addError("Could not find address of [" + host + "].", e);
      return null;
    }
  }

  void fireConnector() {
    if (connector == null) {
      addInfo("Firing a connector thread");
      connector = new Connector(this);
      connector.setContext(this.getContext());
      Thread connectorThread = new Thread(connector);
      connectorThread.setDaemon(true);
      connectorThread.setName("SAA-ConnectorThread");
      connectorThread.setPriority(Thread.MIN_PRIORITY);
      connector.setThread(connectorThread);
      connectorThread.start();
    }
  }

  InetAddress getAddress() {
    return address;
  }

  public int getReconnectionDelay() {
    return reconnectionDelay;
  }

  public void setReconnectionDelay(int reconnectionDelay) {
    this.reconnectionDelay = reconnectionDelay;
  }

  /**
   * The <b>RemoteHost</b> option takes a string value which should be the host
   * name of the server where a {@link SocketNode} is running.
   */
  public void setRemoteHost(String host) {
    address = getAddressByName(host);
    remoteHost = host;
  }

}

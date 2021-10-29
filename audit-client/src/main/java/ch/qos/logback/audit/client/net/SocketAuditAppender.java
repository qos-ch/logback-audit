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
import java.util.Objects;

import ch.qos.logback.audit.AuditEvent;
import ch.qos.logback.audit.AuditException;
import ch.qos.logback.audit.InternalAuditContants;
import ch.qos.logback.audit.client.AuditAppenderBase;

public class SocketAuditAppender extends AuditAppenderBase {

	static final String NO_HOST_URL = InternalAuditContants.CODES_URL + "#NO_HOST_URL";

	static final String FAILIED_WRITE_URL = InternalAuditContants.CODES_URL + "#FAILIED_WRITE_URL";

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
	@Override
	public void start() {
		int errorCount = 0;
		if (port == 0) {
			errorCount++;
			addError("No port was configured for appender " + name);
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
		if (Objects.nonNull(connector)) {
			connector.setClosed(true);
			final Thread connectorThread = connector.getThread();
			try {
				connectorThread.join(5000);
			} catch (final InterruptedException e) {
				addError("Failed to join connector thread", e);
			}
			connector = null;
		}

	}

	void connect(final Socket socket) {
		try {
			// First, close the previous connection if any.
			cleanUp();

			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			super.started = true;
		} catch (final IOException e) {
			addError(String.format(
					"Failed to open a stream on the socket for remmote logback server at [%s] at port [%d]",
					address.getHostName(), port), e);
		}
	}

	void connect(final InetAddress address, final int port) {
		try {
			// First, close the previous connection if any.
			cleanUp();
			final Socket socket = new Socket(address, port);
			connect(socket);
		} catch (final IOException e) {
			addError(String.format("Could not connect to remote logback server at [%s] at port [%d]",
					address.getHostName(), port), e);
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
		if (Objects.nonNull(oos)) {
			try {
				oos.close();
			} catch (final IOException e) {
				addError("Could not close oos.", e);
			}
			oos = null;
		}
		if (Objects.nonNull(ois)) {
			try {
				ois.close();
			} catch (final IOException e) {
				addError("Could not close oos.", e);
			}
			ois = null;
		}
	}

	@Override
	protected void append(final AuditEvent auditEvent) throws AuditException {

		if (!started || Objects.isNull(auditEvent)) {
			return;
		}

		if (Objects.nonNull(oos)) {
			try {
				oos.writeObject(auditEvent);
				oos.flush();

				if (++counter >= RESET_FREQUENCY) {
					counter = 0;
					// Failing to reset the object output stream every now and
					// then creates a serious memory leak.
					oos.reset();
				}
			} catch (final IOException e) {
				oos = null;
				started = false;
				fireConnector();
				throw new AuditException("Failed sending audit event to host \"" + remoteHost
						+ "\" down. For more information, please visit " + FAILIED_WRITE_URL, e);
			}
		}

		try {
			final Object incoming = ois.readObject();
			if (incoming instanceof Exception) {
				if (incoming instanceof AuditException) {
					final AuditException ae = (AuditException) incoming;
					throw ae;
				}
				throw new AuditException("Server incurred an exception", (Exception) incoming);
			}
			if (!(incoming instanceof Boolean)) {
				throw new AuditException("Incoming object [" + incoming + "] outside of protocol");
			}
			final Boolean ack = (Boolean) incoming;
			if (!ack) {
				throw new AuditException("Acknowledgement failure");
			}
		} catch (final IOException e) {
			throw new AuditException("Failed reading acknowledgement", e);
		} catch (final ClassNotFoundException e) {
			throw new AuditException("Unexpecteed object type while acknowledgement", e);
		}
	}

	public int getPort() {
		return port;
	}

	public void setPort(final int port) {
		this.port = port;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	protected static InetAddress getAddressByName(final String host) {
		try {
			return InetAddress.getByName(host);
		} catch (final Exception e) {
			// addError("Could not find address of [" + host + "].", e);
			return null;
		}
	}

	void fireConnector() {
		if (Objects.isNull(connector)) {
			addInfo("Firing a connector thread");
			connector = new Connector(this);
			connector.setContext(getContext());
			final Thread connectorThread = new Thread(connector);
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

	public void setReconnectionDelay(final int reconnectionDelay) {
		this.reconnectionDelay = reconnectionDelay;
	}

	/**
	 * The <b>RemoteHost</b> option takes a string value which should be the host
	 * name of the server where a {@link SocketNode} is running.
	 */
	public void setRemoteHost(final String host) {
		address = getAddressByName(host);
		remoteHost = host;
	}

}

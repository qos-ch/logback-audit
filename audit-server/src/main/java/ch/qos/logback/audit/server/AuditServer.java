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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditServer extends Thread {

	static Logger logger = LoggerFactory.getLogger(AuditServer.class);

	final int port;
	final AuditEventHandler auditEventHandler;
	boolean closed = false;
	boolean serverSocketSucessfullyOpened = false;
	ServerSocket serverSocket;
	List<SocketNode> socketNodeList = new ArrayList<>();

	public AuditServer(final int port, final AuditEventHandler auditEventHandler) {
		this.port = port;
		this.auditEventHandler = auditEventHandler;
	}

	@Override
	public void run() {
		try {
			logger.info("Listening on port " + port);
			serverSocket = new ServerSocket(port);
			serverSocketSucessfullyOpened = true;
			while (!closed) {
				logger.info("Waiting to accept a new client.");
				final Socket socket = serverSocket.accept();
				final InetAddress inetAddress = socket.getInetAddress();
				logger.info("Connected to client at " + inetAddress);

				logger.info("Starting new socket node.");
				final SocketNode newSocketNode = new SocketNode(this, socket, auditEventHandler);
				// don't allow simultaneous access to the socketNodeList
				// (e.g. removal whole iterating on the list causes
				// java.util.ConcurrentModificationException
				synchronized (socketNodeList) {
					socketNodeList.add(newSocketNode);
				}

				new Thread(newSocketNode).start();
			}
		} catch (final SocketException e) {
			if ("socket closed".equals(e.getMessage())) {
				logger.info("Audit server has been closed");
			} else {
				logger.info("Caught an SocketException", e);
			}
		} catch (final IOException e) {
			logger.info("Caught an IOException", e);
		} catch (final Exception e) {
			logger.error("Caught an unexpectged exception.", e);
		}
	}

	void socketNodeClosing(final SocketNode sn) {
		logger.debug("Removing {}", sn);

		// don't allow simultaneous access to the socketNodeList
		// (e.g. removal whole iterating on the list causes
		// java.util.ConcurrentModificationException
		synchronized (socketNodeList) {
			socketNodeList.remove(sn);
		}
	}

	public void close() {
		closed = true;
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (final IOException e) {
				logger.error("Failed to close serverSocket", e);
			}
		}

		// don't allow simultaneous access to the socketNodeList
		// (e.g. removal whole iterating on the list causes
		// java.util.ConcurrentModificationException
		synchronized (socketNodeList) {
			for (final SocketNode sn : socketNodeList) {
				sn.close();
			}
		}

	}

	public boolean isServerSocketSucessfullyOpened() {
		return serverSocketSucessfullyOpened;
	}
}
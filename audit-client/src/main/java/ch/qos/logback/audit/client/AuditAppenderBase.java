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

package ch.qos.logback.audit.client;

import ch.qos.logback.audit.AuditEvent;
import ch.qos.logback.audit.AuditException;
import ch.qos.logback.core.spi.ContextAwareBase;

abstract public class AuditAppenderBase extends ContextAwareBase implements AuditAppender {

	protected boolean started = false;

	// static final int NOT_STARTED_ERROR_COUNT_LIMIT = 5;
	// int notStartedErrorCount = 0;

	/**
	 * Appenders are named.
	 */
	protected String name;

	@Override
	public synchronized void doAppend(final AuditEvent auditEvent) throws AuditException {

		try {

			if (!started) {
				throw new AuditException("Attempted to append to non started appender [" + name + "].");
			}

			// ok, we now invoke derived class' implementation of append
			append(auditEvent);

		} catch (final Exception e) {
			if (e instanceof AuditException) {
				throw (AuditException) e;
			}
			throw new AuditException("Failed to audit an event", e);
		}
	}

	abstract protected void append(AuditEvent auditEvent) throws AuditException;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public void start() {
		started = true;
	}

	@Override
	public void stop() {
		started = false;
	}

	@Override
	public boolean isStarted() {
		return started;
	}
}

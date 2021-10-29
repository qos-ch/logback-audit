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

import java.util.Objects;

import ch.qos.logback.audit.Application;
import ch.qos.logback.audit.AuditEvent;
import ch.qos.logback.audit.AuditEventBuilder;
import ch.qos.logback.audit.AuditEventBuilderImpl;
import ch.qos.logback.audit.AuditException;
import ch.qos.logback.core.ContextBase;

public class Auditor extends ContextBase {

	AuditAppender auditAppender;
	Application clientApplication;

	public AuditEventBuilder newAuditEventBuilder() {
		final AuditEventBuilder ab = new AuditEventBuilderImpl();
		ab.setClientApplication(clientApplication);
		return ab;
	}

	public void log(final AuditEvent auditEvent) throws AuditException {
		auditAppender.doAppend(auditEvent);
	}

	public void log(final AuditEventBuilder builder) throws AuditException {
		final AuditEvent auditEvent = builder.build();
		auditAppender.doAppend(auditEvent);
	}

	public AuditAppender getAuditAppender() {
		return auditAppender;
	}

	public void setAuditAppender(final AuditAppender auditAppender) {
		this.auditAppender = auditAppender;
	}

	public void shutdown() {
		if (Objects.nonNull(auditAppender)) {
			auditAppender.stop();
		}
		auditAppender = null;
	}

	public Application getClientApplication() {
		return clientApplication;
	}

	public void setClientApplication(final Application capp) {
		if (Objects.nonNull(clientApplication)) {
			throw new IllegalStateException("Client application has been set already.");
		}
		clientApplication = capp;
	}

}

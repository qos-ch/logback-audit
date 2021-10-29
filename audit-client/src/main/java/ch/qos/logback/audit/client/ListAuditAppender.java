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

import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.audit.AuditEvent;
import ch.qos.logback.audit.AuditException;

public class ListAuditAppender extends AuditAppenderBase {

	List<AuditEvent> auditEventList = null;

	@Override
	protected void append(final AuditEvent auditEvent) throws AuditException {
		auditEventList.add(auditEvent);
	}

	@Override
	public void start() {
		auditEventList = new ArrayList<>();
		super.start();
	}

	@Override
	public void stop() {
		auditEventList = null;
		super.stop();
	}

	public List<AuditEvent> getAuditEventList() {
		return auditEventList;
	}

}

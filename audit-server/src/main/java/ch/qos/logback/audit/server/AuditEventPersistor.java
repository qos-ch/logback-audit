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

import ch.qos.logback.audit.AuditEvent;
import ch.qos.logback.audit.persistent.AuditEventShaper;
import ch.qos.logback.audit.persistent.Persistor;

public class AuditEventPersistor extends AuditEventHandlerBase {

	final AuditEventShaper eventShaper;

	public AuditEventPersistor(final AuditEventShaper eventShaper) {
		this.eventShaper = eventShaper;
	}

	@Override
	public void doHandle(final AuditEvent ae) {
		eventShaper.shape(ae);
		Persistor.save(ae);
		fireIncoming(ae);
	}

}

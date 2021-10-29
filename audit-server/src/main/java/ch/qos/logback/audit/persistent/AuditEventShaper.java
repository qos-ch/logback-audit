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

package ch.qos.logback.audit.persistent;

import ch.qos.logback.audit.AuditEvent;

/**
 * Shapes audit event so that they are compatible with the target RDBMS.
 *
 * @author ceki
 *
 */
public interface AuditEventShaper {

	/**
	 * Shape auditEvent so that it is compatible with the target RDBMS.
	 */
	void shape(AuditEvent auditEvent);
}

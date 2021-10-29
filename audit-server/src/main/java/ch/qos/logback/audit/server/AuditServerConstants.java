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

import java.text.SimpleDateFormat;

public class AuditServerConstants {

	public static final SimpleDateFormat ISODATE_SDF = new SimpleDateFormat("yyyy-MM-dd'T'HHmmss");
	public static final String PORT_INIT_PARAM = "PORT";
	public static final String RDBMS_DIALECT_INIT_PARAM = "RDBMS_DIALECT";

	public static final int DEFAULT_PORT = 9630;
	public static final String AUDIT_SERVER_REFERENCE = "AUDIT_SERVER_REFERENCE";

	public static final String SQLSERVER_2005_DIALECT_VALUE = "SQLSERVER_2005";

	// maximum length of persisted fields
	public static final int SUBJECT_FIELD_MAX_LENGTH = 1024;
	public static final int VERB_FIELD_MAX_LENGTH = 64;
	public static final int OBJECT_FIELD_MAX_LENGTH = 4096;
	public static final int APPLICATION_NAME_FIELD_MAX_LENGTH = VERB_FIELD_MAX_LENGTH;
	public static final int IP_ADDRESS_FIELD_MAX_LENGTH = 48;
	public static final int PREDICATE_KEY_FIELD_MAX_LENGTH = VERB_FIELD_MAX_LENGTH;
	public static final int PREDICATE_VALUE_FIELD_MAX_LENGTH = OBJECT_FIELD_MAX_LENGTH;
}

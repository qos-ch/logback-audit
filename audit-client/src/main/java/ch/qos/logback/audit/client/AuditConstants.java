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

public interface AuditConstants {
  // TYPES
  static final String SUBJECT_TYPE = "SUBJECT_TYPE";
  static final String CRUD_TYPE = "CRUD_TYPE";
  
  // CRUD_TYPES
  static final String CREATE = "CREATE";
  static final String READ = "READ";
  static final String UPDATE = "UPDATE";
  static final String DELETE = "DELETE";
}

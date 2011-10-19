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

package ch.qos.logback.audit;

public class AuditException extends Exception {

  private static final long serialVersionUID = -8812426046267093174L;

  public AuditException(String msg) {
    super(msg);
  }
  
  public AuditException(String msg, Throwable t) {
    super(msg, t);
  }
  
}

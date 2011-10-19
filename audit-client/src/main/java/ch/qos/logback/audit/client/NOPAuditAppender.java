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

public class NOPAuditAppender extends AuditAppenderBase {

  @Override 
  protected void append(AuditEvent auditEvent) throws AuditException {
  }
  
  @Override
  public void start() {
    super.start();
  }

  
  @Override
  public void stop() {
    super.stop();
  }

}

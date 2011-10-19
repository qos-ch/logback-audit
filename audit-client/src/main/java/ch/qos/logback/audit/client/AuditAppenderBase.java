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

abstract public class AuditAppenderBase extends ContextAwareBase implements
    AuditAppender {

  protected boolean started = false;
  
  //static final int NOT_STARTED_ERROR_COUNT_LIMIT = 5;
  //int notStartedErrorCount = 0;
  
  
  /**
   * Appenders are named.
   */
  protected String name;

  public synchronized void doAppend(AuditEvent auditEvent)
      throws AuditException {

    try {

      if (!this.started) {
        throw new AuditException(
            "Attempted to append to non started appender [" + name + "].");
      }

      // ok, we now invoke derived class' implementation of append
      this.append(auditEvent);

    } catch (Exception e) {
      if (e instanceof AuditException) {
        throw (AuditException) e;
      } else {
        throw new AuditException("Failed to audit an event", e);
      }
    }
  }

  abstract protected void append(AuditEvent auditEvent) throws AuditException;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void start() {
    started = true;
  }

  public void stop() {
    started = false;
  }

  public boolean isStarted() {
    return started;
  }
}

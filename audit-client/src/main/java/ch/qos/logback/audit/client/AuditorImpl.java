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

import java.util.Iterator;

import ch.qos.logback.audit.AuditEvent;
import ch.qos.logback.audit.AuditException;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AppenderAttachableImpl;

public class AuditorImpl  {

  AppenderAttachableImpl<AuditEvent> aai = new AppenderAttachableImpl<AuditEvent>();
  
  public void log(AuditEvent auditEvent) throws AuditException {
    int writes = aai.appendLoopOnAppenders(auditEvent);
    // No appenders in hierarchy
    if (writes == 0) {
      throw new AuditException("no appender defined");
    }
  }
  
  public void addAppender(Appender<AuditEvent> newAppender) {
    aai.addAppender(newAppender);
  }

  public void detachAndStopAllAppenders() {
    aai.detachAndStopAllAppenders();
  }

  public boolean detachAppender(Appender<AuditEvent> appender) {
    return aai.detachAppender(appender);
  }

  public Appender<AuditEvent> getAppender(String name) {
    return aai.getAppender(name);
  }

  public boolean isAttached(Appender<AuditEvent> appender) {
    return aai.isAttached(appender);
  }

  public Iterator iteratorForAppenders() {
    return aai.iteratorForAppenders();
  }

}

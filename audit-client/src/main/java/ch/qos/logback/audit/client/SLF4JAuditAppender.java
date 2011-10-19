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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.audit.AuditEvent;
import ch.qos.logback.audit.AuditException;

public class SLF4JAuditAppender extends AuditAppenderBase {

  final static String  DEFAULT_LOGGER_NAME = "audit";
  String loggerName = DEFAULT_LOGGER_NAME;
  Logger logger;
  
  @Override
  protected void append(AuditEvent auditEvent) throws AuditException {
    logger.info(auditEvent.toString());
  }

  public String getLoggerName() {
    return loggerName;
  }

  public void setLoggerName(String loggerName) {
    if(loggerName == null) {
      throw new IllegalArgumentException("loggerName cannot be null");
    }
    this.loggerName = loggerName;
  }
  
  public void start() {
    logger = LoggerFactory.getLogger("audit");
    super.start();
  }
}

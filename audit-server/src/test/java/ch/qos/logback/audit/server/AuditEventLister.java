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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.audit.AuditEvent;
import ch.qos.logback.audit.AuditException;

public class AuditEventLister extends AuditEventHandlerBase {

  public static final String THROW_EXCEPTION = "THROW_EXCEPTION";
  
  List<AuditEvent> auditEventList = new ArrayList<AuditEvent>();

  int notificationPeriod;
  int count = 0;

  AuditEventLister(int notificationPeriod) {
    this.notificationPeriod = notificationPeriod;
  }

  Logger logger = LoggerFactory.getLogger(AuditEventLister.class);

  public void doHandle(AuditEvent ae) throws AuditException {
    logger.debug("new event {}", ae);
    
    if("THROW_EXCEPTION".equalsIgnoreCase(ae.getVerb())) {
      throw new AuditException("Incoming message caused an exception");
    }

    auditEventList.add(ae);
    count++;
    if (count % notificationPeriod == 0) {
      synchronized (this) {
        this.notify();
      }
    }
    fireIncoming(ae);
    
  }

}

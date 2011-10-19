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

import java.util.List;
import java.util.Map;

public class AuditEventBuilderImpl implements AuditEventBuilder {
  
  AuditEvent auditEvent = new AuditEvent();
  

  public void setSubject(String subject) {
    auditEvent.setSubject(subject);
  }
  
  public void setVerb(String verb) {
    auditEvent.setVerb(verb);
  }
  
  public void setObject(String object) {
    auditEvent.setObject(object);
  }
  
  public void setPredicateMap(Map<String, String> predicateMap) {
    auditEvent.setPredicateMap(predicateMap);
  }

  public void addPredicate(String key, String value) {
    Predicate predicate = new Predicate(key, value);
    auditEvent.addPredicate(predicate);
  }
 
  public void add(Predicate predicate) {
    auditEvent.addPredicate(predicate);
  }

  public void add(List<Predicate> predicateList) {
    throw new UnsupportedOperationException("this operation is not yet supported");
  }

  
  public void setClientApplication(Application clientApplication) {
    auditEvent.setClientApplication(clientApplication);
  }
  
  public void setOriginatingApplication(Application originatingApplication) {
    auditEvent.setOriginatingApplication(originatingApplication);
  }
  
  /* (non-Javadoc)
   * @see ch.qos.logback.audit.AuditEventBuilder#build()
   */
  public AuditEvent build() {
    return auditEvent;
  }
  
}

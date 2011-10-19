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

public interface AuditEventBuilder {

  public void setObject(String object);
  public void setSubject(String subject);
  public void setVerb(String verb);
  public void setPredicateMap(Map<String, String> predicateMap);
  public void add(Predicate predicate);
  public void add(List<Predicate> predicateList);
  
  public void addPredicate(String key, String value);
  public void setClientApplication(Application clientApp);
  public void setOriginatingApplication(Application originatingApplication);
  public AuditEvent build();

}
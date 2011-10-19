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

package ch.qos.logback.audit.persistent;

import java.util.Map;

import ch.qos.logback.audit.AuditEvent;

public class SQLServerAEShaper implements AuditEventShaper {


  /*
   * SQLServer 2005 cannot index varchar columns longer than 900 characters.
   */
  static final int MAX_VARCHAR_COL_WIDTH = 900;
  
  public void shape(AuditEvent auditEvent) {
    
    String subject = auditEvent.getSubject();
    if(tooLong(subject)) {
      auditEvent.setSubject(narrow(subject));
    }
    
    String object = auditEvent.getObject();
    if(tooLong(object)) {
      auditEvent.setObject(narrow(object));
    }
    
    Map<String, String> predicateMap = auditEvent.getPredicateMap();
    if(predicateMap != null) {
      for(String k: predicateMap.keySet()) {
        String value = predicateMap.get(k);
        if(tooLong(value)) {
          predicateMap.put(k, narrow(value));
        }
      }
    }
  }

  final boolean tooLong(String in) {
    if(in != null && in.length() > MAX_VARCHAR_COL_WIDTH) {
      return true;
    } else {
      return false;
    }
  }
  
  final String narrow(String in) {
    return in.substring(0, MAX_VARCHAR_COL_WIDTH);
  }
  
}

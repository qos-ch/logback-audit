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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class AuditEvent implements Serializable {

  private static final long serialVersionUID = 6641931592187864466L;

  Long id;
  private Timestamp timestamp;
  String subject;
  String verb;
  String object;
  Map<String, String> predicateMap = new HashMap<String, String>();

  Application originatingApplication;
  Application clientApplication;

  AuditEvent() {
    timestamp = new Timestamp(System.currentTimeMillis());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public String getObject() {
    return object;
  }

  /**
   * Only AuditEventBuilder can set properties of an AuditEvent.
   * 
   * @param verb
   */
  public void setObject(String object) {
    this.object = object;
  }

  public String getSubject() {
    return subject;
  }

  /**
   * Only AuditEventBuilder can set properties of an AuditEvent.
   * 
   * @param verb
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getVerb() {
    return verb;
  }

  /**
   * Only AuditEventBuilder can set properties of an AuditEvent.
   * 
   * @param verb
   */
  public void setVerb(String verb) {
    this.verb = verb;
  }

  public Application getClientApplication() {
    return clientApplication;
  }

  public void setClientApplication(Application clientApplication) {
    this.clientApplication = clientApplication;
  }

  public Application getOriginatingApplication() {
    return originatingApplication;
  }

  public void setOriginatingApplication(Application originatingApplication) {
    this.originatingApplication = originatingApplication;
  }

  /**
   * Only AuditEventBuilder can set properties of an AuditEvent.
   * 
   * @param verb
   */
  public void addPredicate(Predicate predicate) {
    predicateMap.put(predicate.getName(), predicate.getValue());
  }

  public void setPredicateMap(Map<String, String> predicateMap) {
    this.predicateMap = predicateMap;
  }

  public Map<String, String> getPredicateMap() {
    return predicateMap;
  }

  @Override
  public int hashCode() {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ((id == null) ? 0 : id.hashCode());
    result = PRIME * result + ((timestamp == null) ? 0 : timestamp.hashCode());
    result = PRIME * result + ((subject == null) ? 0 : subject.hashCode());
    return result;
  }
  

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final AuditEvent other = (AuditEvent) obj;

    // timestamp cannot be null
    if (!timestamp.equals(other.timestamp)) {
      return false;
    }
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id)) {
      return false;
    }

    if (subject == null) {
      if (other.subject != null)
        return false;
    } else if (!subject.equals(other.subject)) {
      return false;
    }

    if (verb == null) {
      if (other.verb != null)
        return false;
    } else if (!verb.equals(other.verb)) {
      return false;
    }
    if (object == null) {
      if (other.object != null)
        return false;
    } else if (!object.equals(other.object)) {
      return false;
    }

    if (clientApplication == null) {
      if (other.clientApplication != null)
        return false;
    } else if (!clientApplication.equals(other.clientApplication)) {
      return false;
    }

    if (originatingApplication == null) {
      if (other.originatingApplication != null)
        return false;
    } else if (!originatingApplication.equals(other.originatingApplication)) {
      return false;
    }

    if (predicateMap == null) {
      if (other.predicateMap != null)
        return false;
    } else if (!predicateMap.equals(other.predicateMap)) {
      return false;
    }

    return true;
  }

  public String toString() {

    String retValue = "";

    retValue = "AuditEvent(id=" + this.id + ", timestamp=" + this.timestamp
        + ", subject=" + this.subject + ", verb=" + this.verb + ", object="
        + this.object + ", predicateMap = " + this.predicateMap
        + ", originatingApplication=" + this.originatingApplication
        + ", clientApplication=" + this.clientApplication + ")";

    return retValue;
  }

}

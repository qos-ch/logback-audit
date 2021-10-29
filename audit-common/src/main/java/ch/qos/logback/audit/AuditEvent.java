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
import java.util.Objects;

public class AuditEvent implements Serializable {

	private static final long serialVersionUID = 6641931592187864466L;

	Long id;
	private Timestamp timestamp;
	String subject;
	String verb;
	String object;
	Map<String, String> predicateMap = new HashMap<>();

	Application originatingApplication;
	Application clientApplication;

	AuditEvent() {
		timestamp = new Timestamp(System.currentTimeMillis());
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Timestamp timestamp) {
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
	public void setObject(final String object) {
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
	public void setSubject(final String subject) {
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
	public void setVerb(final String verb) {
		this.verb = verb;
	}

	public Application getClientApplication() {
		return clientApplication;
	}

	public void setClientApplication(final Application clientApplication) {
		this.clientApplication = clientApplication;
	}

	public Application getOriginatingApplication() {
		return originatingApplication;
	}

	public void setOriginatingApplication(final Application originatingApplication) {
		this.originatingApplication = originatingApplication;
	}

	/**
	 * Only AuditEventBuilder can set properties of an AuditEvent.
	 *
	 * @param verb
	 */
	public void addPredicate(final Predicate predicate) {
		predicateMap.put(predicate.getName(), predicate.getValue());
	}

	public void setPredicateMap(final Map<String, String> predicateMap) {
		this.predicateMap = predicateMap;
	}

	public Map<String, String> getPredicateMap() {
		return predicateMap;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, timestamp, subject);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (Objects.isNull(obj) || getClass() != obj.getClass()) {
			return false;
		}
		final AuditEvent other = (AuditEvent) obj;

		// timestamp cannot be null
		if (!timestamp.equals(other.timestamp) || !Objects.equals(id, other.id)
				|| !Objects.equals(subject, other.subject) || !Objects.equals(verb, other.verb)) {
			return false;
		}
		if (!Objects.equals(object, other.object) || !Objects.equals(clientApplication, other.clientApplication)
				|| !Objects.equals(originatingApplication, other.originatingApplication)
				|| !Objects.equals(predicateMap, other.predicateMap)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "AuditEvent(id=" + id + ", timestamp=" + timestamp + ", subject=" + subject + ", verb=" + verb
				+ ", object=" + object + ", predicateMap = " + predicateMap + ", originatingApplication="
				+ originatingApplication + ", clientApplication=" + clientApplication + ")";
	}

}

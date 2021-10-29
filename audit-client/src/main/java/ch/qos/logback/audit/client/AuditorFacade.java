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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.audit.Application;
import ch.qos.logback.audit.AuditEventBuilder;
import ch.qos.logback.audit.AuditException;
import ch.qos.logback.audit.Predicate;

public class AuditorFacade {
	final Logger logger = LoggerFactory.getLogger(AuditorFacade.class);

	final String object;
	final String verb;
	final String subject;
	Application originatingApplication;
	Map<String, String> predicateMap;

	public AuditorFacade(final String subject, final String verb, final String object) {
		this.subject = subject;
		this.verb = verb;
		this.object = object;
	}

	public void audit() throws AuditException {
		final Auditor auditor = AuditorFactory.getAuditor();

		final AuditEventBuilder builder = auditor.newAuditEventBuilder();
		builder.setObject(object);
		builder.setVerb(verb);
		builder.setSubject(subject);
		if (Objects.nonNull(predicateMap)) {
			builder.setPredicateMap(predicateMap);
		}
		if (Objects.nonNull(originatingApplication)) {
			builder.setOriginatingApplication(originatingApplication);
		}
		auditor.log(builder);
	}

	public AuditorFacade setPredicateMap(final Map<String, String> predicateMap) {
		this.predicateMap = predicateMap;
		return this;
	}

	/**
	 * Add a predicate.
	 *
	 * @param predicate
	 * @return
	 */
	public AuditorFacade add(final Predicate predicate) {
		if (Objects.isNull(predicate)) {
			throw new IllegalArgumentException("The predicate parameter cannot be null");
		}
		return add(predicate.getName(), predicate.getValue());
	}

	/**
	 * Add a predicate through two parameters, key and value.
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public AuditorFacade add(final String key, final String value) {
		Objects.<Map<String, String>>requireNonNullElse(predicateMap, new HashMap<>()).put(key, value);
		return this;
	}

	/**
	 * Set the originating application name.
	 *
	 * @param originatingApplication
	 * @return
	 */
	public AuditorFacade originating(final Application originatingApplication) {
		this.originatingApplication = originatingApplication;
		return this;
	}
}

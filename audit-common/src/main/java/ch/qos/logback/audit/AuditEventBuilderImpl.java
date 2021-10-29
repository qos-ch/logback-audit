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

	@Override
	public void setSubject(final String subject) {
		auditEvent.setSubject(subject);
	}

	@Override
	public void setVerb(final String verb) {
		auditEvent.setVerb(verb);
	}

	@Override
	public void setObject(final String object) {
		auditEvent.setObject(object);
	}

	@Override
	public void setPredicateMap(final Map<String, String> predicateMap) {
		auditEvent.setPredicateMap(predicateMap);
	}

	@Override
	public void addPredicate(final String key, final String value) {
		auditEvent.addPredicate(new Predicate(key, value));
	}

	@Override
	public void add(final Predicate predicate) {
		auditEvent.addPredicate(predicate);
	}

	@Override
	public void add(final List<Predicate> predicateList) {
		throw new UnsupportedOperationException("this operation is not yet supported");
	}

	@Override
	public void setClientApplication(final Application clientApplication) {
		auditEvent.setClientApplication(clientApplication);
	}

	@Override
	public void setOriginatingApplication(final Application originatingApplication) {
		auditEvent.setOriginatingApplication(originatingApplication);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ch.qos.logback.audit.AuditEventBuilder#build()
	 */
	@Override
	public AuditEvent build() {
		return auditEvent;
	}

}

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

	void setObject(String object);

	void setSubject(String subject);

	void setVerb(String verb);

	void setPredicateMap(Map<String, String> predicateMap);

	void add(Predicate predicate);

	void add(List<Predicate> predicateList);

	void addPredicate(String key, String value);

	void setClientApplication(Application clientApp);

	void setOriginatingApplication(Application originatingApplication);

	AuditEvent build();

}
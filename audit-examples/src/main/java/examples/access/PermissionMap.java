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

package examples.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PermissionMap {

	Map<String, List<Permission>> map = new HashMap<>();

	void addPermission(final User u, final Permission p) {
		Objects.<List<Permission>>requireNonNullElse(map.get(u.getName()), new ArrayList<>()).add(p);
	}

	void removePermission(final User u, final Permission p) {
		Objects.<List<Permission>>requireNonNullElse(map.get(u.getName()), new ArrayList<>()).remove(p);
	}

	boolean hasPermission(final User u, final Permission p) {
		return Objects.<List<Permission>>requireNonNullElse(map.get(u.getName()), new ArrayList<>()).contains(p);
	}
}

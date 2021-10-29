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
import java.util.Objects;

public class Application implements Serializable {

	private static final long serialVersionUID = 62706289462L;

	String name;
	String ipAddress;

	// Hibernate requires a default Constructor
	public Application() {
	}

	public Application(final String name, final String ipAddress) {
		this.name = name;
		this.ipAddress = ipAddress;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	// Hibernate needs to be able to set the ipAdress
	public void setIpAddress(final String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getName() {
		return name;
	}

	// Hibernate needs to be able to set the name
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ipAddress, name);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final Application other = (Application) obj;
		if (!Objects.equals(ipAddress, other.ipAddress) || !Objects.equals(name, other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Appl(" + "name=" + name + ", ip=" + ipAddress + ")";
	}

}

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

package ch.qos.logback.audit.server.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceUtil {

	static String applicationName;

	public static Properties getProps(final String path) {
		final ClassLoader ccl = Thread.currentThread().getContextClassLoader();

		final InputStream is = ccl.getResourceAsStream(path);
		if (is == null) {
			throw new IllegalStateException("Failed to resource [" + path + "]");
		}
		final Properties props = new Properties();
		try {
			props.load(is);
			return props;
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				is.close();
			} catch (final IOException e) {

			}
		}
	}

	public static String getApplicationName() {
		return applicationName;
	}

	public static void setApplicationName(final String applicationName) {
		if (ResourceUtil.applicationName != null && !ResourceUtil.applicationName.equals(applicationName)) {
			throw new IllegalArgumentException(
					"Application name has been already set to [" + ResourceUtil.applicationName + "].");
		}
		ResourceUtil.applicationName = applicationName;
	}
}

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

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import ch.qos.logback.audit.Application;
import ch.qos.logback.audit.AuditException;
import ch.qos.logback.audit.client.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusChecker;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.util.Loader;
import ch.qos.logback.core.util.OptionHelper;
import ch.qos.logback.core.util.StatusPrinter;

public class AuditorFactory {

	static final String DEFAULT_AUDITOR_NAME = "default";
	static final String AUTOCONFIG_FILE = "logback-audit.xml";
	static final String TEST_AUTOCONFIG_FILE = "logback-audit-test.xml";

	static final public String AUTOCONFIG_FILE_PROPERTY = "logback.audit.autoconfig.file";

	static final public String NULL_CLIENT_APPLICATON_URL = "http://audit.qos.ch/codes.html#nullClientApp";
	static final public String NULL_AUDIT_APPENDER_URL = "http://audit.qos.ch/codes.html#nullAuditAppender";

	static Auditor defaultAuditor;

	static Application clientApplication;

	static void checkSanity(final Auditor auditor) throws AuditException {
		final StatusManager sm = auditor.getStatusManager();
		final StatusChecker checker = new StatusChecker(sm);
		if (checker.getHighestLevel(0) > Status.INFO) {
			StatusPrinter.print(sm);
		}

		if (auditor.getClientApplication() == null) {
			throw new AuditException("Client application has not been set");
		}

		if (auditor.getAuditAppender() == null) {
			throw new AuditException("No audit appender. Please see " + NULL_AUDIT_APPENDER_URL);
		}
	}

	public static void setApplicationName(final String name) throws AuditException {
		if (nonNull(clientApplication) && clientApplication.getName().equals(name)) {
			// don't configure again
			return;
		}
		if (nonNull(clientApplication) && !clientApplication.getName().equals(name)) {
			throw new IllegalStateException(
					"Application name " + clientApplication.getName() + " once set cannot be renamed.");
		}

		if (OptionHelper.isEmpty(name)) {
			throw new IllegalArgumentException("Application name cannot be null or empty");
		}

		try {
			final InetAddress address = InetAddress.getLocalHost();
			final String fqdn = address.getCanonicalHostName();
			// logger("Client application host is ["+fqdn+"].");
			final Application aplication = new Application(name, fqdn);
			// all is nice and dandy
			clientApplication = aplication;
		} catch (final UnknownHostException e) {
			throw new IllegalStateException("Failed to determine the hostname for this host", e);
		}

		// defaultAuditor.close();
		defaultAuditor = new Auditor();
		defaultAuditor.setClientApplication(clientApplication);
		defaultAuditor.setName(DEFAULT_AUDITOR_NAME);
		autoConfig(defaultAuditor);
		checkSanity(defaultAuditor);
	}

	/**
	 * Get the default auditor. If it has not been previously configured, this
	 * method will also configure the default auditor. In case of problems, this
	 * method may throw an exception.
	 *
	 * @return
	 * @throws AuditException
	 */
	public static Auditor getAuditor() {
		return defaultAuditor;
	}

	public static void autoConfig(final Auditor auditor) throws AuditException {
		final ClassLoader tccl = Loader.getTCL();
		autoConfig(auditor, tccl);
	}

	public static void configureByResource(final Auditor auditor, final URL url) throws AuditException {
		if (isNull(url)) {
			throw new IllegalArgumentException("URL argument cannot be null");
		}
		final JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(auditor);

		try {
			configurator.doConfigure(url);
		} catch (final JoranException e) {
			throw new AuditException("Configuration failure in " + url, e);
		}

	}

	public static void autoConfig(final Auditor auditor, final ClassLoader classLoader) throws AuditException {

		final String autoConfigFileByProperty = System.getProperty(AUTOCONFIG_FILE_PROPERTY);
		final String pathPrefix = clientApplication.getName() + "/";
		URL url = null;

		if (autoConfigFileByProperty != null) {
			url = Loader.getResource(pathPrefix + autoConfigFileByProperty, classLoader);

		} else {
			url = Loader.getResource(pathPrefix + TEST_AUTOCONFIG_FILE, classLoader);
			if (isNull(url)) {
				url = Loader.getResource(pathPrefix + AUTOCONFIG_FILE, classLoader);
			}
		}
		if (isNull(url)) {
			String errMsg;
			if (nonNull(autoConfigFileByProperty)) {
				errMsg = "Failed to find configuration file [" + pathPrefix + autoConfigFileByProperty + "].";
			} else {
				errMsg = "Failed to find logback-audit configuration files  [" + pathPrefix + TEST_AUTOCONFIG_FILE
						+ "] or [" + pathPrefix + AUTOCONFIG_FILE + "].";
			}
			throw new AuditException(errMsg);
		}
		configureByResource(auditor, url);
	}

	static public void reset() {
		clientApplication = null;
		if (nonNull(defaultAuditor)) {
			defaultAuditor.shutdown();
		}
		defaultAuditor = null;
	}

}

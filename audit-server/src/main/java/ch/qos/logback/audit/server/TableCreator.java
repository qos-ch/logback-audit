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

package ch.qos.logback.audit.server;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.audit.persistent.Persistor;
import ch.qos.logback.audit.server.helper.ResourceUtil;

/**
 *
 * @author Ceki Gulcu
 */
public class TableCreator {

	Logger logger = LoggerFactory.getLogger(TableCreator.class);
	Configuration cfg;

	public TableCreator(final Configuration cfg) {
		this.cfg = cfg;
	}

	public void createTables(final String filename) {
		final SchemaExport schemaExport = new SchemaExport(cfg);
		boolean printDLL = false;
		if (filename != null) {
			schemaExport.setOutputFile(filename);
			printDLL = true;
		}

		schemaExport.create(printDLL, true);

	}

	public void createTables() {
		createTables(null);
	}

	public static void main(final String[] args) throws IOException {
		final Configuration cfg = Persistor.createConfiguration();

		final Properties props = ResourceUtil.getProps("tmp/hibernate.properties");

		cfg.setProperties(props);

		final TableCreator tc = new TableCreator(cfg);
		tc.createTables("src/test/sql/audit_tables.sql-" + AuditServerConstants.ISODATE_SDF.format(new Date()));
	}
}

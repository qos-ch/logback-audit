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

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.audit.persistent.AuditEventShaper;
import ch.qos.logback.audit.persistent.NullAEShaper;
import ch.qos.logback.audit.persistent.Persistor;
import ch.qos.logback.audit.persistent.SQLServerAEShaper;
import ch.qos.logback.audit.server.helper.ResourceUtil;

public class ServletContextListener implements
    javax.servlet.ServletContextListener {

  Logger logger = LoggerFactory.getLogger(ServletContextListener.class);
  String applicationName;
  Object lock = new Object();
  AuditServer auditServer;
  
  public void contextDestroyed(ServletContextEvent sce) {
    logger.info("Resetting configuration");
    Persistor.resetConfiguration(lock);
    auditServer.close();
  }

  public void contextInitialized(ServletContextEvent sce) {
    logger.debug("ContextListener.contextInitialized called");
    ServletContext servletContext = sce.getServletContext();
    String portStr = servletContext
        .getInitParameter(AuditServerConstants.PORT_INIT_PARAM);
    if (portStr == null || "".equals(portStr)) {
      logger.error("The PORT argument was not set in web.xml. Aborting");
      return;
    }
    int port = Integer.parseInt(portStr);

    AuditEventShaper auditEventShaper;
    String rdbmsDialect = servletContext
        .getInitParameter(AuditServerConstants.RDBMS_DIALECT_INIT_PARAM);
    if (AuditServerConstants.SQLSERVER_2005_DIALECT_VALUE
        .equalsIgnoreCase(rdbmsDialect)) {
      logger.info("Will shape events accord to SQL Server 2005 requirements");
      auditEventShaper = new SQLServerAEShaper();
    } else {
      auditEventShaper = new NullAEShaper();
    }

    applicationName = servletContext.getServletContextName();
    logger.debug("applicationName={}", applicationName);
    Configuration cfg = Persistor.createConfiguration();
    ResourceUtil.setApplicationName(applicationName);
    Properties props = ResourceUtil.getProps(applicationName
        + "/hibernate.properties");

    cfg.setProperties(props);

    Persistor.setConfiguration(cfg, lock);
    auditServer = new AuditServer(port, new AuditEventPersistor(auditEventShaper));
    auditServer.start();
    servletContext.setAttribute(AuditServerConstants.AUDIT_SERVER_REFERENCE,
        auditServer);
  }
}

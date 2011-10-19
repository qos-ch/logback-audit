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

package ch.qos.logback.audit.persistent;

import java.sql.SQLException;

import junit.framework.TestCase;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.HSQLDialect;
import org.hsqldb.Server;
import org.hsqldb.ServerConstants;

import ch.qos.logback.audit.server.TableCreator;

public class PersistorTestBase extends TestCase {

  Object cfgLock = new Integer(13);

  Server server;
  String user = "sa";
  String password = "";
  String url = null;

  // boolean isNetwork = true;
  boolean memMode = true;

  public PersistorTestBase(String name) {
    super(name);
  }

  protected void setUp() throws SQLException {

    if (memMode) {
      url = "jdbc:hsqldb:mem:test;sql.enforce_strict_size=true";
      server = new Server();
      server.setDatabaseName(0, "test");
      server.setDatabasePath(0, url);
      //server.setLogWriter(new PrintWriter(System.out));
      //server.setErrWriter(new PrintWriter(System.out));
      server.setLogWriter(null);
      server.setErrWriter(null);
      server.setTrace(false);
      server.setSilent(true);


      server.start();

      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
      }
    } else {
      url = "jdbc:hsqldb:hsql://localhost:4808/test";
    }
    Configuration cfg  = buildConfiguration();
    //createTables(cfg);
    Persistor.setConfiguration(cfg, cfgLock);
  }

  protected void tearDown() throws Exception {
    super.tearDown();
    server.stop();
    int waitCount = 0;
    while (server.getState() != ServerConstants.SERVER_STATE_SHUTDOWN) {
      try {
        waitCount++;
        if (waitCount == 5) {
          throw new IllegalStateException("HSQLDB server could not be stopped");
        }
        Thread.sleep(1);
      } catch (InterruptedException e) {
      }
    }

  }

  Configuration buildConfiguration() {
    Configuration cfg = Persistor.createConfiguration();

    cfg.setProperty(Environment.USER, user);
    cfg.setProperty(Environment.PASS, password);
    cfg.setProperty(Environment.DIALECT, HSQLDialect.class.getName());
    cfg.setProperty(Environment.URL, url);
    cfg.setProperty(Environment.DRIVER, org.hsqldb.jdbcDriver.class.getName());
    cfg.setProperty(Environment.SHOW_SQL, "false");
    cfg.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
    
   
    return cfg;
  }

  void createTables(Configuration cfg) {
    TableCreator tc = new TableCreator(cfg);
    tc.createTables();
  }
}

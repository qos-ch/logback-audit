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

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.audit.AuditEvent;
import ch.qos.logback.audit.AuditEventBuilder;
import ch.qos.logback.audit.AuditException;
import ch.qos.logback.audit.client.Auditor;
import ch.qos.logback.audit.client.AuditorFacade;
import ch.qos.logback.audit.client.AuditorFactory;
import ch.qos.logback.audit.helper.RandUtil;
import ch.qos.logback.core.util.StatusPrinter;

public class AuditServerReconnectionTest extends TestCase {

  AuditEventLister auditEventLister = new AuditEventLister(1);
  AuditServer as;

  Logger logger = LoggerFactory.getLogger(AuditServerReconnectionTest.class);

  int port = RandUtil.getRandomPort();

  public AuditServerReconnectionTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    System.setProperty("port", String.valueOf(port));

  }

  protected void tearDown() throws Exception {
    super.tearDown();
    logger.debug("in tearDown");
    System.clearProperty(AuditorFactory.AUTOCONFIG_FILE_PROPERTY);
    System.clearProperty("port");
    AuditorFactory.reset();
    if (as != null) {
      as.close();
    } else {
      // throw new IllegalStateException("as should not be null");
    }
    logger.debug("leaving tearDown");
  }

  public void testEarlyServerLaunch() throws AuditException,
      InterruptedException {
    fireServer();
    Thread.sleep(100);

    System.setProperty(AuditorFactory.AUTOCONFIG_FILE_PROPERTY, "okTest.xml");
    AuditorFactory
        .setApplicationName(AuditServerTestContants.AUDIT_SERVER_TEST_APP_NAME);

    AuditorFacade af = new AuditorFacade("s", "v", "o");
    af.audit();
    assertEquals(1, auditEventLister.auditEventList.size());
  }

  public void testLateServerLaunch() throws AuditException,
      InterruptedException {
    System.setProperty(AuditorFactory.AUTOCONFIG_FILE_PROPERTY, "okTest.xml");
    AuditorFactory
        .setApplicationName(AuditServerTestContants.AUDIT_SERVER_TEST_APP_NAME);

    AuditorFacade af = new AuditorFacade("s", "v", "o");

    try {
      af.audit();
      fail("We should not be able to send events to a inexistent server");
    } catch (AuditException e) {
      e.printStackTrace();
    }
    fireServer();

    // reconnectionDelay 50
    Thread.sleep(100 + 50);

    Auditor auditor = AuditorFactory.getAuditor();
    StatusPrinter.print(auditor);
    AuditEventBuilder awb = auditor.newAuditEventBuilder();
    awb.setSubject("asd");
    awb.setVerb("add");
    awb.build();

    AuditEvent auditEvent = awb.build();
    auditor.log(auditEvent);
    assertEquals(1, auditEventLister.auditEventList.size());
  }

  void fireServer() {
    System.out.println("*********** fireServer");
    as = new AuditServer(port, auditEventLister);
    as.start();
  }
}

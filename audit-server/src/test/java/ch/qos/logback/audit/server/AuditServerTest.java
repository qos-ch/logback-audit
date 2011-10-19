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
import ch.qos.logback.audit.client.AuditorFactory;
import ch.qos.logback.audit.helper.RandUtil;
import ch.qos.logback.core.util.StatusPrinter;

public class AuditServerTest extends TestCase {

  AuditEventLister auditEventLister = new AuditEventLister(1);
  AuditServer as;

  Logger logger = LoggerFactory.getLogger(AuditServerTest.class);

  int port = RandUtil.getRandomPort();

  public AuditServerTest(String name) {
    super(name);
    System.setProperty("port", String.valueOf(port));
  }

  protected void setUp() throws Exception {
    super.setUp();
    System.setProperty("port", String.valueOf(port));
    as = new AuditServer(port, auditEventLister);
    as.start();
    // let the audit server thread get a head start
    Thread.sleep(10);
  }

  protected void tearDown() throws Exception {
    super.tearDown();
    logger.debug("in tearDown");
    System.clearProperty(AuditorFactory.AUTOCONFIG_FILE_PROPERTY);
    System.clearProperty("port");
 
    logger.debug("before reset");
    AuditorFactory.reset();

    logger.debug("after reset");
    
    as.close();
    logger.debug("leaving tearDown");
    
  }

  public void testBasic() throws AuditException, InterruptedException {
    System.setProperty(AuditorFactory.AUTOCONFIG_FILE_PROPERTY,
    "okTest.xml");
    AuditorFactory.setApplicationName(AuditServerTestContants.AUDIT_SERVER_TEST_APP_NAME);
    
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

  public void testLoop() throws AuditException, InterruptedException {
    System.setProperty(AuditorFactory.AUTOCONFIG_FILE_PROPERTY,
    "okTest.xml");
    AuditorFactory.setApplicationName(AuditServerTestContants.AUDIT_SERVER_TEST_APP_NAME);
    Auditor auditor = AuditorFactory.getAuditor();
    
    int loopLen = 10;
    for (int i = 0; i < loopLen; i++) {
      AuditEventBuilder awb = auditor.newAuditEventBuilder();

      awb.setSubject("loopTest");
      awb.setVerb("add");
      awb.setObject("num " + i);
      awb.build();
      auditor.log(awb.build());
    }
    assertEquals(loopLen, auditEventLister.auditEventList.size());
  }

  public void testException() throws AuditException, InterruptedException {
    System.setProperty(AuditorFactory.AUTOCONFIG_FILE_PROPERTY,
    "okTest.xml");
    AuditorFactory.setApplicationName(AuditServerTestContants.AUDIT_SERVER_TEST_APP_NAME);
    Auditor auditor = AuditorFactory.getAuditor();

    int loopLen = 10;
    for (int i = 0; i < loopLen; i++) {
      AuditEventBuilder awb = auditor.newAuditEventBuilder();

      awb.setSubject("loopTest");
      awb.setVerb("add");
      awb.setObject("num " + i);
      auditor.log(awb);
    }

    assertEquals(loopLen, auditEventLister.auditEventList.size());

    AuditEventBuilder awb = auditor.newAuditEventBuilder();

    awb.setSubject("loopTest");
    awb.setVerb(AuditEventLister.THROW_EXCEPTION);
    awb.setObject("num " + 0);
    try {
      auditor.log(awb);
      fail("Missing exception");
    } catch (AuditException e) {
      e.printStackTrace();
    }
  }

  public void testUnreachable() throws AuditException, InterruptedException {
    System.setProperty(AuditorFactory.AUTOCONFIG_FILE_PROPERTY,
    "unreachableTest.xml");
    logger.debug("before AuditorFactory.setApplicationName");
    AuditorFactory.setApplicationName(AuditServerTestContants.AUDIT_SERVER_TEST_APP_NAME);
    logger.debug("after AuditorFactory.setApplicationName");

    try {
      logger.debug("before log");
      Auditor auditor = AuditorFactory.getAuditor();
      auditor.log((AuditEvent) null);
      logger.debug("after log");
      fail("Missing remote server should have caused an exception");
    } catch(AuditException e) {
    }
    
    StatusPrinter.print(AuditorFactory.getAuditor());
  }
}

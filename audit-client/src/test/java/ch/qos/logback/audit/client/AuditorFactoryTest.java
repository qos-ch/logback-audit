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

import junit.framework.TestCase;
import junit.framework.TestSuite;
import ch.qos.logback.audit.AuditEvent;
import ch.qos.logback.audit.AuditEventBuilder;
import ch.qos.logback.audit.AuditException;

public class AuditorFactoryTest extends TestCase {

  
  public AuditorFactoryTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();

  }

  protected void tearDown() throws Exception {
    super.tearDown();
    System.clearProperty(AuditorFactory.AUTOCONFIG_FILE_PROPERTY);
    AuditorFactory.reset();
  }

  public void testBasic() throws AuditException {
    
    System.setProperty(AuditorFactory.AUTOCONFIG_FILE_PROPERTY,
        "basicAuditorFactoryTest.xml");
    AuditorFactory.setApplicationName(AuditClientConstants.TEST_CLIENT_APPLICATION_NAME);
    
    Auditor auditor = AuditorFactory.getAuditor();
    assertNotNull(auditor);
    assertNotNull(auditor.getAuditAppender());
    AuditEventBuilder builder = auditor.newAuditEventBuilder();
    builder.setObject("o");
    builder.setVerb("v");
    AuditEvent auditEvent = builder.build();
    auditor.log(auditEvent);

    ListAuditAppender laa = (ListAuditAppender) auditor.getAuditAppender();
    assertEquals("x", laa.getName());
    assertEquals(1, laa.getAuditEventList().size());
    assertEquals(auditEvent, laa.getAuditEventList().get(0));
  }
  

  public void testUnfound() throws AuditException {
    System.setProperty(AuditorFactory.AUTOCONFIG_FILE_PROPERTY, "not_there.xml");
    
    try {
      AuditorFactory.setApplicationName(AuditClientConstants.TEST_CLIENT_APPLICATION_NAME);
      fail("Should have thrown an AuditException");
    } catch (AuditException e) {
      //System.out.println(e.getMessage());
      assertTrue(e.getMessage().startsWith(
          "Failed to find configuration file [selfTest/not_there.xml]."));
    }
  }
  
  public void XXXtestWithServer() throws AuditException {
    System.setProperty(AuditorFactory.AUTOCONFIG_FILE_PROPERTY,
        "serverTest.xml");
    AuditorFactory.setApplicationName(AuditClientConstants.TEST_CLIENT_APPLICATION_NAME);

    Auditor auditor = AuditorFactory.getAuditor();
    assertNotNull(auditor);
    assertNotNull(auditor.getAuditAppender());
    AuditEventBuilder builder = auditor.newAuditEventBuilder();
    builder.setObject("o");
    builder.setVerb("v");
    AuditEvent auditEvent = builder.build();
    auditor.log(auditEvent);
  }
  
  public static junit.framework.Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(new AuditorFactoryTest("testBasic"));
    suite.addTest(new AuditorFactoryTest("testUnfound"));
    //suite.addTestSuite(AuditorFactoryTest.class);
    return suite;
  }
  
}

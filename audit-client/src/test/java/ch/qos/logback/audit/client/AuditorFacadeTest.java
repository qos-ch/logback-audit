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

import ch.qos.logback.audit.AuditEvent;
import ch.qos.logback.audit.AuditEventBuilder;
import ch.qos.logback.audit.AuditException;
import junit.framework.TestCase;

public class AuditorFacadeTest extends TestCase {

  public AuditorFacadeTest(String name) throws AuditException {
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
    AuditorFactory
    .setApplicationName(AuditClientConstants.TEST_CLIENT_APPLICATION_NAME);
    
    Auditor auditor = AuditorFactory.getAuditor();
    assertNotNull(auditor);
    assertNotNull(auditor.getAuditAppender());

    new AuditorFacade("s", "v", null).audit();
    AuditEventBuilder witnessBuilder = auditor.newAuditEventBuilder();
    witnessBuilder.setSubject("s");
    witnessBuilder.setVerb("v");
    AuditEvent witnessEvent = witnessBuilder.build();

    ListAuditAppender laa = (ListAuditAppender) auditor.getAuditAppender();
    assertEquals("x", laa.getName());
    assertEquals(1, laa.getAuditEventList().size());


    AuditEvent back = laa.getAuditEventList().get(0);
    assertEquals(witnessEvent, back);
  }

  public void testWithPredicates() throws AuditException {

    System.setProperty(AuditorFactory.AUTOCONFIG_FILE_PROPERTY,
        "basicAuditorFactoryTest.xml");
    AuditorFactory
    .setApplicationName(AuditClientConstants.TEST_CLIENT_APPLICATION_NAME);
    
    Auditor auditor = AuditorFactory.getAuditor();
    assertNotNull(auditor);
    assertNotNull(auditor.getAuditAppender());

    new AuditorFacade("a subject", "a verb", "some object").add(
        "key1", "val1").add("key2", "val2").audit();

    ListAuditAppender laa = (ListAuditAppender) auditor.getAuditAppender();
    assertEquals("x", laa.getName());
    assertEquals(1, laa.getAuditEventList().size());

    AuditEventBuilder builder = auditor.newAuditEventBuilder();
    builder.setSubject("a subject");
    builder.setVerb("a verb");
    builder.setObject("some object");

    builder.addPredicate("key1", "val1");
    builder.addPredicate("key2", "val2");
    
    AuditEvent witnessEvent = builder.build();
    AuditEvent back = laa.getAuditEventList().get(0);
    witnessEvent.setTimestamp(back.getTimestamp());
    assertEquals(witnessEvent, back);
  }

}

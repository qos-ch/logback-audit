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
import ch.qos.logback.core.util.StatusPrinter;
import junit.framework.TestCase;

public class APITest extends TestCase {

  public void testBasic() throws AuditException {
      // audirContext and Auditor can be merged into one
      Auditor auditContext = new Auditor();

      ListAuditAppender laa = new ListAuditAppender();
      laa.setContext(auditContext);
      laa.setName("saa");
      laa.start();

      auditContext.setAuditAppender(laa);
      
      AuditEventBuilder awb = auditContext.newAuditEventBuilder();

      awb.setSubject("testBasic");
      awb.setVerb("APITest");
      awb.build();

      AuditEvent auditEvent = awb.build();
      auditContext.log(auditEvent);
      assertEquals(1, laa.auditEventList.size());
      assertEquals(auditEvent, laa.auditEventList.get(0));
      StatusPrinter.print(auditContext);
  }
}

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

package ch.qos.logback.audit;

import java.sql.SQLException;
import java.util.List;

import ch.qos.logback.audit.Application;
import ch.qos.logback.audit.AuditEvent;
import ch.qos.logback.audit.Predicate;
import ch.qos.logback.audit.persistent.AuditEventDAO;
import ch.qos.logback.audit.persistent.PersistorTestBase;

// This test must be located in the ch.qos.logback.audit package
// in order to access AuditEvent

public class AuditEventDAOTest extends PersistorTestBase {

  long diff = System.currentTimeMillis();

  public AuditEventDAOTest(String name) {
    super(name);
  }

  protected void setUp() throws SQLException {
    super.setUp();
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public void testBasic() {
    AuditEvent ae = new AuditEvent();
    ae.setSubject("s");
    ae.setVerb("v");
    ae.setObject("o");
    Application clientApp = new Application("oapp", "1.2.3.3");
    ae.setClientApplication(clientApp);

    AuditEventDAO.save(ae);
    assertNotNull(ae.getId());

    AuditEvent aeBack = AuditEventDAO.findById(ae.getId());
    assertEquals(ae.getId(), aeBack.getId());
    assertEquals(ae, aeBack);

    AuditEventDAO.delete(aeBack);
    assertNonExistence(ae.getId());
  }

  public void testMap() {

    AuditEvent ae = new AuditEvent();
    ae.setSubject("s");
    ae.setVerb("v");
    ae.setObject("o");
    Application clientApp = new Application("oapp", "1.2.3.3");
    ae.setClientApplication(clientApp);

    Predicate p = new Predicate("color", "green");
    ae.addPredicate(p);

    AuditEventDAO.save(ae);
    assertNotNull(ae.getId());

    AuditEvent aeBack = AuditEventDAO.findById(ae.getId());
    assertEquals(ae.getId(), aeBack.getId());

    // AuditEventDAO.delete(aeBack);
    // assertNonExistence(ae.getId());
  }

  public void testFindAll() {
    
    List<AuditEvent> initialList = AuditEventDAO.findAll();
    
    Application clientApp = new Application("oapp", "1.2.3.3");
    {
      AuditEvent ae = new AuditEvent();
      ae.setSubject("s1");
      ae.setVerb("v1");
      ae.setObject("o1");
      ae.setClientApplication(clientApp);

      Predicate p = new Predicate("color", "green");
      ae.addPredicate(p);

      AuditEventDAO.save(ae);
    }
    
    {
      AuditEvent ae = new AuditEvent();
      ae.setSubject("s2");
      ae.setVerb("v2");
      ae.setObject("o2");
      ae.setClientApplication(clientApp);

      Predicate p = new Predicate("color", "green");
      ae.addPredicate(p);

      AuditEventDAO.save(ae);
    }

    List<AuditEvent> lae = AuditEventDAO.findAll();
    assertEquals(initialList.size()+2, lae.size());
    
    for(AuditEvent ae: lae) {
      if(!initialList.contains(ae)) {
        AuditEventDAO.delete(ae);
        assertNonExistence(ae.getId());
      }
     }
  }

  public void testBigColumn() {
    AuditEvent ae = new AuditEvent();
    StringBuffer subjectBuf = new StringBuffer();
    for(int i = 0; i < 100; i++) {
      subjectBuf.append("subject_"+i);
    }
    ae.setSubject(subjectBuf.toString());
    ae.setVerb("v");
    StringBuffer objectBuf = new StringBuffer();
    for(int i = 0; i < 100; i++) {
      objectBuf.append("object__"+i);
    }
    ae.setObject(objectBuf.toString());
    Application clientApp = new Application("oapp", "1.2.3.3");
    ae.setClientApplication(clientApp);

    StringBuffer predicateBuf = new StringBuffer();
    for(int i = 0; i < 100; i++) {
      predicateBuf.append("predicate_"+i);
    }
    ae.addPredicate(new Predicate("pkey", predicateBuf.toString()));
    
    AuditEventDAO.save(ae);
    assertNotNull(ae.getId());

    AuditEvent aeBack = AuditEventDAO.findById(ae.getId());
    assertEquals(ae.getId(), aeBack.getId());
    assertEquals(ae, aeBack);

    AuditEventDAO.delete(aeBack);
    assertNonExistence(ae.getId());
  }
  
  void assertNonExistence(Long id) {
    AuditEvent aeBack = AuditEventDAO.findById(id);
    assertNull(aeBack);
  }

}

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

import java.io.IOException;

import ch.qos.logback.audit.client.Auditor;
import ch.qos.logback.audit.client.net.SocketAuditAppender;
import ch.qos.logback.audit.server.AuditServerConstants;
import ch.qos.logback.core.util.StatusPrinter;

public class GoClient {

  static Auditor auditor = new Auditor();
  static SocketAuditAppender saa = new SocketAuditAppender();
  
  /**
   * @param args
   * @throws AuditException
   * @throws IOException 
   */
  public static void main(String[] args) throws AuditException, IOException {
    init();
    for (int i = 0; i < 1000; i++) {
      System.out.print(".");
      logMessage("add", "verb", "object" + i);
    }

    saa.stop();
    //StatusPrinter.print(auditor);
    System.out.println("Exiting");
  }
 
  static void init() {
    
    saa.setContext(auditor);
    saa.setPort(AuditServerConstants.DEFAULT_PORT);
    saa.setName("saa");
    saa.setRemoteHost("localhost");
    saa.start();
    auditor.setAuditAppender(saa);

    // fail("shouldn't joran throw an exception if the server is not there?")
    StatusPrinter.print(auditor);

    Application capp = new Application("GoClient", "localhost");
    auditor.setClientApplication(capp);
  }

  static void logMessage(String subject, String verb, String ob)
      throws AuditException {
    int LIMIT = 20;
    AuditEventBuilder awb = auditor.newAuditEventBuilder();
    awb.setSubject(subject);
    awb.setVerb(verb);
    awb.setObject(ob);
    awb.build();

    AuditEvent auditEvent = awb.build();
    int attempts = 0;
    while ((attempts++ < LIMIT)) {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e1) {
      }
      try {
        auditor.log(auditEvent);
        System.out.print("x");
        break;
      } catch (AuditException e) {
        if(attempts != LIMIT) {
        System.out.println(e.getMessage());
        System.out.println("Failed attempt. Will attempt again");
        } else {
          throw e;
        }
      }
    }
  }
}

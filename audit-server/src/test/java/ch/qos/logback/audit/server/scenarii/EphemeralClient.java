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

package ch.qos.logback.audit.server.scenarii;

import ch.qos.logback.audit.AuditException;
import ch.qos.logback.audit.client.AuditConstants;
import ch.qos.logback.audit.client.AuditorFacade;
import ch.qos.logback.audit.client.AuditorFactory;

public class EphemeralClient extends Thread implements AuditConstants {

  int runLength;

  EphemeralClient(String name, int runLength) {
    super(name);
    this.runLength = runLength;
  }

  @Override
  public void run() {
    System.out.println("Starting " + getName());
    for (int i = 0; i < runLength; i++) {
      try {
        new AuditorFacade(getName(), "EMPHEMERAL", "CLIENT" + i).add(
            SUBJECT_TYPE, "user").add(CRUD_TYPE, READ).audit();
        sleep(100);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) throws InterruptedException, AuditException {
    AuditorFactory.setApplicationName("ephemeral");
    for (int i = 0; i < 4; i++) {
      launchBatch(i, 20);
    }
    System.out.println("Done");
  }

  static void launchBatch(int batchNum, int numOfClients)
      throws InterruptedException {

    EphemeralClient ec[] = new EphemeralClient[numOfClients];
    for (int i = 0; i < numOfClients; i++) {
      ec[i] = new EphemeralClient("EPHEMERAL-" + batchNum + "-" + i, 100);
      ec[i].start();
    }

    for (int i = 0; i < numOfClients; i++) {
      System.out.println("Joining " + ec[i].getName());
      ec[i].join();
    }

  }
}

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

import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.audit.AuditException;
import ch.qos.logback.audit.client.AuditConstants;
import ch.qos.logback.audit.client.AuditorFacade;
import ch.qos.logback.audit.client.AuditorFactory;

public class UserAccessScenario implements AuditConstants {

  static final String APP_NAME = "userAccessScenario";

  static final int NUMBER_OF_ACCOUNTS = 1000;
  static final int NUMBER_OF_USERS = 50;
  static final int NUMBER_AUDIT_ENTRIES = 1000 * 1000;

  static List<String> accountList = new ArrayList<String>();
  static List<String> userList = new ArrayList<String>();

  public static void main(String[] args) throws AuditException {
    AuditorFactory.setApplicationName("userAccessScenario");

    makeUserList();
    makeAccountList();

    playScenario();
  }
  
  static final int STEP = 100;
  static final int COLS = 60;
  static final int BIG_STEP = COLS*STEP;
  

  static void playScenario() throws AuditException {
    long start = System.currentTimeMillis();
    long s =  System.currentTimeMillis();
    System.out.println("Will insert "+ NUMBER_AUDIT_ENTRIES+" entries");
    for (int i = 0; i < NUMBER_AUDIT_ENTRIES; i++) {
      String userName = RandomUtil.getRandomEntry(userList);
      String accountNum = RandomUtil.getRandomEntry(accountList);
      new AuditorFacade(userName, "ACCESS", accountNum).add(SUBJECT_TYPE,
          "user").add(CRUD_TYPE, READ).audit();
      if ((i % STEP) == 0) {
        System.out.print('.');
        if ((i % (BIG_STEP)) == 0 && i != 0) {
          long t = System.currentTimeMillis();
          System.out.println(((t-s) / BIG_STEP) + " millis on average");
          s = t;
        }
      }
    }
    System.out.println();
    long end = System.currentTimeMillis();
    long duration = end - start;
    System.out.println("Inserted " + NUMBER_AUDIT_ENTRIES + " events in "
        + duration / 1000 + " seconds, that is "
        + (duration / NUMBER_AUDIT_ENTRIES) + " millis on average");

  }

  static void makeAccountList() {
    for (int i = 0; i < NUMBER_OF_ACCOUNTS; i++) {
      int n = RandomUtil.nextInt(1000000);
      accountList.add(String.valueOf(n));
    }
  }

  static void makeUserList() {
    for (int i = 0; i < NUMBER_OF_USERS; i++) {
      String name = RandomUtil.randomName();
      // System.out.println(name);
      userList.add(name);
    }
  }

}

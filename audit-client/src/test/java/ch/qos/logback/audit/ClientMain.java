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

import ch.qos.logback.audit.client.AuditorFacade;
import ch.qos.logback.audit.client.AuditorFactory;
import ch.qos.logback.core.util.StatusPrinter;

public class ClientMain {

  public static void main(String[] args) {
    try {

      AuditorFactory.setApplicationName("NetBaby"); 
      new AuditorFacade("baby", "take", "step").audit();
      StatusPrinter.print(AuditorFactory.getAuditor());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

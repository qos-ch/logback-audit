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

package examples;

import ch.qos.logback.audit.AuditException;
import ch.qos.logback.audit.client.AuditorFacade;
import ch.qos.logback.audit.client.AuditorFactory;

public class SecondStep {

  public static void main(String[] args) throws AuditException {
    // First set the application name. Setting the application will
    // also cause logback-audit to configure itself.
    AuditorFactory.setApplicationName("SecondStep");
    // One the application name is set and logback-audit is configured, you 
    // can start auditing
    new AuditorFacade("baby", "take", "step").audit();
  }
}

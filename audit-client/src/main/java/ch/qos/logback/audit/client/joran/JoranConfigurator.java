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

package ch.qos.logback.audit.client.joran;


import ch.qos.logback.audit.client.joran.action.AuditAppenderAction;
import ch.qos.logback.audit.client.joran.action.AuditorAction;
import ch.qos.logback.core.joran.GenericConfigurator;
import ch.qos.logback.core.joran.action.NestedBasicPropertyIA;
import ch.qos.logback.core.joran.action.NestedComplexPropertyIA;
import ch.qos.logback.core.joran.spi.Interpreter;
import ch.qos.logback.core.joran.spi.Pattern;
import ch.qos.logback.core.joran.spi.RuleStore;



/**
 * JoranConfigurator for the audit module.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class JoranConfigurator extends GenericConfigurator {

  @Override
  public void addInstanceRules(RuleStore rs) {
    rs.addRule(new Pattern("auditor"), new AuditorAction());
    rs.addRule(new Pattern("auditor/appender"), new AuditAppenderAction());
  }

  @Override
  protected void addImplicitRules(Interpreter interpreter) {
    // The following line adds the capability to parse nested components
    NestedComplexPropertyIA nestedComplexPropertyIA = new NestedComplexPropertyIA();
    nestedComplexPropertyIA.setContext(context);
    interpreter.addImplicitAction(nestedComplexPropertyIA);

    NestedBasicPropertyIA nestedBasicIA = new NestedBasicPropertyIA();
    nestedBasicIA.setContext(context);
    interpreter.addImplicitAction(nestedBasicIA);
  }

}

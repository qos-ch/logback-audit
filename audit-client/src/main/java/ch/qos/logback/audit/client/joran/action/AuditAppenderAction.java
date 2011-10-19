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

package ch.qos.logback.audit.client.joran.action;

import org.xml.sax.Attributes;

import ch.qos.logback.audit.client.AuditAppender;
import ch.qos.logback.audit.client.Auditor;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.util.OptionHelper;

public class AuditAppenderAction extends Action {

  AuditAppender auditAppender;
  private boolean inError = false;
  
  @Override
  public void begin(InterpretationContext ec, String name, Attributes attributes)
      throws ActionException {
    String className = attributes.getValue(CLASS_ATTRIBUTE);

    // We are just beginning, reset variables
    auditAppender = null;
    inError = false;
    
    try {
      addInfo("About to instantiate appender of type ["+className+"]");

      auditAppender = (AuditAppender) OptionHelper.instantiateByClassName(
          className, ch.qos.logback.audit.client.AuditAppender.class, context);

      auditAppender.setContext(context);

      String appenderName = attributes.getValue(NAME_ATTRIBUTE);

      if (OptionHelper.isEmpty(appenderName)) {
        addWarn(
          "No appender name given for appender of type " + className + "].");
      } else {
        auditAppender.setName(appenderName);
        addInfo("Naming appender as [" + appenderName + "]");
      }

      //getLogger().debug("Pushing appender on to the object stack.");
      ec.pushObject(auditAppender);
    } catch (Exception oops) {
      inError = true;
      addError(
        "Could not create an Appender of type ["+className+"].", oops);
      throw new ActionException(ActionException.SKIP_CHILDREN, oops);
    }
  }

  @Override
  public void end(InterpretationContext ec, String name) throws ActionException {
    if (inError) {
      return;
    }
    Object o = ec.peekObject();

    if (o != auditAppender) {
      addWarn(
        "The object at the of the stack is not the appender named ["
        + auditAppender.getName() + "] pushed earlier.");
    } else {
      addInfo(
        "Popping appender named [" + auditAppender.getName()
        + "] from the object stack");
      ec.popObject();
    }

    addInfo(
        "Setting auditor's appender to appender named [" + auditAppender.getName()
        + "]");
    
    if (auditAppender instanceof LifeCycle) {
      ((LifeCycle) auditAppender).start();
    }

    Auditor ac = (Auditor) context; 
    ac.setAuditAppender(auditAppender);

  }

}

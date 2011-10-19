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
import ch.qos.logback.audit.AuditException;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;

public interface AuditAppender extends ContextAware, LifeCycle {

  /**
   * Get the name of this appender. The name uniquely identifies the appender.
   */
  public String getName();

  /**
   * Set the name of this appender. The name is used by other components to
   * identify this appender.
   * 
   */
  public void setName(String name);
  
  /**
   * This is where an appender accomplishes its work. Note that the argument 
   * is of type Object.
   * @param event
   */
  void doAppend(AuditEvent event) throws AuditException;

  
}

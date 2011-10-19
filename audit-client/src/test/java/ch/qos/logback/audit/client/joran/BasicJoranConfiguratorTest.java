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

import ch.qos.logback.audit.client.Auditor;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import junit.framework.TestCase;

public class BasicJoranConfiguratorTest extends TestCase {

  public BasicJoranConfiguratorTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public void testBasic() throws JoranException {
     Auditor ac = new Auditor();
     ac.setName("default");
     
     JoranConfigurator jc = new JoranConfigurator();
     jc.setContext(ac);
     jc.doConfigure("src/test/input/joran/lb-audit-basic.xml");
     
     assertNotNull(ac.getAuditAppender());
     StatusPrinter.print(ac);
  }
}

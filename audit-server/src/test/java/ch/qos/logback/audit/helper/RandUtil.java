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

package ch.qos.logback.audit.helper;

import java.util.Random;

public class RandUtil {

  
  static Random random = new Random(99807);
  
  public static int getRandomInt() {
    return random.nextInt();
  }
  
  /**
   * Get a random number between 0 and u 
   * @param u
   * @return
   */
  public static int getRandomInt(int u) {
    if(u <= 0) {
      throw new IllegalArgumentException("input cannot be negative but was "+u);
    }
    return random.nextInt(u);
  }
  
  public static int getRandomPort() {
    int offset = getRandomInt(10000);
    return 1024 + offset;
  }
}

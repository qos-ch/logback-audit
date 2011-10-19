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

import java.util.List;
import java.util.Random;

public class RandomUtil {

  private final static long SEED = 7413;

  private final static Random random = new Random(SEED);

  private final static int AVERAGE_USER_NAME_LEN = 6;
  private final static int AVERAGE_USER_NAME_DEV = 3;

  public static boolean oneInFreq(int freq) {
    return (random.nextInt(freq) % freq) == 0;
  }

  static String getRandomEntry(List<String> list) {
    int len = list.size();
    int index = random.nextInt(len);
    return list.get(index);
  }
  
  static int nextInt(int n) {
    return random.nextInt(n);
  }

  public static String randomName() {

    int len = gaussianAsPositiveInt(AVERAGE_USER_NAME_LEN, AVERAGE_USER_NAME_DEV);
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < len; i++) {
      int offset = random.nextInt(26);
      char c = (char) ('a' + offset);
      buf.append(c);
    }
    return buf.toString();
  }
  
  /**
   * Approximate a gaussian distrib with only only positive integer values
   * 
   * @param average
   * @param stdDeviation
   * @return
   */
  public static int gaussianAsPositiveInt(int average, int stdDeviation) {
    if (average < 1) {
      throw new IllegalArgumentException(
          "The average must not be smaller than 1.");
    }

    if (stdDeviation < 1) {
      throw new IllegalArgumentException(
          "The stdDeviation must not be smaller than 1.");
    }

    double d = random.nextGaussian() * stdDeviation + average;
    int result = 1;
    if (d > 1.0) {
      result = (int) Math.round(d);
    }
    return result;
  }
}

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

import java.util.Date;

public class MainP extends Thread {

	int div;
	
	public static void main(String[] args) {
		
		
		int d = 16;
		Thread[] tA = new Thread[d];
		for(int x = 0;x<d;x++) {
			tA[x] = new MainP(d);
		}
		System.out.println("START "+new Date());
		for(Thread t: tA) {
			t.start();
		}
		
		//doIt(); // 10400
		//doIt(); // 10400

	}

	MainP(int div) {
		this.div = div;
	}
	public void run() {
		doIt(div);
	}
	
	public static void doIt(int div) {
		final long MIL = 1000 * 1000;
		final long LEN = 20 * MIL/div;

		final long start = System.currentTimeMillis();
		for (long i = 0; i < LEN; i++) {
			Math.tan(i);
			if (i % (MIL / 5) == 0) {
				System.out.print(".");
			}
		}

		long end = System.currentTimeMillis();

		System.out.println("time " + (end - start));
		System.out.println("exit: "+new Date());
	}

}

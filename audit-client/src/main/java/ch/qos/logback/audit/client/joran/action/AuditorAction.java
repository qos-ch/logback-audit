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

import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.util.StatusPrinter;

public class AuditorAction extends Action {
	static final String INTERNAL_DEBUG_ATTR = "debug";
	boolean debugMode = false;

	@Override
	public void begin(final InterpretationContext ec, final String name, final Attributes attributes) throws ActionException {
		final String debugAttrib = attributes.getValue(INTERNAL_DEBUG_ATTR);

		if (debugAttrib == null || debugAttrib.equals("")
				|| debugAttrib.equals("false") || debugAttrib.equals("null")) {
			addInfo("Ignoring " + INTERNAL_DEBUG_ATTR + " attribute.");
		} else {
			debugMode = true;
		}

		//    if ((nameAttrib == null) || nameAttrib.equals("")) {
		//      String errMsg = "Empty  " + NAME_ATTRIBUTE + " attribute.";
		//      addError(errMsg);
		//      throw new ActionException(ActionException.SKIP_CHILDREN);
		//    }

		// the context is appender attachable, so it is pushed on top of the stack
		ec.pushObject(getContext());
	}

	@Override
	public void end(final InterpretationContext ec, final String name) {
		if (debugMode) {
			addInfo("End of configuration.");
			StatusPrinter.print(context);
		}

		ec.popObject();
	}
}

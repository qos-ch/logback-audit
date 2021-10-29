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

package examples.access;

import ch.qos.logback.audit.AuditException;
import ch.qos.logback.audit.client.AuditorFacade;
import ch.qos.logback.audit.client.AuditorFactory;

/**
 * A simple application illustrating calls to the logback-audit API.
 *
 * @author Ceki Gulcu
 */
public class AccessRightsAdministrator {

	static String ACCESS_RIGHTS_ADMIN_APPLICATION_NAME = "AccessRightsAdmin";

	static String ADD_PERMISSION_VERB = "ADD_PERMISSION";
	static String REMOVE_PERMISSION_VERB = "REMOVE_PERMISSION";

	final PermissionMap permissionMap = new PermissionMap();

	/**
	 * Add a permission to a user.
	 *
	 * @param user
	 * @param permission
	 * @throws AccessException thrown in case of error while adding a permission
	 */
	public void addPermission(final User user, final Permission permission) throws AccessException {

		// add the permission
		permissionMap.addPermission(user, permission);

		// let us now audit the code

		// The following will create an AuditEvent with a subject equal to the
		// user's name, a verb equal to ADD_PERMISSION, an the object
		// equal to the name of permission passed as argument
		final AuditorFacade auditorFacade = new AuditorFacade(user.getName(), ADD_PERMISSION_VERB,
				permission.getName());
		try {
			// send the AuditEvent through an appender (usually to a server)
			auditorFacade.audit();
		} catch (final AuditException e) {
			// if an AuditException occurs, rethrow it as
			// an application failure (AccessException)
			throw new AccessException("Audit failure", e);
		}
	}

	/**
	 * Remove a permission from a user.
	 *
	 * @param user
	 * @param permission
	 * @throws AccessException thrown in case of error while removing a permission
	 */
	public void removePermission(final User user, final Permission permission) throws AccessException {
		permissionMap.removePermission(user, permission);

		final AuditorFacade auditorFacade = new AuditorFacade(user.getName(), REMOVE_PERMISSION_VERB,
				permission.getName());
		AuditHelper.audit(auditorFacade);

	}

	public static void main(final String[] args) throws AuditException, AccessException {
		AuditorFactory.setApplicationName(ACCESS_RIGHTS_ADMIN_APPLICATION_NAME);
		final AccessRightsAdministrator ara = new AccessRightsAdministrator();

		// let us simulate a session adding and removing permissions using
		// the AccessRightsAdministration instance just created

		// create 4 users, Alice, Bob, Carol and Dave
		final User alice = new User("alice");
		final User bob = new User("bob");
		final User carol = new User("carol");
		final User dave = new User("dave");

		// let us create 5 permission instances
		final Permission adminPerm = new Permission("administrator");
		final Permission createPerm = new Permission("create");
		final Permission commentPerm = new Permission("comment");
		final Permission votePerm = new Permission("vote");
		final Permission browsePerm = new Permission("browse");

		// grant Alice admin permissions
		ara.addPermission(alice, adminPerm);

		// grant Bob create, comment and voting permissions
		ara.addPermission(bob, createPerm);
		ara.addPermission(bob, commentPerm);
		ara.addPermission(bob, votePerm);

		// grant Carol browsing permissions
		ara.addPermission(carol, browsePerm);

		// grant Dave browsing permissions
		ara.addPermission(dave, browsePerm);

		// remove permissions granted to Bob
		ara.removePermission(bob, createPerm);
		ara.removePermission(bob, commentPerm);
		ara.removePermission(bob, votePerm);
	}

}

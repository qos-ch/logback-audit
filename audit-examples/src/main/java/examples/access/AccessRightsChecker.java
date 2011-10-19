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
 * As the name indicates, checks whether a user possesses a given permission.
 * 
 * AccessRightsChecker is also a stand-alone java application, simulating a very
 * simple and short simulation of invocation of a AccessRightsChecker
 * 
 * @author Ceki Gulcu
 */
public class AccessRightsChecker {

  /**
   * Constant used to identify this application.
   */
  static String ACCESS_RIGHTS_CHECKER_APPLICATION_NAME = "AccessRightsChecker";

  /*
   * The permissionMap used internally by AccessRightsChecker
   */
  final PermissionMap permissionMap;

  public AccessRightsChecker(PermissionMap permissionMap) {
    this.permissionMap = permissionMap;
  }

  /**
   * Does the user have the permission passed as parameter?
   * 
   * @param user the user to check against
   * @param permission the permission to check
   * @return true if the user has the permission, false otherwise.
   * @throws AccessException
   *                 thrown if there is a problem checking user permissions
   */
  public boolean hasPermission(User user, Permission permission)
      throws AccessException {
    boolean allowed = permissionMap.hasPermission(user, permission);
    if (!allowed) {
      AuditorFacade auditorFacade = new AuditorFacade(user.getName(),
          "ACCESS_DENIED", permission.getName());
      AuditHelper.audit(auditorFacade);
    }
    return allowed;
  }

  public static void main(String[] args) throws AuditException, AccessException {
    // Initialize logback-audit framework with the file
    // AccessRightsChecker/logback-audit.xml
    AuditorFactory.setApplicationName(ACCESS_RIGHTS_CHECKER_APPLICATION_NAME);

    // create an empty PermissionMap
    PermissionMap pm = new PermissionMap();

    // Create users Bob, Carol and Dave
    User bob = new User("Bob");
    User carol = new User("Carol");
    User dave = new User("Dave");

    // create a browse permission
    Permission browsePerm = new Permission("browse");

    // grant Dave browse permission
    pm.addPermission(dave, browsePerm);

    // create an AccessRightsChecker with the permissionMap
    // just instantiated
    AccessRightsChecker arc = new AccessRightsChecker(pm);

    // Bob does not have browse permissions. Thus, the call
    // will be audited.
    arc.hasPermission(bob, browsePerm);

    // Carol does not have browse permissions. Thus, the call
    // will be audited.
    arc.hasPermission(carol, browsePerm);

    // Dave does have browse permissions
    arc.hasPermission(dave, browsePerm);

  }
}

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionMap {

  Map<String, List<Permission>> map = new HashMap<String, List<Permission>>();

  void addPermission(User u, Permission p) {
    List<Permission> permissionList = map.get(u.getName());
    if (permissionList == null) {
      permissionList = new ArrayList<Permission>();
    }
    permissionList.add(p);
  }

  void removePermission(User u, Permission p) {
    List<Permission> permissionList = map.get(u.getName());
    if (permissionList == null) {
      return;
    }
    permissionList.remove(p);
  }

  boolean hasPermission(User u, Permission p) {
    List<Permission> permissionList = map.get(u.getName());
    if (permissionList == null) {
      return false;
    }
    return permissionList.contains(p);
  }
}

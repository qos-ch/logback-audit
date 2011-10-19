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

import java.io.Serializable;

public class Application implements Serializable {

  private static final long serialVersionUID = 62706289462L;

  String name;
  String ipAddress;

  // Hibernate requires a default Constructor
  public Application() {
  }

  public Application(String name, String ipAddress) {
    this.name = name;
    this.ipAddress = ipAddress;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  // Hibernate needs to be able to set the ipAdress
  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getName() {
    return name;
  }

  // Hibernate needs to be able to set the name
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
    result = PRIME * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final Application other = (Application) obj;
    if (ipAddress == null) {
      if (other.ipAddress != null)
        return false;
    } else if (!ipAddress.equals(other.ipAddress))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  @Override
  public String toString() {
    String retValue = "";

    retValue = "Appl(" + "name=" + this.name + ", ip=" + this.ipAddress + ")";

    return retValue;
  }

}

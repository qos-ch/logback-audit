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

package ch.qos.logback.audit.persistent;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import ch.qos.logback.audit.AuditEvent;

public class AuditEventDAO extends Persistor {
  
  final static String AE_CLASS = AuditEvent.class.getName();
  
  static public AuditEvent findById(Long id) throws HibernateException {
    Session s = null;
    try {
      s = openSession();
      return (AuditEvent) s.get(AuditEvent.class, id);
    } finally {
      close(s);
    }
  }

  @SuppressWarnings("unchecked")
  static public List<AuditEvent> findAll() throws HibernateException {
    Session s = null;
    try {
      s = openSession();
      Criteria criteria = s.createCriteria(AuditEvent.class);
      return criteria.list();
    } finally {
      close(s);
    }
  }

  // SELECT grp.subject_count, grp.subject FROM
  // (SELECT count(subject) as subject_count, subject from audit_event GROUP BY
  // subject) AS grp
  // ORDER BY grp.subject_count DESC;

  @SuppressWarnings("unchecked")
  static public List<Object[]> findMaxSubject() throws HibernateException {
    Session s = null;
    try {
      s = openSession();

//      Query q = s.createQuery("select grp.sc, grp.sub from ( " +
//
//      " select count(subject) as sub, subject from "
//          + AuditEvent.class.getName() + " GROUP BY subject) AS grp");

      Query q = s.createQuery("select count(ae.subject), ae.subject FROM "+ AE_CLASS + " AS ae "+
          "GROUP BY ae.subject ORDER BY count(ae.subject) desc");
      ScrollableResults sr = q.scroll();
      List<Object[]> l = new ArrayList<Object[]>();
      int i = 0;
      while(sr.next()) {
        System.out.println(i++);
        Object[] oa = sr.get();
        l.add(oa);
      }
      
      return l;
    } finally {
      close(s);
    }
  }

  @SuppressWarnings("unchecked")
  static public List<Object[]> findMaxObject() throws HibernateException {
    Session s = null;
    try {
      s = openSession();

      //Query q = s.createQuery("select count(ae.subject), ae.subject FROM "+ AE_CLASS + " AS ae "+
      //"GROUP BY ae.subject ORDER BY count(ae.subject) desc");
            
      Criteria crit = s.createCriteria(AuditEvent.class); 
      crit.setProjection(Projections.projectionList()
         .add(Projections.count("object").as("gcount"))
         .add(Projections.groupProperty("object"))
         );
      crit.addOrder(Order.desc("gcount"));
      crit.setMaxResults(10);
      return crit.list();
    } finally {
      close(s);
    }
  }
}

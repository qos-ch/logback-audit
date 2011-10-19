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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.audit.AuditEvent;

/**
 * Base functionality "class" for all Persistor classes - this class only
 * bundles static functions, has no members nor methods.
 * 
 * Persistor includes functions to create or retrieve a Hibernate
 * SessionFactory, and functions to open, close or rollback sessions.
 * 
 * @author Ceki Gülcü
 */
public class Persistor {

  // static Logger staticLogger = LoggerFactory.getLogger(Persistor.class);
  static SessionFactory sessionFactory;
  static Configuration cfg;
  static Object cfgLock;
  static Logger logger = LoggerFactory.getLogger(Persistor.class);

  /**
   * Create or re-obtain our single Hibernate SessionFactory.
   * 
   * @return An instantiated SessionFactory, always the same (the created
   *         instance is cached in this class).
   * @throws HibernateException
   */
  private static SessionFactory getSessionFactory() throws HibernateException {
    if (sessionFactory == null) {
      // Double-locked synchronization pattern
      synchronized (SessionFactory.class) {
        if (sessionFactory == null) {
          sessionFactory = createSessionFactory();
        }
      }
    }
    return sessionFactory;
  }

  static public Configuration createConfiguration() {
    final Configuration cfg = new Configuration();

    // Configure Hibernate mappings,this will look for the "hbm.xml" resources.
    // In alphabetical order:
    cfg.addClass(AuditEvent.class);

    return cfg;
  }

  public static void setConfiguration(Configuration cfg, Object lock) {
    // set the lock if not set previously
    if (Persistor.cfgLock == null && lock != null) {
      Persistor.cfgLock = lock;
    }
    if (Persistor.cfgLock == null || Persistor.cfgLock.equals(lock)) {
      Persistor.cfg = cfg;
    } else if (Persistor.cfg == null) {
      Persistor.cfg = cfg;
    } else {
      throw new IllegalStateException("Configuration was already set.");
    }
  }

  public static void resetConfiguration(Object lock) {
    if (Persistor.cfgLock == null || Persistor.cfgLock.equals(lock)) {
      Persistor.cfg = null;
      if (Persistor.sessionFactory != null) {
        try {
          Persistor.sessionFactory.close();
        } catch (HibernateException he) {
          logger.error("Warn failed to close session factory", he);
        }
      }
      Persistor.sessionFactory = null;

    }
  }

  static SessionFactory createSessionFactory() {
    if (cfg == null) {
      throw new IllegalStateException("Configuration must be set before usage.");
    }
    return cfg.buildSessionFactory();
  }

  /**
   * Open an Hibernate Session.
   * 
   * @return A valid Session instance, never null.
   * @throws HibernateException
   *                         In case the session could not be created.
   */
  protected static Session openSession() throws HibernateException {
    final SessionFactory sessionFactory = getSessionFactory();
    return sessionFactory.openSession();
  }

  protected static void update(Object o) throws HibernateException {
    Session s = null;
    Transaction tx = null;
    try {
      s = openSession();
      tx = s.beginTransaction();
      s.update(o);
      tx.commit();
    } catch (HibernateException he) {
      if (tx != null) {
        tx.rollback();
      }
      throw he;
    } finally {
      close(s, tx);
    }
  }

  public static void save(Object o) throws HibernateException {
    Session s = null;
    Transaction tx = null;
    try {
      s = openSession();
      tx = s.beginTransaction();
      s.save(o);
      tx.commit();
    } catch (HibernateException he) {
      logger.error("Failed to save object", he);
      if (tx != null) {
        tx.rollback();
      }
      throw he;
    } finally {
      close(s, tx);
    }
  }

  static private Logger getStaticLogger() {
    return LoggerFactory.getLogger(Persistor.class);
  }

  public static void delete(Object o) throws HibernateException {
    Session s = null;
    Transaction tx = null;
    try {
      s = openSession();
      tx = s.beginTransaction();
      s.delete(o);
      tx.commit();
    } catch (HibernateException he) {
      if (tx != null) {
        tx.rollback();
      }
      throw he;
    } finally {
      close(s, tx);
    }
  }

  /**
   * Close a Hibernate Session.
   * 
   * @param theHibernateSession
   *                        The open session to close.
   * 
   * In case of a problem, this method will log an error but won't propagate any
   * exception - therefore it may be used safely in a finally{} block.
   * 
   */
  protected static void close(Session theHibernateSession) {
    try {
      if (theHibernateSession != null) {
        theHibernateSession.close();
      }
    } catch (Exception e) {
      getStaticLogger().error("Failed to close session " + theHibernateSession,
          e);
    }
  }

  /**
   * Rollback a Hibernate transaction and close the session.
   * 
   * In case of a problem, this method will log an error but won't propagate any
   * exception - therefore it may be used safely in a finally{} block.
   * 
   * @param theHibernateSession
   *                        The open session to rollback and close.
   * @param tx
   */
  protected static void close(Session theHibernateSession, Transaction tx) {
    try {
      if ((tx != null) && (tx.isActive() && !tx.wasCommitted())) {
        tx.rollback();
      }
    } catch (HibernateException he) {
      getStaticLogger().error("Could not rollback transaction" + tx);
    }

    try {
      if (theHibernateSession != null) {
        theHibernateSession.close();
      }
    } catch (Exception e) {
      getStaticLogger().error("Failed to close session " + theHibernateSession,
          e);
    }
  }
}

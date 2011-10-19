package ch.qos.logback.audit.server;

import java.util.Properties;

import org.hibernate.cfg.Configuration;

import ch.qos.logback.audit.persistent.NullAEShaper;
import ch.qos.logback.audit.persistent.Persistor;
import ch.qos.logback.audit.server.helper.ResourceUtil;

public class Main {

  /**
   * @param args
   * @throws Exception 
   */
  public static void main(String[] args) throws Exception {

    Configuration cfg = Persistor.createConfiguration();

    Properties props = ResourceUtil.getProps("@PROJECT_NAME@/hibernate.properties");

    cfg.setProperties(props);

    Persistor.setConfiguration(cfg, new Object());

    AuditServer auditServer = new AuditServer(
        AuditServerConstants.DEFAULT_PORT, new AuditEventPersistor(new NullAEShaper()));
    auditServer.run();
  }

}

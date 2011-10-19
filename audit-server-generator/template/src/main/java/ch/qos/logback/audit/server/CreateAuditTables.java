package ch.qos.logback.audit.server;


import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.hibernate.cfg.Configuration;

import ch.qos.logback.audit.persistent.Persistor;
import ch.qos.logback.audit.server.helper.ResourceUtil;
import ch.qos.logback.audit.server.TableCreator;

public class CreateAuditTables {

  
  public static void main(String[] args) throws IOException {
    Configuration cfg = Persistor.createConfiguration();

    Properties props = ResourceUtil.getProps("@PROJECT_NAME@/hibernate.properties");
    
    cfg.setProperties(props);
    
    TableCreator tc = new TableCreator(cfg);
    tc.createTables("audit_tables.sql-"
        + AuditServerConstants.ISODATE_SDF.format(new Date()));
  }
  

}

package com.kloudless;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Access test properties file
 * TODO: Add logger
 *
 * Created by gchiou on 08/06/2017.
 */
public final class PropertiesHelper {

  private final static String PROP_FILE_NAME = "configuration.properties";
  private volatile Properties props = null;
  private final static Object lck = new Object();
  private static PropertiesHelper instance;

  private PropertiesHelper() {
  }

  public static PropertiesHelper getInst() {
    if(instance == null) {
      synchronized (lck) {
        if(instance == null) {
          try {
            instance = new PropertiesHelper();
            instance.bindProperties(PROP_FILE_NAME);
          } catch (IOException e) {
            //TODO: add a logger
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  private void bindProperties(String propertyFilename)
      throws IOException {
    if(props == null) {
      InputStream ins = ClassLoader.getSystemResourceAsStream(propertyFilename);
      if(ins == null) throw new IOException(propertyFilename + " is found!");
      try {
        props = new Properties();
        props.load(ins);
      } catch(IOException ex){
        throw ex;
      } finally {
        if(ins != null) {
          ins.close();
        }
      }
    }
  }

  public Properties getProperties() {
    return props;
  }

  public String getProperty(String key) {
    return getProperty(key,"");
  }

  public String getProperty(String key, String defaultValue) {
    String val = null;
    val = getProperties().getProperty(key,defaultValue);
    return val;
  }
}

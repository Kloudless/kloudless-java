package com.kloudless;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Access test properties file
 * TODO: Add logger
 * <p>
 * Created by gchiou on 08/06/2017.
 */
public final class PropertiesHelper {
	
	private static final String PROP_FILE_NAME = "kloudless.configuration.properties";
	private static final Object LOCK = new Object();
	private static PropertiesHelper instance;
	private volatile Properties props = null;
	
	private PropertiesHelper() {
	}
	
	public static PropertiesHelper getInst() {
		if (instance == null) {
			synchronized (LOCK) {
				if (instance == null) {
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
		if (props == null) {
			InputStream ins = ClassLoader.getSystemResourceAsStream(propertyFilename);
			if (ins == null) throw new IOException(propertyFilename + " is found!");
			try {
				props = new Properties();
				props.load(ins);
			} catch (IOException ex) {
				throw ex;
			} finally {
				if (ins != null) {
					ins.close();
				}
			}
		}
	}
	
	public Properties getProperties() {
		return props;
	}
	
	public String getProperty(String key) {
		return getProperty(key, "");
	}
	
	public String getProperty(String key, String defaultValue) {
		String val;
		val = getProperties().getProperty(key, defaultValue);
		return val;
	}
}

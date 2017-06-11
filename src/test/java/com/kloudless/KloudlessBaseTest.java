package com.kloudless;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;

/**
 * Created by gchiou on 09/06/2017.
 */
public abstract class KloudlessBaseTest {

  protected static ObjectMapper mapper;
  private static Object lck = new Object();

  @BeforeClass
  public static void baseSetup() {
    synchronized (lck) {
      if(mapper == null) {
        mapper = new ObjectMapper();
      }
    }
  }
}

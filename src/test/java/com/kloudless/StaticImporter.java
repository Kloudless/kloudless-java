package com.kloudless;

/**
 * Created by gchiou on 09/06/2017.
 */
public final class StaticImporter {

  // be mindful the order here to initialize PropertyiesHelper before TestInfoHelper
  public static PropertiesHelper Props = PropertiesHelper.getInst();
  public static TestInfoHelper TestInfo = TestInfoHelper.getInst();

  private StaticImporter() {}
}

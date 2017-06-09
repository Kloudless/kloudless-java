package com.kloudless;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kloudless.StaticImporter.Props;

/**
 * Created by gchiou on 08/06/2017.
 *
 * TestInfoHelper collects required information for unit test cases
 */
public final class TestInfoHelper {

  private final static String API_KEY_KEY = "api_key";
  private final static String BEARER_TOKEN_KEY = "bearer_token";
  private final static String API_KEY_ARG = "apiKey";
  private final static String BEARER_TOKEN_ARG = "bearerToken";
  private final static String API_KEY_ENV = "API_KEY";
  private final static String BEARER_TOKEN_ENV = "BEARER_TOKEN";

  private final static String TEST_ACCOUNTS_KEY = "test_accounts";

  private String apiKey = "";
  private String beaerToken = "";

  private final static Object lck = new Object();

  private static TestInfoHelper instance = null;

  public String getApiKey() {
    return apiKey;
  }

  public String getBearerToken() {
    return beaerToken;
  }

  public List<String> getTestAccounts(){
    String[] accounts = Props.getProperty(TEST_ACCOUNTS_KEY).split(";");
    if(accounts.length == 0) return new ArrayList<>(0);
    List<String> accounts2 =  Arrays.stream(accounts).filter(x -> x.length() > 0)
        .map(String::trim).collect(Collectors.toList());
    return accounts2;
  }

  private TestInfoHelper() {}

  public static TestInfoHelper getInst() {
    if(instance == null) {
      synchronized (lck) {
        if(instance == null) {
          instance = new TestInfoHelper();
          instance.bindKey();
        }
      }
    }
    return instance;
  }

  /**
   * key binding order is environment variable, command argument and property file
   */
  private void bindKey() {
    //get api key from env
    Optional<String> opt = Optional.ofNullable(System.getenv(API_KEY_ENV));

    if(opt.isPresent() && opt.get().length() > 0) {
      apiKey = opt.get().trim();
    } else {
      opt = Optional.ofNullable(System.getProperty(API_KEY_ARG));
      if (opt.isPresent() && opt.get().length() > 0) {
        apiKey = opt.get().trim();
      } else {
        apiKey = Props.getProperty(API_KEY_KEY).trim();
      }
    }

    opt = Optional.ofNullable(System.getenv(BEARER_TOKEN_ENV));
    if(opt.isPresent() && opt.get().length() > 0) {
      beaerToken = opt.get().trim();
    } else {
      opt = Optional.ofNullable(System.getProperty(BEARER_TOKEN_ARG));
      if(opt.isPresent() && opt.get().length() > 0) {
        beaerToken = opt.get().trim();
      } else {
        beaerToken = Props.getProperty(BEARER_TOKEN_KEY).trim();
      }
    }

    if(apiKey.length() == 0 && beaerToken.length() == 0) {
      throw new IllegalArgumentException("api key or bearer token is not found!");
    }
  }
}

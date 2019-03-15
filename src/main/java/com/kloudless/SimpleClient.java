package com.kloudless;

import java.util.HashMap;
import java.util.Map;

/**
 * SimpleClient is a pure http client without any token infomation
 */
public class SimpleClient extends BaseHttpClient {

    public SimpleClient() {

    }

    /**
     * Will return empty string, the url will not include account id
     * 
     * @return String of empty
     */
    @Override
    public String getUrlPrefix() {
        return "";
    }

    /**
     * Will return empty Map beacuse Simple Client doesn't support this
     * 
     * @return Map of empty
     */
    @Override
    public Map<String, Object> getDefaultHeaders() {
        return new HashMap<String, Object>();
    }

    /**
     * Will return empty Map beacuse Simple Client doesn't support this
     * 
     * @return Map of empty
     */
    @Override
    public Map<String, Object> getDefaultQueryParameters() {
        return new HashMap<String, Object>();
    }
}

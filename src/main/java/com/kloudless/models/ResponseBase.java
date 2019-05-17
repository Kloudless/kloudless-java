package com.kloudless.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import com.kloudless.BaseHttpClient;

/**
 * Base class of Response data
 */
public class ResponseBase extends BaseHttpClient {
    protected String url;
    protected Map<String, Object> defaultHeaders;
    protected Map<String, Object> defaultQueryParameters = new HashMap<String, Object>();

    public ResponseBase() {

    }

    /**
     * Get the prefix of URL
     * 
     * @return String url prefix
     */
    @Override
    public String getUrlPrefix() {
        return this.url;
    }

    /**
     * Get default http headers
     * 
     * @return Map key, value of http headers
     */
    @Override
    public Map<String, Object> getDefaultHeaders() {
        return this.defaultHeaders;
    }

    /**
     * Get default query parameters
     * 
     * @return Map, key, value of http query string
     */
    @Override
    public Map<String, Object> getDefaultQueryParameters() {
        return this.defaultQueryParameters;
    }

    /**
     * Get URL
     * 
     * @return String of URL of resource
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Will remove query string from the url string and store them to defaultQueryParameters
     * 
     * @param url url of http request
     * @return String url without any query string
     */
    protected String storeAndRemoveQueryString(String url) {
        if (url.indexOf("?") != -1) {
            storeQueryParameters(url);
            return getRidOfQueryString(url);
        }
        return url;
    }

    /**
     * To remove query string from url string
     * 
     * @param url url the original url string
     * @return String url without any query string
     */
    private String getRidOfQueryString(String url) {
        String[] urls = url.split("\\?");
        return urls[0];
    }

    /**
     * Store the query string to class variable named defaultQueryParameters
     * 
     * @param url url the original url string
     */
    private void storeQueryParameters(String url) {
        String[] urls = url.split("\\?");
        String[] queryString = urls[1].split("&");
        this.defaultQueryParameters = Arrays.asList(queryString).stream()
                .map(param -> param.split("=", 2)).collect(Collectors.toMap(a -> a[0], a -> a[1]));
    }
}

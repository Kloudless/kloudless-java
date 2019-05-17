package com.kloudless;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.kloudless.exceptions.InvalidArgumentException;

/**
 * Client object is a useful tool to handle any http interaction between client and Kloudless API
 * Server.
 */
public class Client extends BaseHttpClient {
    private String apiKey;
    private String token;
    protected int apiVersion;
    protected String urlPrefix;
    protected Map<String, Object> defaultHeaders;


    /**
     * Constructor of Client, either token or apiKey could initialize a client
     * 
     * @param apiKey  Use apiKey to access APIs of Kloudless Server
     * @param headers Could add additional header in request.
     * @throws InvalidArgumentException Error when both token and apiKey are not existed.
     */
    public Client(String apiKey, Map<String, Object> headers) throws InvalidArgumentException {
        this(apiKey, "", headers, Application.getDefaultApiVersion());
    }

    /**
     * Constructor of Client, either token or apiKey could initialize a client
     * 
     * @param apiKey     Use apiKey to access APIs of Kloudless Server
     * @param headers    Could add additional header in request.
     * @param apiVersion Set the apiVersion, default is 1
     * @throws InvalidArgumentException Error when both token and apiKey are not existed.
     */
    public Client(String apiKey, Map<String, Object> headers, int apiVersion)
            throws InvalidArgumentException {
        this(apiKey, "", headers, apiVersion);
    }

    /**
     * Constructor of Client, either token or apiKey could initialize a client
     * 
     * @param apiKey     Use apiKey to access APIs of Kloudless Server
     * @param token      User bearer token to access APIs of Kloudless server
     * @param headers    Could add additional header in request.
     * @param apiVersion Set the apiVersion, default is 1
     * @throws InvalidArgumentException Error when both token and apiKey are not existed.
     */
    protected Client(String apiKey, String token, Map<String, Object> headers, int apiVersion)
            throws InvalidArgumentException {
        this.apiKey = apiKey;
        this.token = token;
        this.defaultHeaders = Optional.ofNullable(headers).orElse(new HashMap<String, Object>());
        this.apiVersion = apiVersion;

        if (this.token != null && !this.token.isEmpty()) {
            this.defaultHeaders.put("Authorization", "Bearer " + this.token);
        } else if (this.apiKey != null && !this.apiKey.isEmpty()) {
            this.defaultHeaders.put("Authorization", "APIKey " + this.apiKey);
        } else {
            throw new InvalidArgumentException(
                    "Initialize Client failure, neither token nor apiKey.", null);
        }

        this.urlPrefix = String.format("/v%s/", this.apiVersion);
    }

    /**
     * Get the prefix of URL
     * 
     * @return String url prefix
     */
    @Override
    public String getUrlPrefix() {
        return this.urlPrefix;
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
        return new HashMap<String, Object>();
    }

    /**
     * Get what apiVersion current Client uses
     * 
     * @return int apiVersion
     */
    public int getApiVersion() {
        return this.apiVersion;
    }

    /**
     * Set apiVersion of current Client uses
     * 
     * @param apiVersion int apiVersion for this Client
     */
    public void setApiVersion(int apiVersion) {
        this.apiVersion = apiVersion;
        this.urlPrefix = String.format("/v%s/", this.apiVersion);
    }

}

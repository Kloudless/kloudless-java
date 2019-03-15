package com.kloudless;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.kloudless.exceptions.ApiException;
import com.kloudless.exceptions.InvalidArgumentException;
import org.apache.http.HttpResponse;

/**
 * Account is the class based on the Client to keep bearer token and maintain user id.
 */
public class Account extends Client {
    private String id;

    /**
     * Constructor of Account
     * 
     * @param token Bearer token initialize the account object
     * @throws InvalidArgumentException Error when both token and apiKey are not existed.
     */
    public Account(String token) throws InvalidArgumentException {
        this("me", token, "", new HashMap<String, Object>(), Application.getDefaultApiVersion());
    }

    /**
     * Constructor of Account
     * 
     * @param token      Bearer token initialize the account object
     * @param apiVersion specify apiVersion, default is 1
     * @throws InvalidArgumentException Error when both token and apiKey are not existed.
     */
    public Account(String token, int apiVersion) throws InvalidArgumentException {
        this("me", token, "", new HashMap<String, Object>(), apiVersion);
    }

    /**
     * Constructor of Account
     * 
     * @param token   Bearer token initialize the account object
     * @param headers Could add additional header in request.
     * @throws InvalidArgumentException Error when both token and apiKey are not existed.
     */
    public Account(String token, Map<String, Object> headers) throws InvalidArgumentException {
        this("me", token, "", headers, Application.getDefaultApiVersion());
    }

    /**
     * Constructor of Account
     * 
     * @param token      Bearer token initialize the account object
     * @param headers    Could add additional header in request.
     * @param apiVersion specify apiVersion, default is 1
     * @throws InvalidArgumentException Error when both token and apiKey are not existed.
     */
    public Account(String token, Map<String, Object> headers, int apiVersion)
            throws InvalidArgumentException {
        this("me", token, "", headers, apiVersion);
    }

    /**
     * Constructor of Account
     * 
     * @param accountId account id
     * @param apiKey    Api key of your application
     * @throws InvalidArgumentException Error when both token and apiKey are not existed.
     */
    public Account(String accountId, String apiKey) throws InvalidArgumentException {
        this(accountId, "", apiKey, new HashMap<String, Object>(),
                Application.getDefaultApiVersion());
    }

    /**
     * Constructor of Account
     * 
     * @param accountId  account id
     * @param apiKey     Api key of your application
     * @param apiVersion specify apiVersion, default is 1
     * @throws InvalidArgumentException Error when both token and apiKey are not existed.
     */
    public Account(String accountId, String apiKey, int apiVersion)
            throws InvalidArgumentException {
        this(accountId, "", apiKey, new HashMap<String, Object>(), apiVersion);
    }

    /**
     * Constructor of Account
     * 
     * @param accountId account id
     * @param apiKey    Api key of your application
     * @param headers   Could add additional header in request.
     * @throws InvalidArgumentException Error when both token and apiKey are not existed.
     */
    public Account(String accountId, String apiKey, Map<String, Object> headers)
            throws InvalidArgumentException {
        this(accountId, "", apiKey, headers, Application.getDefaultApiVersion());
    }

    /**
     * Constructor of Account
     * 
     * @param accountId  account id
     * @param apiKey     Api key of your application
     * @param headers    Could add additional header in request.
     * @param apiVersion specify apiVersion, default is 1
     * @throws InvalidArgumentException Error when both token and apiKey are not existed.
     */
    public Account(String accountId, String apiKey, Map<String, Object> headers, int apiVersion)
            throws InvalidArgumentException {
        this(accountId, "", apiKey, headers, apiVersion);
    }

    /**
     * Constructor of Account
     * 
     * @param accountId  account id
     * @param token      Bearer token initialize the account object
     * @param apiKey     Api key of your application
     * @param headers    Could add additional header in request.
     * @param apiVersion specify apiVersion, default is 1
     * @throws InvalidArgumentException Error when both token and apiKey are not existed.
     */
    private Account(String accountId, String token, String apiKey, Map<String, Object> headers,
            int apiVersion) throws InvalidArgumentException {
        super(apiKey, token, headers, apiVersion);
        this.urlPrefix = super.getUrlPrefix() + "accounts/" + accountId;
        this.id = accountId;
    }

    /**
     * Get account id
     * 
     * @return String Account Id
     */
    public String getId() {
        return this.id;
    }

    /**
     * This method support pass through function. API server will return original reponse from
     * upstream server
     *
     * @param httpMethod Should be GET, POST, PUT, PATCH, and DELETE
     * @param url        url should be up stream service name, and could limit
     * @param headers    Additional header parameters in this request.
     * @param content    Any information for post, put and patch.
     * @return HttpResponse
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public HttpResponse raw(String httpMethod, String url, Map<String, Object> headers,
            Map<String, Object> content) throws ApiException {

        httpMethod = httpMethod.toUpperCase();
        headers = Optional.ofNullable(headers).orElse(new HashMap<String, Object>());
        headers.put("X-Kloudless-Raw-URI", url);
        headers.put("X-Kloudless-Raw-Method", httpMethod);
        url = assembleUrl("/raw");
        headers = mergeHeaders(headers);
        return this.rawJsonExecute("POST", url, headers, content);
    }

    /**
     * Set apiVersion of current Client uses
     * 
     * @param apiVersion int apiVersion for this Client
     */
    public void setApiVersion(int apiVersion) {
        this.apiVersion = apiVersion;
        this.urlPrefix = String.format("/v%s/", this.apiVersion) + "accounts/" + this.id;
    }

}

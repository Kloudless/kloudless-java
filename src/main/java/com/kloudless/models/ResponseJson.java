package com.kloudless.models;

import java.net.URISyntaxException;
import java.util.Map;
import com.google.gson.JsonObject;
import com.kloudless.exceptions.ApiException;

/**
 * ResponseJson Class is extends from ResponseBase, it contains JsonObject which is the result from
 * API Server, this class store JsonObject but without id
 */
public class ResponseJson extends ResponseBase {

    private JsonObject data;

    /**
     * Constructor of Resource
     * 
     * @param data           JsonObject is from the Kloudless API Server.
     * @param url            The url of this response, could apply when user invoke refresh method.
     * @param defaultHeaders Store the original headers for further http operations
     * @throws URISyntaxException Error when the format of url is invalid
     */
    public ResponseJson(JsonObject data, String url, Map<String, Object> defaultHeaders)
            throws URISyntaxException {
        this.url = storeAndRemoveQueryString(url);
        this.data = data;
        this.defaultHeaders = defaultHeaders;
    }

    /**
     * Get JsonObject Content
     * 
     * @return JsonObject
     */
    public JsonObject getData() {
        return this.data;
    }

    /**
     * To refrsh this resource will trigger a new http get and update the data attribute in
     * resource.
     * 
     * @throws ApiException Error when any error from Kloudless API server.
     */
    public void refresh() throws ApiException {
        ResponseJson newResponse = (ResponseJson) super.get("");
        this.data = newResponse.getData();
    }
}

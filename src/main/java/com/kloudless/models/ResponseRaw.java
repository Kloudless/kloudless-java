package com.kloudless.models;

import java.util.Map;
import org.apache.http.HttpResponse;

/**
 * ResourceRaw Class is extends from ResponseBase, it contains pure httpResponse which is the result
 * of any API operation from Kloudless API server. This class is for file downlaod or other types of
 * data other than JsonObject.
 */
public class ResponseRaw extends ResponseBase {

    private HttpResponse data;

    /**
     * Constructor of Resource
     * 
     * @param response       Original httpResponse is from the Kloudless API Server.
     * @param url            The url of this resource, could apply when user invoke refresh method.
     * @param defaultHeaders Store the original headers for further http operations
     */
    public ResponseRaw(HttpResponse response, String url, Map<String, Object> defaultHeaders) {
        this.data = response;
        this.url = url;
        this.defaultHeaders = defaultHeaders;
    }

    /**
     * Get resource data
     * 
     * @return HttpResponse
     */
    public HttpResponse getData() {
        return this.data;
    }

}

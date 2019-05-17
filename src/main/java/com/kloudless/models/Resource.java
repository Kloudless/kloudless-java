package com.kloudless.models;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kloudless.exceptions.ApiException;

/**
 * Resource Class is extends from ResponseBase, it contains JsonObject which is the result of any
 * API operation from Kloudless API server
 */
public class Resource extends ResponseBase {

    private JsonObject data;
    private String id;

    /**
     * Constructor of Resource
     * 
     * @param data           JsonObject is from the Kloudless API Server.
     * @param url            The url of this resource, could apply when user invoke refresh method.
     * @param defaultHeaders Store the original headers for further http operations
     * @throws URISyntaxException Error when the format of url is invalid
     */
    public Resource(JsonObject data, String url, Map<String, Object> defaultHeaders)
            throws URISyntaxException {
        this.url = storeAndRemoveQueryString(url);
        this.data = data;
        this.id = retrieveId(data);
        this.defaultHeaders = defaultHeaders;
    }

    /**
     * Get Object Id from the JsonObject
     * 
     * @param JsonObject the JsonObject from API server
     * @return String Id of Object
     */
    private String retrieveId(JsonObject data) {
        Optional<JsonElement> id = Optional.ofNullable(data.get("id"));
        return (id.isPresent() ? id.get().getAsString() : "");
    }

    /**
     * Get resource data
     * 
     * @return JsonObject
     */
    public JsonObject getData() {
        return this.data;
    }

    /**
     * Get Resouece Id
     * 
     * @return String id of resource
     */
    public String getId() {
        return this.id;
    }

    /**
     * To refrsh this resource will trigger a new http get and update the data attribute in
     * resource.
     * 
     * @throws ApiException Error when any error from Kloudless API server.
     */
    public void refresh() throws ApiException {
        Resource newResource = (Resource) super.get("");
        this.data = newResource.getData();
    }
}

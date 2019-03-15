package com.kloudless;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.kloudless.exceptions.ApiException;
import com.kloudless.models.Resource;
import com.kloudless.models.ResourceList;
import com.kloudless.models.ResponseBase;
import com.kloudless.models.ResponseJson;
import com.kloudless.models.ResponseRaw;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Client object is a useful tool to handle any http interaction between client and Kloudless API
 * Server.
 */
public abstract class BaseHttpClient {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Get the prefix of URL
     * 
     * @return String url prefix
     */
    abstract public String getUrlPrefix();

    /**
     * Get default http headers
     * 
     * @return Map, key, value pair of arguments
     */
    abstract public Map<String, Object> getDefaultHeaders();


    /**
     * Get default http query string
     * 
     * @return Map, key, value pair of query parameters
     */
    abstract public Map<String, Object> getDefaultQueryParameters();

    /**
     * Http GET method
     * 
     * @param url URL of API supported by Kloudless API server.
     * @return ResponseBase Class and list of data or JsonObject is inside the data attribute.
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public ResponseBase get(String url) throws ApiException {
        return get(url, null);
    }

    /**
     * Http GET method
     * 
     * @param url     URL of API supported by Kloudless API server.
     * @param headers Addition header parameter in this request.
     * @return ResponseBase Class and list of data or JsonObject is inside the data attribute.
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public ResponseBase get(String url, Map<String, Object> headers) throws ApiException {
        return runHttpTransaction("GET", url, "Json", headers, null);
    }

    /**
     * Http POST method
     * 
     * @param url     URL of API supported by Kloudless API server.
     * @param content The post data will be in the request boby
     * @return ResponseBase Class and list of data or JsonObject is inside the data attribute.
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public ResponseBase post(String url, Map<String, Object> content) throws ApiException {
        return post(url, null, content);
    }

    /**
     * Http POST method
     * 
     * @param url     URL of API supported by Kloudless API server.
     * @param headers Addition header parameter in this request.
     * @param content The post data wiil be in the request body
     * @return ResponseBase Class and list of data or JsonObject is inside the data attribute.
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public ResponseBase post(String url, Map<String, Object> headers, Map<String, Object> content)
            throws ApiException {
        return runHttpTransaction("POST", url, "Json", headers, content);
    }


    /**
     * Http POST method
     * 
     * @param url        URL of API supported by Kloudless API server.
     * @param headers    Addition header parameter in this request.
     * @param uploadFile File object will be posted to the server.
     * @return ResponseBase Class and list of data or JsonObject is inside the data attribute.
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public ResponseBase post(String url, Map<String, Object> headers, File uploadFile)
            throws ApiException {
        return runHttpTransaction("POST", url, "File", headers, uploadFile);
    }

    /**
     * Http PATCH method
     * 
     * @param url     URL of API supported by Kloudless API server.
     * @param content The patch data will be in the request boby
     * @return ResponseBase Class and list of data or JsonObject is inside the data attribute.
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public ResponseBase patch(String url, Map<String, Object> content) throws ApiException {
        return patch(url, null, content);
    }

    /**
     * Http PATCH method
     * 
     * @param url     URL of API supported by Kloudless API server.
     * @param headers Addition header parameter in this request.
     * @param content The patch data wiil be in the request body
     * @return ResponseBase Class and list of data or JsonObject is inside the data attribute.
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public ResponseBase patch(String url, Map<String, Object> headers, Map<String, Object> content)
            throws ApiException {
        return runHttpTransaction("PATCH", url, "Json", headers, content);
    }

    /**
     * Http PATCH method
     * 
     * @param url        URL of API supported by Kloudless API server.
     * @param headers    Addition header parameter in this request.
     * @param uploadFile File object will be patch to the server.
     * @return ResponseBase Class and list of data or JsonObject is inside the data attribute.
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public ResponseBase patch(String url, Map<String, Object> headers, File uploadFile)
            throws ApiException {
        return runHttpTransaction("PATCH", url, "File", headers, uploadFile);
    }

    /**
     * Http PUT method
     * 
     * @param url     URL of API supported by Kloudless API server.
     * @param content The put data will be in the request boby
     * @return ResponseBase Class and list of data or JsonObject is inside the data attribute.
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public ResponseBase put(String url, Map<String, Object> content) throws ApiException {
        return put(url, null, content);
    }

    /**
     * Http PUT method
     * 
     * @param url     URL of API supported by Kloudless API server.
     * @param headers Addition header parameter in this request.
     * @param content The put data wiil be in the request body
     * @return ResponseBase Class and list of data or JsonObject is inside the data attribute.
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public ResponseBase put(String url, Map<String, Object> headers, Map<String, Object> content)
            throws ApiException {
        return runHttpTransaction("PUT", url, "Json", headers, content);
    }

    /**
     * Http PUT method
     * 
     * @param url        URL of API supported by Kloudless API server.
     * @param headers    Addition header parameter in this request.
     * @param uploadFile File object will be put to the server.
     * @return ResponseBase Class and list of data or JsonObject is inside the data attribute.
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public ResponseBase put(String url, Map<String, Object> headers, File uploadFile)
            throws ApiException {
        return runHttpTransaction("PUT", url, "File", headers, uploadFile);
    }

    /**
     * Http DELETE method
     * 
     * @param url URL of API supported by Kloudless API server.
     * @return ResponseBase object but might be empty content
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public ResponseBase delete(String url) throws ApiException {
        return delete(url, null);
    }

    /**
     * Http DELETE method
     * 
     * @param url     URL of API supported by Kloudless API server.
     * @param headers Addition header parameter in this request.
     * @return ResponseBase object but might be empty content
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public ResponseBase delete(String url, Map<String, Object> headers) throws ApiException {
        return runHttpTransaction("DELETE", url, "Json", headers, null);
    }

    /**
     * This method find the proper rawExecute method and dispatch
     * 
     * @param method  the Http method
     * @param url     the url string
     * @param format  String should be Json, File or Form
     * @param headers the key, value pairs of http headers
     * @param content the key, value pairs of http body
     * @return ResponseBase The object extends from ResponseBase
     * @throws ApiException Error if call API server failed.
     */
    private ResponseBase runHttpTransaction(String method, String url, String format,
            Map<String, Object> headers, Object content) throws ApiException {
        url = assembleUrl(url);
        headers = mergeHeaders(headers);
        format = format.toUpperCase();
        switch (format) {
            case "JSON":
                return responseFactory(rawJsonExecute(method, url, headers, content), url, headers,
                        method);
            case "FORM":
                return responseFactory(rawFormExecute(method, url, headers, content), url, headers,
                        method);
            case "FILE":
                return responseFactory(rawFileExecute(method, url, headers, content), url, headers,
                        method);
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + format);
        }

    }

    /**
     * The method of execute the http request and supports form data content
     * 
     * @param method  the Http method
     * @param url     the url string
     * @param headers the key, value pairs of http headers
     * @param content the key, value pairs of http body
     * @return HttpResponse the httpResponse object
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    protected HttpResponse rawFormExecute(String method, String url, Map<String, Object> headers,
            Object content) throws ApiException {

        try {
            content = Optional.ofNullable(content).orElse(new HashMap<String, Object>());
            List<NameValuePair> requestContent = new ArrayList<NameValuePair>();
            for (Entry entry : ((Map<String, Object>) content).entrySet()) {
                requestContent.add(
                        new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
            }
            HttpEntity entity = new UrlEncodedFormEntity(requestContent);
            return handleResponse(assembleHttpRequest(method, url, entity, headers,
                    ContentType.APPLICATION_FORM_URLENCODED.getMimeType()));
        } catch (ApiException | ParseException | IOException e) {
            throw new ApiException(
                    "Run http " + method + " to " + url + " failed, " + e.getMessage(), e);
        }
    }

    /**
     * The method of execute the http request and supports json data content
     * 
     * @param method  the Http method
     * @param url     the url string
     * @param headers the key, value pairs of http headers
     * @param content the key, value pairs of http body
     * @return HttpResponse the httpResponse object
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    protected HttpResponse rawJsonExecute(String method, String url, Map<String, Object> headers,
            Object content) throws ApiException {
        try {
            content = (Map<String, Object>) Optional.ofNullable(content)
                    .orElse(new HashMap<String, Object>());
            HttpEntity entity = new StringEntity(gson.toJson(content).toString());
            return handleResponse(assembleHttpRequest(method, url, entity, headers,
                    ContentType.APPLICATION_JSON.getMimeType()));
        } catch (ApiException | ParseException | IOException e) {
            throw new ApiException(
                    "Run http " + method + " to " + url + " failed, " + e.getMessage(), e);
        }
    }

    /**
     * The method of execute the http request and supports file uploading
     * 
     * @param method     the Http method
     * @param url        the url string
     * @param headers    the key, value pairs of http headers
     * @param uploadFile the file object to upload
     * @return HttpResponse the httpResponse object
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    protected HttpResponse rawFileExecute(String method, String url, Map<String, Object> headers,
            Object uploadFile) throws ApiException {
        try {
            HttpEntity entity = EntityBuilder.create().setFile((File) uploadFile).build();
            return handleResponse(assembleHttpRequest(method, url, entity, headers,
                    ContentType.APPLICATION_OCTET_STREAM.getMimeType()));
        } catch (ApiException | ParseException | IOException e) {
            throw new ApiException(
                    "Run http " + method + " to " + url + " failed, " + e.getMessage(), e);
        }
    }

    /**
     * This method is assemble HttpReques object
     * 
     * @param method      the Http method
     * @param url         the url string
     * @param entity      the object of HttpEntity
     * @param headers     the key, value pairs of http headers
     * @param contentType the contentType of this http rquest header
     * @return HttpRequestBase The HttpRequest object
     * @throws UnsupportedEncodingException Error when the unsupport method is inputed
     */
    private static HttpRequestBase assembleHttpRequest(String method, String url, HttpEntity entity,
            Map<String, Object> headers, String contentType) throws UnsupportedEncodingException {
        HttpRequestBase request = requestFactory(method, Application.getBaseUrl() + url, entity);
        request = assembleHeaders(request, headers, contentType);
        return request;
    }

    /**
     * The method of receving the httpResponse and check the status of httpResponse
     * 
     * @param request A HttpRequest object which has populated all essential data
     * @return HttpResponse the HttpResponse object
     * @throws ApiException            An error if any data other than 2xx from Kloudless API server
     *                                 or upstream services.
     * @throws ClientProtocolException Error when http call failed
     * @throws IOException             Error when http call failed
     * @throws ParseException          Error when parse the JsonObject failed
     */
    private static HttpResponse handleResponse(HttpRequestBase request)
            throws ClientProtocolException, IOException, ApiException, ParseException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() < 200
                || response.getStatusLine().getStatusCode() >= 300) {
            throw new ApiException("Get error response from API server, status code:"
                    + response.getStatusLine().getStatusCode() + ", and its message"
                    + EntityUtils.toString(response.getEntity()), null);
        }
        return response;
    }

    /**
     * This method merge header from API call and default Headers in the Client object and
     * attributes of default headers will be overwrite if duplicated
     * 
     * @param headers the key value in headers from API call.
     * @return mergedHeaders the final key value of headers
     */
    protected Map<String, Object> mergeHeaders(Map<String, Object> headers) {
        headers = Optional.ofNullable(headers).orElse(new HashMap<String, Object>());
        Map<String, Object> mergedMap = new HashMap<String, Object>();

        mergedMap.putAll(this.getDefaultHeaders());
        // overwrited by additional headers
        mergedMap.putAll(headers);

        return mergedMap;
    }

    /**
     * Add headers from key value map to request object
     * 
     * @param request     the HttpRequest object
     * @param headers     the key value pair of headers
     * @param contentType the content type in the headers
     * @return HttpRequest object
     */
    private static HttpRequestBase assembleHeaders(HttpRequestBase request,
            Map<String, Object> headers, String contentType) {
        for (Entry entry : headers.entrySet()) {
            request.addHeader((String) entry.getKey(), (String) entry.getValue());
        }
        request.addHeader("User-Agent", "kloudless-java/" + Application.getSDKVersion());
        request.addHeader("Content-Type", contentType);
        return request;
    }

    /**
     * Assemble the whole Url for http request
     * 
     * @param url relative url path from parameters of API call
     * @return String URL with baseUrl , prefix , paths and query string.
     */
    protected String assembleUrl(String url) {
        return this.getUrlPrefix() + appendDefaultQueryParameters(url);
    }

    /**
     * This method is merge query parameters from API call and existed query parameters which might
     * from Client object or generated and populated to Resource object.
     * 
     * @param url relative url path from parameters of API call
     * @return String the handled query string
     */
    private String appendDefaultQueryParameters(String url) {
        if (url.indexOf("?") != -1) {
            String[] urls = url.split("\\?");
            url = urls[0] + "?" + toQueryString(mergeDeaultQueryParameters(getQueryParams(url)));
        } else {
            String queryString =
                    toQueryString(mergeDeaultQueryParameters(new HashMap<String, String>()));
            if (!queryString.isEmpty()) {
                url = url + "?" + queryString;
            }
        }

        return url;
    }

    /**
     * Parse the query string to key, value pair of query parameters
     * 
     * @param url relative url path from parameters of API call
     * @return map the key, value pairs of query parameters
     */
    private Map<String, String> getQueryParams(String url) {
        String[] urls = url.split("\\?");
        String[] queryString = urls[1].split("&");
        return Arrays.asList(queryString).stream().map(param -> param.split("=", 2))
                .collect(Collectors.toMap(a -> a[0], a -> a[1]));
    }

    /**
     * Merge the query parameters with defaultQueryParameters
     * 
     * @param querys Key value pairs of query parameters from API call
     * @return map A merged key value pair of query parameters
     */
    private Map<String, String> mergeDeaultQueryParameters(Map<String, String> querys) {
        for (Entry entry : this.getDefaultQueryParameters().entrySet()) {
            if (!querys.containsKey(entry.getKey())) {
                querys.put((String) entry.getKey(), (String) entry.getValue());
            }
        }
        return querys;
    }

    /**
     * Transfer key value pair of query parameters to query string
     * 
     * @param queryParameters Key value pairs of query parameters
     * @return String queryString
     */
    private static String toQueryString(Map<String, String> queryParameters) {
        return queryParameters.entrySet().stream()
                .map(element -> element.getKey() + "=" + element.getValue())
                .collect(Collectors.joining("&"));
    }

    /**
     * Factory of httpRequest object
     * 
     * @param method http method
     * @param url    the string of url
     * @param entity the http body
     * @return HttpRequest a http request object.
     * @throws UnsupportedEncodingException Error when the http method is not supported
     */
    private static HttpRequestBase requestFactory(String method, String url, HttpEntity entity)
            throws UnsupportedEncodingException {
        switch (method) {
            case "GET":
                return new HttpGet(url);
            case "POST":
                HttpPost post = new HttpPost(url);
                post.setEntity(entity);
                return post;
            case "PUT":
                HttpPut put = new HttpPut(url);
                put.setEntity(entity);
                return put;
            case "PATCH":
                HttpPatch patch = new HttpPatch(url);
                patch.setEntity(entity);
                return patch;
            case "DELETE":
                return new HttpDelete(url);
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }
    }

    /**
     * Response object factory generates Resource/ResourceList/ResponseRaw object
     * 
     * @param response httpResponse object
     * @param url      the url string of current http request
     * @param headers  the key value pairs of current http headers
     * @param method   the http method
     * @return ResponseBase object, could be ResponseRaw, Resource, or ResourceList depends on the
     *         JsonObject type from API Server
     * @throws ApiException Errors when trandfer to ResponseBase object
     */
    private ResponseBase responseFactory(HttpResponse response, String url,
            Map<String, Object> headers, String method) throws ApiException {
        if (response != null && response.getEntity() != null
                && ContentType.get(response.getEntity()).toString()
                        .equals(ContentType.APPLICATION_JSON.getMimeType())) {
            try {
                JsonObject data =
                        gson.fromJson(EntityUtils.toString(response.getEntity()), JsonObject.class);
                if (data.has("type") && "object_list".equals(data.get("type").getAsString())) {
                    List<Resource> tmpList = new ArrayList<Resource>();
                    JsonArray array = data.get("objects").getAsJsonArray();
                    for (int i = 0; i < array.size(); i++) {
                        JsonObject element = array.get(i).getAsJsonObject();
                        Resource one = new Resource(element,
                                generateResourceUrl(url, element, method, true), headers);
                        tmpList.add(one);
                    }

                    ResourceList rList = new ResourceList(url, headers, tmpList, data);
                    return rList;

                } else if (data.has("id")) {
                    return new Resource(data, generateResourceUrl(url, data, method, false),
                            headers);
                } else {
                    return new ResponseJson(data, url, headers);
                }
            } catch (JsonSyntaxException | ParseException | IOException | URISyntaxException e) {
                throw new ApiException("Generate Resource object failed, " + e.getMessage(), e);
            }

        } else {
            return new ResponseRaw(response, url, headers);
        }
    }

    /**
     * This method temporarily handle query string.
     * 
     * @param url       the url of this http query
     * @param data      the response object
     * @param method    the http method
     * @param isListUrl boolean of the queried url is for list or not
     * @return String of handled url
     * @throws URISyntaxException the error occurs when url format is invalid
     */
    private static String generateResourceUrl(String url, JsonObject data, String method,
            boolean isListUrl) throws URISyntaxException {
        if (url.indexOf("?") != -1) {
            String[] unhandledUrl = url.split("\\?");
            return assembleUrlWithId(unhandledUrl[0], data, method, isListUrl) + "?"
                    + unhandledUrl[1];
        }
        return assembleUrlWithId(url, data, method, isListUrl);
    }

    /**
     * Assemale or retrieve url from original http reqeust
     * 
     * @param url       url of original http URL
     * @param data      JsonObject data
     * @param method    the http method
     * @param isListUrl boolean of this url is querying list or object
     * @return String the url with object Id
     * @throws URISyntaxException errors if the url is invalid
     */
    private static String assembleUrlWithId(String url, JsonObject data, String method,
            boolean isListUrl) throws URISyntaxException {
        Optional<JsonElement> href = Optional.ofNullable(data.get("href"));
        if (href.isPresent()) {
            String fromHref = href.get().getAsString();
            URI tempUrl = new URIBuilder(fromHref).build();
            return tempUrl.getPath();
        }
        Optional<JsonElement> id = Optional.ofNullable(data.get("id"));
        if (id.isPresent()) {
            Optional<JsonElement> dataOptional = Optional.ofNullable(data.get("type"));
            Optional<JsonElement> apiOptional = Optional.ofNullable(data.get("api"));
            String apiType = apiOptional.isPresent() ? apiOptional.get().getAsString() : "";
            String dataType = dataOptional.isPresent() ? dataOptional.get().getAsString() : "";
            if (apiType.equals("storage")) {
                URI tempUrl = new URIBuilder(url).build();
                String[] paths = tempUrl.getPath().split("/");
                return String.join("/", Arrays.asList(paths).subList(0, 4))
                        + String.format("/%s/%ss/%s", apiType, dataType, id.get().getAsString());
            }
            if (method.equals("POST") || isListUrl) {
                URI tempUrl = new URIBuilder(url).build();
                return tempUrl.getPath() + "/" + id.get().getAsString();
            }
        }
        return url;
    }
}

package com.kloudless;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import javax.management.RuntimeErrorException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kloudless.exceptions.ApiException;
import com.kloudless.exceptions.InvalidArgumentException;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;


/**
 * Application store global value and provides some helper methods
 */
public class Application {
    private static String baseUrl = "https://api.kloudless.com";
    private static String sdkVersion;
    private static int defaultApiVersion = 1;
    private static int defaultAuthPathVersion = 1;
    private static SimpleClient simpleClient = new SimpleClient();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * static To get version number from app.properties file
     * 
     * @throw RuntimeException error if read file failed or file is not found.
     */
    static {
        try {
            Properties properties = new Properties();
            properties.load(new BufferedReader(new InputStreamReader(
                    Application.class.getClassLoader().getResourceAsStream("app.properties"))));
            Application.sdkVersion = properties.getProperty("version");
        } catch (IOException e) {
            throw new RuntimeException("Read version properties file failed.", e);
        }
    }


    public Application() {
    }

    /**
     * Get global setting of BaseURL, the default value is api.kloudless.com
     * 
     * @return String the BaseURL
     */
    public static String getBaseUrl() {
        return Application.baseUrl;
    }

    /**
     * Set the golbal setting of BaseURL
     * 
     * @param baseUrl String string of BaseUrl
     */
    public static void setBaseUrl(String baseUrl) {
        Application.baseUrl = baseUrl;
    }

    /**
     * Get SDK Version number
     * 
     * @return String the SDK Version number
     */
    public static String getSDKVersion() {
        return Application.sdkVersion;
    }

    /**
     * Get default api version
     * 
     * @return int the default API version
     */
    public static int getDefaultApiVersion() {
        return Application.defaultApiVersion;
    }

    /**
     * Set defautl API version
     * 
     * @param defaultApiVersion to set the defaultApiVersion
     */
    public static void setDefaultApiVersion(int defaultApiVersion) {
        Application.defaultApiVersion = defaultApiVersion;
    }

    /**
     * Get default version of Auth API path
     * 
     * @return int the default API version
     */
    public static int getDefaultAuthPathVersion() {
        return Application.defaultAuthPathVersion;
    }

    /**
     * Set default version of Auth API path
     * 
     * @param defaultAuthPathVersion to set the defaultAuthPathVersion
     */
    public static void setDefaultAuthPathVersion(int defaultAuthPathVersion) {
        Application.defaultAuthPathVersion = defaultAuthPathVersion;
    }

    /**
     * Returns a authorization url to allow user login via Kloudless API server..
     *
     * @param appId       AppId of your Application
     * @param redirectUrl The url can receive the request from Kloudless Server
     * @return A Map includes url and state
     * @throws RuntimeErrorException An error when the url format is inproper.
     */
    public static Map<String, String> getAuthorizationUrl(String appId, String redirectUrl)
            throws RuntimeException {
        return getAuthorizationUrl(appId, redirectUrl, "all", null);
    }

    /**
     * Returns a authorization url to allow user login via Kloudless API server..
     *
     * @param appId       AppId of your Application
     * @param redirectUrl The url can receive the request from Kloudless Server
     * @param scope       The scope of this specific service
     * @return A Map includes url and state
     * @throws RuntimeErrorException An error when the url format is inproper.
     */
    public static Map<String, String> getAuthorizationUrl(String appId, String redirectUrl,
            String scope) throws RuntimeException {
        return getAuthorizationUrl(appId, redirectUrl, scope, null);
    }

    /**
     * Returns a authorization url to allow user login via Kloudless API server..
     *
     * @param appId       AppId of your Application
     * @param redirectUrl The url can receive the request from Kloudless Server
     * @param scope       The scope of this specific service
     * @param state       An random string to verify the request from Kloudless Server
     * @return A Map includes url and state
     * @throws RuntimeErrorException An error when the url format is inproper.
     */
    public static Map<String, String> getAuthorizationUrl(String appId, String redirectUrl,
            String scope, String state) throws RuntimeException {
        if (state == null) {
            try {
                state = Base64.getEncoder()
                        .encodeToString(UUID.randomUUID().toString().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                state = UUID.randomUUID().toString();
            }
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("client_id", appId);
        params.put("redirect_uri", redirectUrl);
        params.put("scope", scope);
        params.put("state", state);
        params.put("response_type", "code");

        String path = String.format("v%s/", defaultAuthPathVersion) + "oauth";

        Map<String, String> result;
        try {
            URIBuilder uriBuilder = new URIBuilder(Application.baseUrl).setPath(path);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), entry.getValue().toString());
            }
            result = new HashMap<String, String>();
            result.put("url", uriBuilder.build().toURL().toString());
            result.put("state", state);
            return result;
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException("Generate autohrization url failed. ", e);
        }
    }

    /**
     * Returns a bearer token to allow user access via Kloudless API server..
     *
     * @param state           An random string to verify the request from Kloudless Server
     * @param origState       The original state is for verification.
     * @param code            The string is return from Kloudless server then callback is invoked
     * @param origRedirectUrl The original url can receive the request from Kloudless Server
     * @param appId           AppId of your application
     * @param apiKey          Api key of your application
     * @return String Bearer token
     * @throws ApiException             An error if any data other than 2xx from Kloudless API
     *                                  server or upstream services.
     * @throws InvalidArgumentException An error if origState and state are not match
     */
    public static String retrieveToken(String state, String origState, String code,
            String origRedirectUrl, String appId, String apiKey)
            throws InvalidArgumentException, ApiException {

        if (!origState.equals(state)) {
            throw new InvalidArgumentException(
                    String.format("State %s does not match original state %s", state, origState),
                    null);
        }
        String url = String.format("/v%s/", defaultAuthPathVersion) + "oauth/token";

        try {
            return getToken(url,
                    assembleTokenRetrievingContent(code, origRedirectUrl, appId, apiKey));
        } catch (ParseException | IOException e) {
            throw new ApiException("Get token failed, " + e.getMessage(), e);
        }
    }

    /**
     * Help method to verify token and App Id is match
     *
     * @param token Bearer token for verification.
     * @param appId App Id
     * @return true or false if token is inconsistent with App Id
     * @throws ApiException An error if any data other than 2xx from Kloudless API server or
     *                      upstream services.
     */
    public static boolean verifyToken(String token, String appId) throws ApiException {
        try {
            String verifyData = getApplicationId(token);
            if (verifyData.equals(appId)) {
                return true;
            }
            return false;
        } catch (ParseException | IOException e) {
            throw new ApiException("Verify token failed " + e.getMessage(), e);
        }
    }

    /**
     * Use bearer token to get Application Id from API server
     * 
     * @param String Bearer token
     * @return String Application Id
     */
    private static String getApplicationId(String token)
            throws ApiException, ClientProtocolException, ParseException, IOException {
        String url = String.format("/v%s/", defaultAuthPathVersion) + "oauth/token";
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("Authorization", "Bearer " + token);
        HttpResponse response = simpleClient.rawFormExecute("GET", url, headers, null);
        JsonObject data =
                gson.fromJson(EntityUtils.toString(response.getEntity()), JsonObject.class);
        Optional<JsonElement> clientId = Optional.ofNullable(data.get("client_id"));
        return clientId.isPresent() ? clientId.get().getAsString() : "";
    }

    /**
     * Assemble the http body for token retrieving from API Server
     * 
     * @param code            String from API Server in the OAuth flow
     * @param origRedirectUrl Original input in the first leg of OAuth flow
     * @param appId           AppId of your application
     * @param apiKey          Api Key of your application
     * @return Map the key value pair of all http body
     */
    private static Map<String, Object> assembleTokenRetrievingContent(String code,
            String origRedirectUrl, String appId, String apiKey) {
        Map<String, Object> requestContent = new HashMap<String, Object>();
        requestContent.put("grant_type", "authorization_code");
        requestContent.put("code", code);
        requestContent.put("redirect_uri", origRedirectUrl);
        requestContent.put("client_id", appId);
        requestContent.put("client_secret", apiKey);
        return requestContent;
    }

    /**
     * Get token from API Server
     * 
     * @param url         String of url
     * @param httoContent Key, value pairs will be in http body
     * @return String Bearer token
     */
    private static String getToken(String url, Map<String, Object> httpContent)
            throws ClientProtocolException, ApiException, ParseException, IOException {
        HttpResponse response = simpleClient.rawFormExecute("POST", url, null, httpContent);
        JsonObject jsonObject =
                gson.fromJson(EntityUtils.toString(response.getEntity()), JsonObject.class);
        return jsonObject.get("access_token").getAsString();
    }

}

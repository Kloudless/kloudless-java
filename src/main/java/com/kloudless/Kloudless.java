package com.kloudless;

import java.util.HashMap;
import java.util.Map;

public abstract class Kloudless {

	public static final String BASE_URL = "https://api.kloudless.com";
	public static final String VERSION = "1.0.0";
	public static String apiKey = null;
	public static String accountId = null;
	public static String developerKey = null;
	public static String bearerToken = null;
	public static String apiVersion = "1";
	public static Map<String, String> customHeaders = new HashMap<String, String>();
	public static final String APPLICATIONS = "applications";

	private static String apiBase = BASE_URL; 
	

	/**
	 * (FOR TESTING ONLY) If you'd like your API requests to hit your own
	 * (mocked) server, you can set this up here by overriding the base api URL.
	 */
	public static void overrideApiBase(final String overriddenApiBase) {
		apiBase = overriddenApiBase;
	}

	public static String getApiBase() {
		return apiBase;
	}

	public static void addCustomHeaders(Map<String, String> headers) {
		customHeaders.putAll(headers);
	}

}

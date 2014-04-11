package com.kloudless;

public abstract class Kloudless {

	public static final String BASE_URL = "https://api.kloudless.com";
	public static final String VERSION = "0.0.1";
	public static String apiKey = null;
	public static String accountId = null;
	public static String accountKey = null;
	public static String apiVersion = "0";

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

}

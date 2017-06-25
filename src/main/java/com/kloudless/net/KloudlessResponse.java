package com.kloudless.net;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

public class KloudlessResponse {
	int responseCode;
	String responseBody;
	Map<String, List<String>> responseHeaders;
	ByteArrayOutputStream responseStream;

	public KloudlessResponse(int responseCode, String responseBody) {
		this.responseCode = responseCode;
		this.responseBody = responseBody;
		this.responseHeaders = null;
	}

	public KloudlessResponse(int responseCode, String responseBody,
													 Map<String, List<String>> responseHeaders) {
		this.responseCode = responseCode;
		this.responseBody = responseBody;
		this.responseHeaders = responseHeaders;
	}

	public KloudlessResponse(int responseCode, String responseBody,
													 Map<String, List<String>> responseHeaders, ByteArrayOutputStream responseStream) {
		this.responseCode = responseCode;
		this.responseBody = responseBody;
		this.responseHeaders = responseHeaders;
		this.responseStream = responseStream;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public Map<String, List<String>> getResponseHeaders() {
		return responseHeaders;
	}

	public ByteArrayOutputStream getResponseStream() {
		return responseStream;
	}

	public void setResponseStream(ByteArrayOutputStream responseStream) {
		this.responseStream = responseStream;
	}
}

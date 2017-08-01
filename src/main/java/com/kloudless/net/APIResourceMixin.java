package com.kloudless.net;

import java.util.Map;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;

public abstract class APIResourceMixin extends APIResource {

	// List Mixin
	/**
	 * The all() method is a "mixin" that allows for a list of objects to be returned.
	 * By passing in a class and path url, the list of objects is abstracted away along
	 * with the http requests.  A JSON object will be returned with 
	 * @param path -- url path for the list resource
	 * @param params -- url query parameters
	 * @param clazz -- class of the resource object to be returned
	 * @param apiKey -- optional parameter if you want to override apiKey
	 * @return <Resource>Collection of type class that you pass in
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	protected static <T> T all(String path, Map<String, Object> params,
			Class<T> clazz, Map<String, String> headers) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
				
		KloudlessResponse response = request(RequestMethod.GET, path, params,
					headers);
		int rCode = response.responseCode;
		String rBody = response.responseBody;
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return GSON.fromJson(rBody, clazz);
	}

	// Retrieve Mixin
	protected static <T> T retrieve(String path, Map<String, Object> params,
			Class<T> clazz, Map<String, String> keys) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		KloudlessResponse response = request(RequestMethod.GET, path, params,
				keys);

		int rCode = response.responseCode;
		String rBody = response.responseBody;
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return GSON.fromJson(rBody, clazz);
	}

	// Create Mixin
	protected static <T> T create(String path, Map<String, Object> params,
			Class<T> clazz, Map<String, String> keys) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		KloudlessResponse response = request(RequestMethod.POST, path, params, keys);
		int rCode = response.responseCode;
		String rBody = response.responseBody;
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return GSON.fromJson(rBody, clazz);
	}

	// UpdatePartial Mixin
	protected static <T> T save(String path, Map<String, Object> params,
			Class<T> clazz, Map<String, String> keys) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		KloudlessResponse response = request(RequestMethod.PATCH, path, params, keys);
		int rCode = response.responseCode;
		String rBody = response.responseBody;
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return GSON.fromJson(rBody, clazz);
	}
	
	// Update Mixin
	protected static <T> T update(String path, Map<String, Object> params,
			Class<T> clazz, Map<String, String> keys) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		KloudlessResponse response = request(RequestMethod.PUT, path, params, keys);
		int rCode = response.responseCode;
		String rBody = response.responseBody;
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return GSON.fromJson(rBody, clazz);
	}

	// Delete Mixin
	protected static KloudlessResponse delete(String path,
			Map<String, Object> params, Map<String, String> keys) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		KloudlessResponse response = request(RequestMethod.DELETE, path,
				params, keys);

		int rCode = response.responseCode;
		String rBody = response.responseBody;
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return response;
	}

}

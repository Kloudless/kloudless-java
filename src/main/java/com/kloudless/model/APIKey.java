package com.kloudless.model;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.net.APIResourceMixin;
import com.kloudless.net.KloudlessResponse;

import java.util.Map;

public class APIKey extends APIResourceMixin {

	public String key;
	public String created;
	public String modified;

	/**
	 * Makes a Kloudless API request returning an APIKeyCollection (list of APIKey objects).
	 * Use this method to retrieve all APIKeys associated with your application.
	 *
	 * @param applicationId - application identifier
	 * @param params        - query parameters that include page and page_size.
	 * @return APIKeyCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static APIKeyCollection all(String applicationId, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {

		String path = String.format("%s/%s",
				instanceURL(Application.class, applicationId),
				classURL(APIKey.class));

		return all(path, params, APIKeyCollection.class, null);
	}

	/**
	 * Makes a Kloudless API request to create a new APIKey.
	 *
	 * @param applicationId - application identifier
	 * @param params        - no query parameters
	 * @return APIKey
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static APIKey create(String applicationId, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {

		String path = String.format("%s/%s",
				instanceURL(Application.class, applicationId),
				classURL(APIKey.class));

		return create(path, params, APIKey.class, null);
	}

	/**
	 * Makes a Kloudless API request to delete an APIKey. Returns a success or false within the
	 * KloudlessResponse object.
	 *
	 * @param APIKey
	 * @param applicationId - application identifier
	 * @param params        - no query parameters for deleting an APIKey
	 * @return KloudlessResponse
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static KloudlessResponse delete(String APIKey, String applicationId,
																				 Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {

		String path = String.format("%s/%s",
				instanceURL(Application.class, applicationId),
				instanceURL(APIKey.class, APIKey));

		return delete(path, params, null);
	}
}

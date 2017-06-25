package com.kloudless.model;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.net.APIResourceMixin;
import com.kloudless.net.KloudlessResponse;

import java.util.Map;

public class Application extends APIResourceMixin {

	public String id;
	public String name;
	public String description;
	public String logo_url;
	public Boolean active;
	public String created;
	public String modified;
	public String source;

	/**
	 * Makes a Kloudless API request returning an ApplicationCollection (list of Application objects).
	 * Use this method to retrieve all applications associated with your account.
	 *
	 * @param params - query parameters that include page, page_size, and active.
	 * @return ApplicationCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static ApplicationCollection all(Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		return all(classURL(Application.class), params, ApplicationCollection.class, null);
	}

	/**
	 * Makes a Kloudless API request to create a new Application.
	 *
	 * @param accountId - account identifier
	 * @param params    - The parameters include:
	 *                  - name - name of the new application
	 *                  - description - description of the new application
	 *                  - logo_url - URL of logo
	 *                  - source - The ID of another application owned by the developer
	 *                  to use as a template to create the new application
	 * @return Application
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static Application create(Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		return create(classURL(Application.class), params, Application.class, null);
	}

	/**
	 * Makes a Kloudless API request to retrieve information about an individual application.
	 *
	 * @param applicationId - application identifier
	 * @param params        - no query parameters for retrieving application data
	 * @return File
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static Application retrieve(String applicationId,
																		 Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		return retrieve(instanceURL(Application.class, applicationId),
				params, Application.class, null);
	}

	/**
	 * Makes a Kloudless API request to update properties of an Application.  The contents
	 * are placed in the body
	 *
	 * @param applicationId - application identifier
	 * @param params        - The parameters include:
	 *                      - name - name of the new application
	 *                      - description - description of the new application
	 *                      - logo_url - URL of logo
	 * @return Application
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static Application update(String applicationId, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {

		KloudlessResponse response = request(RequestMethod.PATCH,
				instanceURL(Application.class, applicationId),
				params, null);
		int rCode = response.getResponseCode();
		String rBody = response.getResponseBody();
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return GSON.fromJson(rBody, Application.class);
	}

	/**
	 * Makes a Kloudless API request to delete an Application. Returns a success or false within the
	 * KloudlessResponse object.
	 *
	 * @param applicationId
	 * @param params        - no query parameters for deleting an Application
	 * @return KloudlessResponse
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static KloudlessResponse delete(String applicationId,
																				 Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		return delete(instanceURL(Application.class, applicationId),
				params, null);
	}
}

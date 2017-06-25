package com.kloudless.model;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.net.APIResourceMixin;

import java.util.Map;

public class User extends APIResourceMixin {
	public String email;
	public String id;
	public String name;
	public String role;

	/**
	 * Makes a Kloudless API request returning a UserCollection (list of User objects).
	 * Use this method to retrieve all users associated with your admin account.
	 *
	 * @param accountId - account identifier
	 * @param params    - query parameters that include page and page_size
	 * @return UserCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static UserCollection all(String accountId, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {

		String path = String.format("%s/%s/%s",
				instanceURL(Account.class, accountId),
				"team",
				classURL(User.class));
		return all(path, params, UserCollection.class, null);
	}

	/**
	 * Makes a Kloudless API request to retrieve data on an individual user.
	 *
	 * @param id        - user identifier
	 * @param accountId - account identifier
	 * @param params    - no query parameters for retrieving user data
	 * @return User
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static User retrieve(String id, String accountId,
															Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {

		String path = String.format("%s/%s/%s",
				instanceURL(Account.class, accountId),
				"team",
				instanceURL(User.class, id));
		return retrieve(path, params, User.class, null);
	}

	/**
	 * Makes a Kloudless API request returning a GroupCollection (list of Group objects).
	 * Use this method to retrieve a list of a user's group memberships.
	 *
	 * @param id        - user identifier
	 * @param accountId - account identifier
	 * @param params    - query parameters that include page and page_size
	 * @return GroupCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static GroupCollection groups(String id, String accountId, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {

		String path = String.format("%s/%s/%s/%s",
				instanceURL(Account.class, accountId),
				"team",
				instanceURL(User.class, id),
				"memberships");
		return all(path, params, GroupCollection.class, null);
	}
}

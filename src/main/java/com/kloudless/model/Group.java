package com.kloudless.model;

import java.util.Map;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.net.APIResourceMixin;

public class Group extends APIResourceMixin {
	public String id;
	public String name;
	public String role;
	
	/**
	 * Makes a Kloudless API request returning a GroupCollection (list of Group objects).
	 * Use this method to retrieve a list of groups associated with your admin account
	 *
	 * @param accountId - account identifier
	 * @param params - query parameters that include page and page_size
	 * @return GroupCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static GroupCollection all(String accountId, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId),
				classURL(Group.class));
		return all(path, params, GroupCollection.class, null);
	}
	
	/**
	 * Makes a Kloudless API request to retrieve data on a specific group.
	 * 
	 * @param id - group identifier
	 * @param accountId - account identifier
	 * @param params - no query parameters for retrieving user data
	 * @return Group
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static Group retrieve(String id, String accountId,
			Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId),
				instanceURL(Group.class, id));
		return retrieve(path, params, Group.class, null);
	}
	
	/**
	 * Makes a Kloudless API request returning a UserCollection (list of User objects).
	 * Use this method to retrieve a list of users associated with your group
	 * 
	 * @param id - group identifier
	 * @param accountId - account identifier
	 * @param params - no query parameters for retrieving user data
	 * @return UserCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static UserCollection users(String id, String accountId,
			Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		
		String path = String.format("%s/%s/%s",
				instanceURL(Account.class, accountId),
				instanceURL(Group.class, id),
				"members");
		return all(path, params, UserCollection.class, null);
	}
}

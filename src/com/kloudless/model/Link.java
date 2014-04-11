package com.kloudless.model;

import java.util.Map;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.net.APIResourceMixin;
import com.kloudless.net.KloudlessResponse;

public class Link extends APIResourceMixin {

	public String id;
	public String file_id;
	public String url;
	public Integer account;
	public Boolean active;
	public String created;
	public String modified;

	/**
	 * Makes a Kloudless API request to retrieve all links associated with this account. Returns a LinkCollection
	 * which is a list of metadata for all links.
	 * 
	 * @param accountId
	 * @param params
	 * @return LinkCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static LinkCollection all(String accountId, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId), classURL(Link.class));
		return all(path, params, LinkCollection.class, null);
	}

	/**
	 * Makes a Kloudless API request to retrieve metadata of a specific link.
	 * 
	 * @param id - identifier of link
	 * @param accountId
	 * @param params
	 * @return Link
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static Link retrieve(String id, String accountId,
			Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String
				.format("%s/%s", instanceURL(Account.class, accountId),
						instanceURL(Link.class, id));
		return retrieve(path, params, Link.class, null);
	}

	/**
	 * Makes a Kloudless API request to update an existing link.
	 * 
	 * @param id
	 * @param accountId
	 * @param params - The parameters include:
	 * 		- active
	 * 		- expiration
	 * 		- password
	 * @return Link
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static Link save(String id, String accountId,
			Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId),
				instanceURL(Link.class, id));
		return save(path, params, Link.class, null);
	}

	/**
	 * Makes a Kloudless API request to create a link.
	 * 
	 * @param accountId
	 * @param params - The parameters include:
	 * 		- file_id - the file identifier to create the link from
	 * 		- password - password
	 * 		- expiration - ISO 8601 timestamp specifying when the link expires
	 * 		- direct_link - specifying whether the link is a direct link or not
	 * @return Link
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static Link create(String accountId, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId), classURL(Link.class));
		return create(path, params, Link.class, null);
	}

	/**
	 * Makes a Kloudless API Request to delete a link
	 * 
	 * @param id - link identifier
	 * @param params
	 * @return KloudlessResponse
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static KloudlessResponse delete(String id, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		return delete(instanceURL(Link.class, id), params, null);
	}

}

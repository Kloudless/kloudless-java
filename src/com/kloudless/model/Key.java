package com.kloudless.model;

import java.util.Map;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.net.APIResourceMixin;

public class Key extends APIResourceMixin {

	public String id;
	public Boolean active;
	public String key;
	public Integer account;
	public String expiration;

	/**
	 * Makes a Kloudless API request to retrieve all account keys associated with this account. Returns an AccountKeyCollection
	 * which is a list of account key data.
	 * 
	 * @param accountId
	 * @param params
	 * @return AccountKeyCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static KeyCollection all(String accountId, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId), classURL(Key.class));
		return all(path, params, KeyCollection.class, null);
	}

	/**
	 * Makes a Kloudless API request to retrieve data of a specific account key.
	 * 
	 * @param id - identifier of account key
	 * @param accountId
	 * @param params
	 * @return AccountKey
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static Key retrieve(String id, String accountId,
			Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String
				.format("%s/%s", instanceURL(Account.class, accountId),
						instanceURL(Key.class, id));
		return retrieve(path, params, Key.class, null);
	}

}

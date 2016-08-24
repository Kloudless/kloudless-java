package com.kloudless.model;

import java.util.Map;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.net.APIResourceMixin;

public class Token extends APIResourceMixin {

	public String id;
	public Boolean active;
	public String key;
	public Long account;
	public String expiration;

	/**
	 * Makes a Kloudless API request to retrieve all bearer tokens associated
	 * with this account. Returns a TokenCollection which is a list of
	 * token data.
	 *
	 * @param accountId
	 * @param params
	 * @return TokenCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static TokenCollection all(String accountId, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId), classURL(Token.class));
		return all(path, params, TokenCollection.class, null);
	}

	/**
	 * Makes a Kloudless API request to retrieve data of a specific Bearer token.
	 *
	 * @param id - identifier of Bearer token
	 * @param accountId
	 * @param params
	 * @return Token
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static Token retrieve(String id, String accountId,
			Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String
				.format("%s/%s", instanceURL(Account.class, accountId),
						instanceURL(Token.class, id));
		return retrieve(path, params, Token.class, null);
	}

}

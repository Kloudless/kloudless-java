package com.kloudless.model;

import java.util.Map;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.net.APIResourceMixin;
import com.kloudless.net.KloudlessResponse;

public class Account extends APIResourceMixin {

	public String id;
	public String account;
	public Boolean active;
	public String service;
	public String created;
	public String modified;

	/**
	 * Makes a Kloudless API request returning an AccountCollection (list of Account objects).
	 * Use this method to retrieve all accounts associated with your application.
	 *
	 * @param params - query parameters that include page, pageCount, and active.
	 * @return AccountCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static AccountCollection all(Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		return all(classURL(Account.class), params, AccountCollection.class, null);
	}


	/**
	 * Makes a Kloudless API request to retrieve account metadata for a connected cloud
	 * storage account. Use this method to retrieve a specific account.
	 * 
	 * @param id - the account id (ex: 42)
	 * @param params - query parameters that include active.
	 * @return Account
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static Account retrieve(String id, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		return retrieve(instanceURL(Account.class, id), params, Account.class, null);
	}

	/**
	 * Makes a Kloudless API request to delete an account returning a KloudlessResponse
	 * of a successful delete.  Use this method to delete a specific account.
	 * 
	 * @param id - the account id (ex: 42)
	 * @param params - query parameters
	 * @return KloudlessResponse
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static KloudlessResponse delete(String id, Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		return delete(instanceURL(Account.class, id), params, null);
	}
	
	/**
	 * Makes a Kloudless API request to return a MetadataCollection of files/folders that match a search query.
	 * You can search across multiple accounts with comma separated ids: 1,2,3,4.  If you are using
	 * this in the Android SDK, you can only search across a single account associated with your account
	 * key. Use this method to search files and folders.
	 * 
	 * @param id - the account id or comma separated account ids.
	 * @param params - query parameters
	 * @return MetadataCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static MetadataCollection search(String id, Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String.format("%s/search", instanceURL(Account.class, id));
		KloudlessResponse response = request(RequestMethod.GET, path, params,
				null);

		int rCode = response.getResponseCode();
		String rBody = response.getResponseBody();
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return GSON.fromJson(rBody, MetadataCollection.class);
	}

	/**
	 * Makes a Kloudless API request for a list of recent files. If using an ApiKey, the recent() method
	 * can return files for multiple accounts.  If using an Account Key, the recent() method returns files
	 * for a single account.
	 *
	 * @param id account ID or comma delimited string of accounts (EX: 42 or 2,4,42
	 * @param params - parameters include:
	 * 		- page_size
	 * 		- page
	 * @return FileCollection of the format:
	 * 		- total
	 * 		- count
	 * 		- page
	 * 		- files
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static FileCollection recent(String id, Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String.format("%s/recent", instanceURL(Account.class, id));
		KloudlessResponse response = request(RequestMethod.GET, path, params, null);

		int rCode = response.getResponseCode();
		String rBody = response.getResponseBody();
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return GSON.fromJson(rBody, FileCollection.class);
	}

	/**
	 * Makes a Kloudless API request for a list of events for this account.
	 *
	 * @param id account ID or comma delimited string of accounts (EX: 42 or 2,4,42
	 * @param params - parameters include:
	 * 		- cursor
	 * @return EventCollection of the format:
	 * 		- cursor
	 * 		- objects
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static EventCollection events(String id, Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String.format("%s/events", instanceURL(Account.class, id));
		KloudlessResponse response = request(RequestMethod.GET, path, params, null);

		int rCode = response.getResponseCode();
		String rBody = response.getResponseBody();
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return GSON.fromJson(rBody, EventCollection.class);
	}

}

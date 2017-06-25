package com.kloudless.model;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.net.KloudlessResponse;

import java.util.Map;

public class Folder extends Metadata {

	/**
	 * Makes a Kloudless API request that returns the contents of a specific folder.  A MetadataCollection is
	 * returned containing a list of objects of files and folders.
	 *
	 * @param id        - identifier of folder
	 * @param accountId
	 * @param params    - The parameters can include:
	 *                  - page_number
	 *                  - page_size
	 * @return MetadataCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static MetadataCollection contents(String id, String accountId,
																						Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId),
				detailURL(Folder.class, id));
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
	 * Makes a Kloudless API request to retrieve folder metadata for a specific folder
	 *
	 * @param id        - folder identifier
	 * @param accountId
	 * @param params
	 * @return Folder
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static Folder retrieve(String id, String accountId,
																Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId),
				instanceURL(Folder.class, id));
		return retrieve(path, params, Folder.class, null);
	}

	/**
	 * Makes a Kloudless API request to rename or move the folder. Since this is a patch request, the parameters are
	 * moved to the request body.
	 *
	 * @param id
	 * @param accountId
	 * @param params    - The parameters include:
	 *                  - parent_id
	 *                  - name
	 * @return Folder
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static Folder save(String id, String accountId,
														Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId),
				instanceURL(Folder.class, id));
		return save(path, params, Folder.class, null);
	}

	/**
	 * Makes a Kloudless API request to create a folder. Parameters are placed into the body
	 *
	 * @param accountId
	 * @param params    - The parameters include:
	 *                  - parent_id
	 *                  - name
	 *                  - conflict_if_exists - if true, an existing folder with the same name will result in an error
	 * @return Folder
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static Folder create(String accountId, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId), classURL(Folder.class));

		// conflict_if_exists
		if (params.containsKey("conflict_if_exists")) {
			Object conflict = params.remove("conflict_if_exists");
			path += "?" + conflict.toString();
		}

		return create(path, params, Folder.class, null);
	}

	/**
	 * Makes a Kloudless API request to delete a folder.  The KloudlessResponse returns true or false for whether
	 * a delete was successful or not.
	 *
	 * @param id        - folder identifier
	 * @param accountId
	 * @param params
	 * @return KloudlessResponse
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static KloudlessResponse delete(String id, String accountId,
																				 Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId),
				instanceURL(Folder.class, id));
		return delete(path, params, null);
	}

}

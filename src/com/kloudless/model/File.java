package com.kloudless.model;

import java.util.Map;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.net.KloudlessResponse;

public class File extends Metadata {

	/**
	 * Makes a Kloudless API Request to retrieve the File contents given a file identifier.  The KloudlessResponse
	 * contains a string responseBody and stream responseStream format.
	 * 
	 * @param id - file identifier
	 * @param accountId - account identifier
	 * @param params - no query parameters for downloading a file
	 * @return KloudlessResponse
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static KloudlessResponse contents(String id, String accountId,
			Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId),
				detailURL(File.class, id));
		KloudlessResponse response = request(RequestMethod.GET, path, params,
				null);
		return response;
	}

	/**
	 * Makes a Kloudless API request to retrieve file metadata information.
	 * 
	 * @param id - file identifier
	 * @param accountId - account identifier
	 * @param params - no query parameters for retrieving file metadata
	 * @return File
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static File retrieve(String id, String accountId,
			Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId),
				instanceURL(File.class, id));
		return retrieve(path, params, File.class, null);
	}

	/**
	 * Makes a Kloudless API request to rename or move a file. Since this is a patch request, the parameters are
	 * moved to the request body.
	 * 
	 * @param id
	 * @param accountId
	 * @param params - The optional parameters include:
	 * 		- parent_id
	 * 		- name
	 * 		- account
	 * @return File
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static File save(String id, String accountId,
			Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId),
				instanceURL(File.class, id));
		return save(path, params, File.class, null);
	}

	/**
	 * Makes a Kloudless API request to upload a file.  Since file uploads are performed as a multipart/form
	 * data request, the parameters are placed into the body.
	 * 
	 * @param accountId
	 * @param params - The parameters include:
	 * 		- file - byte array of file
	 * 		- metadata - this is a JSON string of metadata information including
	 * 			- parent_id - identifier of where the file needs to be placed
	 * 			- name - name of the file
	 * 		- overwrite - option true/false of whether you want the file to be overwritten
	 * @return File
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static File create(String accountId, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {

		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId), classURL(File.class));

		// overwrite
		if (params.containsKey("overwrite")) {
			Object overwrite = params.remove("overwrite");
			path += "?" + overwrite.toString();
		}
		
		return create(path, params, File.class, null);
	}

	/**
	 * Makes a Kloudless API request to update contents of a file.  The contents
	 * are placed in the body
	 * 
	 * @param id
	 * @param accountId
	 * @param params - The parameters include:
	 * 		- body - byte array of file
	 * @return File
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static File update(String id, String accountId, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {

		String path = String.format("%s/%s",
				instanceURL(Account.class, accountId),
				instanceURL(File.class, id));
		return update(path, params, File.class, null);
	}
	
	/**
	 * Makes a Kloudless API request to delete a file. Returns a success or false within the
	 * KloudlessResponse object.
	 * 
	 * @param id
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
				instanceURL(File.class, id));
		return delete(path, params, null);
	}

	/**
	 * Makes a Kloudless API request to copy a file.
	 * 
	 * @param id - the account id or comma separated account ids.
	 * @param params - query parameters
	 * @return MetadataCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static File copy(String id, String accountId, Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String.format("%s/%s/copy",
				instanceURL(Account.class, accountId),
				instanceURL(File.class, id));
		KloudlessResponse response = request(RequestMethod.POST, path, params,
				null);

		int rCode = response.getResponseCode();
		String rBody = response.getResponseBody();
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return GSON.fromJson(rBody, File.class);
	}	
	
}

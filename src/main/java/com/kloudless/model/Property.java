package com.kloudless.model;

import java.util.Map;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.net.APIResourceMixin;
import com.kloudless.net.KloudlessResponse;

public class Property extends APIResourceMixin {
	public String key;
	public String value;
	public String created;
	public String modified;
	public String box_scope;
	public String box_template;
	private String property_class = "properties";
	
	/**
	 * Makes a Kloudless API Request returning a PropertyCollection (list of Property objects) for a given file.
	 * The KloudlessResponse contains a string responseBody and stream responseStream format.
	 * 
	 * @param id - file identifier
	 * @param accountId - account identifier
	 * @param params - no query parameters for this method
	 * @return PropertyCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static PropertyCollection all(String id, String accountId,
			Map<String, Object> params) throws APIException, 
			AuthenticationException, InvalidRequestException, 
			APIConnectionException {
		
		String path = String.format("%s/%s/%s",
				instanceURL(Account.class, accountId),
				instanceURL(File.class, id),
				property_class
		);
		return all(path, params, PropertyCollection.class, null);
	}
	
	/**
	 * Makes a Kloudless API request to update the properties of a file object.  The contents
	 * are placed in the body
	 * 
	 * @param id - file identifier
	 * @param accountId - account identifier
	 * @param params:
	 * 		- list of objects
	 * 			-key: key, value: value
	 * @return PropertyCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static PropertyCollection update(String id, String accountId, List<Map<String, String>> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		String path = String.format("%s/%s/%s",
				instanceURL(Account.class, accountId),
				instanceURL(File.class, id),
				property_class
		);
		return update(path, params, PropertyCollection.class, null);
	}
	
	/**
	 * Makes a Kloudless API request to update the delete the properties of a file object.
	 * 
	 * @param id - file identifier
	 * @param accountId - account identifier
	 * @param params - no query parameters for this method
	 * @return KloudlessResponse
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	
	public static KloudlessResponse delete(String id, String accountId, Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {
		String path = String.format("%s/%s/%s",
				instanceURL(Account.class, accountId),
				instanceURL(File.class, id),
				property_class
		);
		return delete(path, params, null);
	}
	
}

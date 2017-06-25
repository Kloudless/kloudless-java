package com.kloudless.model;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.net.APIResourceMixin;

import java.util.Map;

public class Permission extends APIResourceMixin {
	public String id;
	public String email;
	public String name;
	public String role;

	/**
	 * Makes a Kloudless API Request returning a PermissionCollection (list of Permission objects).
	 * The KloudlessResponse contains a string responseBody and stream responseStream format.
	 *
	 * @param id        - file/folder identifier
	 * @param accountId - account identifier
	 * @param type      - "file" or "folder"
	 * @param params    - no query parameters for this method
	 * @return PermissionCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static PermissionCollection all(String id, String accountId, String type,
																				 Map<String, Object> params) throws APIException,
			AuthenticationException, InvalidRequestException,
			APIConnectionException {

		String path = null;
		if (type == "folder") {
			path = String.format("%s/%s/%s",
					instanceURL(Account.class, accountId),
					instanceURL(Folder.class, id),
					classURL(Permission.class)
			);
		} else if (type == "file") {
			path = String.format("%s/%s/%s",
					instanceURL(Account.class, accountId),
					instanceURL(File.class, id),
					classURL(Permission.class)
			);
		}
		return all(path, params, PermissionCollection.class, null);
	}

	/**
	 * Makes a Kloudless API request to update/override the permissions of a file/folder object.  The contents
	 * are placed in the body
	 *
	 * @param id        - file/folder identifier
	 * @param accountId - account identifier
	 * @param type      - "file" or "folder"
	 * @param params:   - key: email, value: role  ("previewer, "reader", "writer", "owner")
	 * @return PermissionCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static PermissionCollection update(String id, String accountId, String type, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {
		String path = null;
		if (type == "folder") {
			path = String.format("%s/%s/%s",
					instanceURL(Account.class, accountId),
					instanceURL(Folder.class, id),
					classURL(Permission.class)
			);
		} else if (type == "file") {
			path = String.format("%s/%s/%s",
					instanceURL(Account.class, accountId),
					instanceURL(File.class, id),
					classURL(Permission.class)
			);
		}
		return update(path, params, PermissionCollection.class, null);
	}

	/**
	 * Makes a Kloudless API request to update the permissions of a file/folder object.  The contents
	 * are placed in the body
	 *
	 * @param id        - file/folder identifier
	 * @param accountId - account identifier
	 * @param type      - "file" or "folder"
	 * @param params:   - key: email, value: role  ("previewer, "reader", "writer", "owner")
	 * @return PermissionCollection
	 * @throws APIException
	 * @throws AuthenticationException
	 * @throws InvalidRequestException
	 * @throws APIConnectionException
	 */
	public static PermissionCollection save(String id, String accountId, String type, Map<String, Object> params)
			throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {

		String path = null;
		if (type == "folder") {
			path = String.format("%s/%s/%s",
					instanceURL(Account.class, accountId),
					instanceURL(Folder.class, id),
					classURL(Permission.class)
			);
		} else if (type == "file") {
			path = String.format("%s/%s/%s",
					instanceURL(Account.class, accountId),
					instanceURL(File.class, id),
					classURL(Permission.class)
			);
		}
		return save(path, params, PermissionCollection.class, null);
	}
}

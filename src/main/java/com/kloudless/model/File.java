package com.kloudless.model;

import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.net.KloudlessResponse;

import java.util.Hashtable;
import java.util.Map;

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
	 * @return
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

	/**
	 * Make a Kloudless API request to initialize the session of multipart upload
	 *
	 * @param accountId the account id
	 * @param params the parameters include
	 *    - name - The name of the file to upload
	 *    - parent_id - The ID of the parent folder to upload the file to
	 *    - size - The total size of the file being uploaded
	 * @param keys query parameters
	 *    - overwrite - overwrite a existing file
	 * @return {@code File.Multipart}
	 * @throws InvalidRequestException
	 * @throws APIException
	 * @throws APIConnectionException
	 * @throws AuthenticationException
	 */
	public static File.Multipart initializeMultipartUpload(String accountId,
	    Map<String, Object> params, Map<String, String> keys)
			throws InvalidRequestException, APIException, APIConnectionException,
			    AuthenticationException {

		String path = String.format("%s/storage/multipart",
				instanceURL(Account.class, accountId));

		File.Multipart multipart = create(path, params, File.Multipart.class, keys);
		multipart.setOriginalFileSize((Long) params.get("size"));
		return multipart;
	}


	/**
	 * Make a Kloudless API request to upload parts of a file
	 *
	 * @param accountId the account id
	 * @param params the parameters include
	 *     - part_id  - the session id of the multipart upload
	 *     - part_num - corresponds to the order in which the part appears in the original file.
	 * @return {@code KloudlessResponse}
	 * @throws InvalidRequestException
	 * @throws APIException
	 * @throws APIConnectionException
	 * @throws AuthenticationException
	 */
	public static KloudlessResponse multipartUpload(String accountId, Map<String, Object> params)
			throws InvalidRequestException, APIException, APIConnectionException,
			    AuthenticationException {

		String path = String.format("%s/storage/multipart/%d?part_number=%d",
				instanceURL(Account.class, accountId), (int)params.get("part_id"),
				(int)params.get("part_number"));
		KloudlessResponse response = request(RequestMethod.PUT, path, params, null);
		return response;
	}

	/**
	 * Make a Klouldless API request to finalize the multipart upload
	 *
	 * @param accountId the account id
	 * @param partId the session id of the multipart upload
	 * @return {@code File}
	 * @throws InvalidRequestException
	 * @throws APIException
	 * @throws APIConnectionException
	 * @throws AuthenticationException
	 */
	public static File finalizeMultipartUpload(String accountId, int partId)
			throws InvalidRequestException, APIException, APIConnectionException,
			AuthenticationException {
		String path = String.format("%s/storage/multipart/%d/complete",
				instanceURL(Account.class, accountId), partId);
		KloudlessResponse response = request(RequestMethod.POST, path, null, null);
		int rCode = response.getResponseCode();
		String rBody = response.getResponseBody();
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return GSON.fromJson(rBody, File.class);
	}

	/**
	 * Make a Kloudless API request to retrive current status of the multipart upload
	 *
	 * @param account the account id
	 * @param partId the session id of the multipart upload
	 * @return {@code File.Multipart}
	 * @throws InvalidRequestException
	 * @throws APIException
	 * @throws APIConnectionException
	 * @throws AuthenticationException
	 */
	public static Multipart retrieveMultipartUploadInfo(String account, int partId)
			throws InvalidRequestException, APIException, APIConnectionException,
			AuthenticationException {

		String path = String.format("%s/storage/multipart/%d",
				instanceURL(Account.class, account), partId);
		KloudlessResponse response = request(RequestMethod.GET, path, null,null);
		int rCode = response.getResponseCode();
		String rBody = response.getResponseBody();
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return GSON.fromJson(rBody, File.Multipart.class);
	}


	/**
	 * Make a Kloudless API request to abort the multipart upload session
	 *
	 * @param account the account id
	 * @param partId the session id of the multipart upload
	 * @return {@code KloudlessResponse}
	 * @throws InvalidRequestException
	 * @throws APIException
	 * @throws APIConnectionException
	 * @throws AuthenticationException
	 */
	public static KloudlessResponse abortMultipartUpload(String account, int partId)
			throws InvalidRequestException, APIException, APIConnectionException,
			AuthenticationException {
		String path = String.format("%s/storage/multipart/%d",
				instanceURL(Account.class, account), partId);
		return delete(path, new Hashtable<>(),null);
	}


	/**
	 * Multipart upload is a POJO for the inforation of multipart upload
	 */
	public class Multipart {

		private int id;
		private String account;
		private long partSize;
		private boolean parallelUploads;
		private long originalFileSize;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public long getPartSize() {
			return partSize;
		}

		public int getPartCount() {
			return (int) Math.ceil((double)this.originalFileSize / getPartSize());
		}

		void setOriginalFileSize(long originalFileSize) {
			this.originalFileSize = originalFileSize;
		}

		public long getOriginalFileSize() {
			return this.originalFileSize;
		}

		public void setPartSize(long partSize) {
			this.partSize = partSize;
		}

		public boolean isParallelUploads() {
			return parallelUploads;
		}

		public void setParallelUploads(boolean parallelUploads) {
			this.parallelUploads = parallelUploads;
		}
	}
}

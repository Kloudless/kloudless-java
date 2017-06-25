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
	 * @param id        - file identifier
	 * @param accountId - account identifier
	 * @param params    - no query parameters for downloading a file
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
	 * @param id        - file identifier
	 * @param accountId - account identifier
	 * @param params    - no query parameters for retrieving file metadata
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
	 * @param params    - The optional parameters include:
	 *                  - parent_id
	 *                  - name
	 *                  - account
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
	 * @param params    - The parameters include:
	 *                  - file - byte array of file
	 *                  - metadata - this is a JSON string of metadata information including
	 *                  - parent_id - identifier of where the file needs to be placed
	 *                  - name - name of the file
	 *                  - overwrite - option true/false of whether you want the file to be overwritten
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
	 * @param params    - The parameters include:
	 *                  - body - byte array of file
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
	 * @param id     - the account id or comma separated account ids.
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
	 * @param params    the parameters include
	 *                  - name        The name of the file to upload
	 *                  - parent_id   The ID of the parent folder to upload the file to
	 *                  - size        The total size of the file being uploaded
	 * @param keys      query parameters
	 *                  - overwrite   overwrite a existing file
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
				instanceURL(Account.class, String.valueOf(accountId)));

		if (keys.containsKey("overwrite")) {
			path += "?overwrite";
		}

		File.Multipart multipart = create(path, params, File.Multipart.class, keys);
		multipart.setOriginalFileSize((Long) params.get("size"));
		return multipart;
	}


	/**
	 * Make a Kloudless API request to upload parts of a file
	 *
	 * @param accountId the account id
	 * @param params    the parameters include
	 *                  - session_id   the session id of the multipart upload
	 *                  - file         {@link java.io.File} to be uploaded
	 *                  - part_size    the size of each part
	 *                  - part_number  the part will be uploaded
	 * @param keys      the keys include
	 *                  - part_number  the part will be uploaded
	 * @return {@code KloudlessResponse}
	 * @throws InvalidRequestException
	 * @throws APIException
	 * @throws APIConnectionException
	 * @throws AuthenticationException
	 */
	public static KloudlessResponse multipartUpload(String accountId,
																									Map<String, Object> params, Map<String, String> keys)
			throws InvalidRequestException, APIException, APIConnectionException,
			AuthenticationException {

		String path = String.format("%s/storage/multipart/%s?part_number=%s",
				instanceURL(Account.class, accountId), String.valueOf(params.get("session_id")),
				String.valueOf(keys.get("part_number")));
		KloudlessResponse response = request(RequestMethod.PUT, path, params, keys);
		return response;
	}

	/**
	 * Make a Kloudless API request to finalize the multipart upload
	 *
	 * @param accountId the account id
	 * @param params    the parameters include
	 *                  - session_id   session_id of the multipart upload
	 * @return {@code File}
	 * @throws InvalidRequestException
	 * @throws APIException
	 * @throws APIConnectionException
	 * @throws AuthenticationException
	 */
	public static File finalizeMultipartUpload(String accountId,
																						 Map<String, Object> params)
			throws InvalidRequestException, APIException, APIConnectionException,
			AuthenticationException {

		String path = String.format("%s/storage/multipart/%s/complete",
				instanceURL(Account.class, accountId), String.valueOf(params.get("session_id")));
		KloudlessResponse response = request(RequestMethod.POST, path, null, null);
		int rCode = response.getResponseCode();
		String rBody = response.getResponseBody();
		if (rCode < 200 || rCode >= 300) {
			handleAPIError(rBody, rCode);
		}
		return GSON.fromJson(rBody, File.class);
	}

	/**
	 * Make a Kloudless API request to retrieve current status of the multipart upload
	 *
	 * @param account the account id
	 * @param params  the parameters include
	 *                - sessions_id the session id of the multipart upload
	 * @return {@code File.Multipart}
	 * @throws InvalidRequestException
	 * @throws APIException
	 * @throws APIConnectionException
	 * @throws AuthenticationException
	 */
	public static Multipart retrieveMultipartUploadInfo(String account,
																											Map<String, Object> params)
			throws InvalidRequestException, APIException, APIConnectionException,
			AuthenticationException {

		String path = String.format("%s/storage/multipart/%s",
				instanceURL(Account.class, account), String.valueOf(params.get("session_id")));
		KloudlessResponse response = request(RequestMethod.GET, path, null, null);
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
	 * @param params  the parameters include
	 *                - session_id  the session id of the multipart upload
	 * @return {@code KloudlessResponse}
	 * @throws InvalidRequestException
	 * @throws APIException
	 * @throws APIConnectionException
	 * @throws AuthenticationException
	 */
	public static KloudlessResponse abortMultipartUpload(String account,
																											 Map<String, Object> params)
			throws InvalidRequestException, APIException, APIConnectionException,
			AuthenticationException {
		String path = String.format("%s/storage/multipart/%s",
				instanceURL(Account.class, account), String.valueOf(params.get("session_id")));
		return delete(path, new Hashtable<>(), null);
	}


	/**
	 * Multipart upload is a POJO for the information of a multipart upload resource
	 */
	public class Multipart {

		private int id;
		private String account;
		private long partSize;
		private boolean parallelUploads;
		private long originalFileSize;

		/**
		 * Returns the id of a new multipart upload resource with which will be used to interact
		 *
		 * @return the id of a new multipart upload resource
		 */
		public int getId() {
			return id;
		}

		/**
		 * Sets the id for a new multipart upload resource
		 *
		 * @param id the id for a new multipart upload resource
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * Returns the account that a multipart upload is associated with.
		 *
		 * @return the account that a multipart upload is associated with.
		 */
		public String getAccount() {
			return account;
		}

		/**
		 * Sets the account to associate with a multipart upload resource
		 *
		 * @param account the account associates with a multipart upload resource
		 */
		public void setAccount(String account) {
			this.account = account;
		}

		/**
		 * Returns a number in <code>long</code> indicating the size of each part
		 * but the last to be uploaded.
		 *
		 * @return the number indicates the size of the each part but the last
		 */
		public long getPartSize() {
			return partSize;
		}

		/**
		 * Sets the size of each part but the last
		 *
		 * @param partSize the size of a each part but the last
		 */
		public void setPartSize(long partSize) {
			this.partSize = partSize;
		}

		/**
		 * Returns a number indicating the count of parts to be uploaded
		 *
		 * @return a number indicating the count of parts to be uploaded
		 */
		public int getPartCount() {
			return (int) Math.ceil((double) this.originalFileSize / getPartSize());
		}

		/**
		 * Returns the original size of a file before uploading
		 *
		 * @return the original size of a file
		 */
		public long getOriginalFileSize() {
			return this.originalFileSize;
		}

		void setOriginalFileSize(long originalFileSize) {
			this.originalFileSize = originalFileSize;
		}

		/**
		 * Returns a boolean value determining whether the chunks can be
		 * uploaded in parallel. If <code>false</code> then each chunks must be uploaded
		 * serially.
		 *
		 * @return <code>true</code> if parallel uploaded is allowed. Otherwise,
		 * chunks must be uploaded serially
		 */
		public boolean isParallelUploads() {
			return parallelUploads;
		}

		/**
		 * Sets if parallel uploading is allowed for chunks
		 *
		 * @param parallelUploads
		 */
		public void setParallelUploads(boolean parallelUploads) {
			this.parallelUploads = parallelUploads;
		}
	}
}

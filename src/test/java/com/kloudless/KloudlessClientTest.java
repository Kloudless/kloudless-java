package com.kloudless;

import static com.kloudless.StaticImporter.TestInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kloudless.exception.KloudlessException;
import com.kloudless.model.Account;
import com.kloudless.model.AccountCollection;
import com.kloudless.model.CRMAccount;
import com.kloudless.model.CRMObject;
import com.kloudless.model.CRMObjectCollection;
import com.kloudless.model.File;
import com.kloudless.model.Folder;
import com.kloudless.model.MetadataCollection;
import com.kloudless.model.Permission;
import com.kloudless.model.PermissionCollection;
import com.kloudless.net.KloudlessResponse;

public class KloudlessClientTest {

	static KClient storageClient = null;
	static KClient crmClient = null;
	static KClient calendarClient = null;	
	static Gson gson = new Gson();
	
	@BeforeClass
	public static void setUp() {
		// bearerToken and accountId for storage account
		storageClient = new KClient("token1", "accountID1", null);

		// bearerToken and accountId for crm account
		crmClient = new KClient("token2", "accountID2", null);

		// bearerToken and accountId for calendar account
		calendarClient = new KClient("token3", "accountID3", null);
	}
	
	@Test
	public void testCoreMethods() throws KloudlessException,
			UnsupportedEncodingException {
		
		// List all accounts (will return 1 because of bearer authorization)
		AccountCollection data = storageClient.all(null, Account.class);

		// Retrieve metadata on connected account
		Account datum = storageClient.retrieve(null, Account.class, null);

		// Update an account's information
		HashMap<String, Object> updateParams = new HashMap<String, Object>();
		updateParams.put("active", true);
		datum = storageClient.update(null, Account.class, null, updateParams);
		
		// Make a raw request using the pass-through API
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("X-KLOUDLESS-RAW-URI", "/drive/v3/files");
		headers.put("X-KLOUDLESS-RAW-METHOD", "GET");		
		KloudlessResponse response = storageClient.authenticatedRequest(
				headers, "POST", null,
				storageClient.formatPath(Account.class, null, "raw"), null,
				null);
		JsonElement rawResponse = new JsonParser().parse(response.getResponseBody());

		// Make a request to the encode_raw_id endpoint
		HashMap<String, Object> encodeParams = new HashMap<String, Object>();
		encodeParams.put("id", "root");
		String path = storageClient.formatPath(Account.class, null, "encode_raw_id");
		KloudlessResponse encodeResponse = storageClient.authenticatedRequest(
				null, "POST", null, path, encodeParams, null);
		JsonElement encodeResponseJson = new JsonParser().parse(encodeResponse
				.getResponseBody());
	
	}
	
	@Test
	public void testStorageMethods() throws KloudlessException,
			UnsupportedEncodingException {

		// Retrieve Folder contents
		MetadataCollection contents = storageClient.contents(null, Folder.class, "root");

		// Create a new folder
		HashMap<String, Object> newFolderParams = new HashMap<String, Object>();
		newFolderParams.put("parent_id", "root");
		newFolderParams.put("name", "newFolderJavaSDK");
		Folder newFolder = storageClient.create(null, Folder.class, newFolderParams);

		// Retrieve folder metadata
		Folder newFolderMetadata = storageClient.retrieve(null, Folder.class,
				newFolder.id);

		// Rename a folder
		newFolderParams.remove("parent_id");
		newFolderParams.put("name", "renamedFolderJavaSDK");
		Folder renamedFolder = storageClient.update(null, Folder.class,
				newFolder.id, newFolderParams);
		
		// Delete a folder
		storageClient.delete(null, Folder.class, renamedFolder.id);
			
		// Upload a file
		File newFile = storageClient.uploadFile("root", "hello.txt", false,
				TestInfo.getPathUploadingFile());
				
		// Retrieve File Permissions
		PermissionCollection permissions = storageClient.listPermissions(newFile.id,
				File.class);		

		// Set File Permissions
		PermissionCollection newPermissions = storageClient.setPermissions(
				newFile.id, File.class, false, permissions);
		
		// Download a file
		KloudlessResponse fileResponse = storageClient.contents(null,
				File.class, newFile.id);
		System.out.println(fileResponse.getResponseStream().size());
		
		// Delete a file
		storageClient.delete(null, File.class, newFile.id);
	}

	@Test
	public void testCrmMethods() throws KloudlessException,
			UnsupportedEncodingException {
		
		// List all CRM Accounts
		CRMObjectCollection accounts = crmClient.all(null, CRMAccount.class);
		System.out.println(accounts);
		
		// Raw type
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("raw_type", "User");
		CRMObjectCollection objects = crmClient.all(queryParams, CRMObject.class);
		System.out.println(objects);	
		
		// Make a CRM search request
		CRMObjectCollection results = crmClient.crmSearch(
				"SELECT Id FROM User", "SOQL", 100, "1");
		System.out.println(results);
		
		// Make a CRM schema request
		CRMObjectCollection schema = crmClient.crmSchema(null, 0, null);
		System.out.println(schema);
		
	}
	
	@Test
	public void testCalendarMethods() throws KloudlessException,
		UnsupportedEncodingException {
		
		// TODO: add calendar support, but the other examples should be sufficient.
	
	}
}

package com.kloudless;

import static com.kloudless.StaticImporter.TestInfo;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kloudless.exception.KloudlessException;
import com.kloudless.model.Account;
import com.kloudless.model.AccountCollection;
import com.kloudless.model.File;
import com.kloudless.model.Folder;
import com.kloudless.model.MetadataCollection;
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
	public void testCoreMethods() throws KloudlessException {
		
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
	}
	
	@Test
	public void testStorageMethods() throws KloudlessException {

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
		java.io.File file = new java.io.File(TestInfo.getPathUploadingFile());
		HashMap<String, Object> newFileParams = new HashMap<String, Object>();
		HashMap<String, String> metadata = new HashMap<String, String>();
		metadata.put("parent_id", "root");
		metadata.put("name", "hello.txt");
		newFileParams.put("metadata", gson.toJson(metadata));
		newFileParams.put("file", file);
		File newFile = storageClient.create(null, File.class, newFileParams);
		
		// Download a file
		KloudlessResponse fileResponse = storageClient.contents(null,
				File.class, newFile.id);
		System.out.println(fileResponse.getResponseStream().size());
		
		// Delete a file
		storageClient.delete(null, File.class, newFile.id);
	}
}

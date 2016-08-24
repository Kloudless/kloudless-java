package com.kloudless;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kloudless.exception.KloudlessException;
import com.kloudless.model.Account;
import com.kloudless.model.AccountCollection;
import com.kloudless.model.EventCollection;
import com.kloudless.model.File;
import com.kloudless.model.FileCollection;
import com.kloudless.model.Folder;
import com.kloudless.model.Token;
import com.kloudless.model.TokenCollection;
import com.kloudless.model.Link;
import com.kloudless.model.LinkCollection;
import com.kloudless.model.MetadataCollection;
import com.kloudless.model.Permission;
import com.kloudless.model.PermissionCollection;
import com.kloudless.model.Property;
import com.kloudless.model.PropertyCollection;
import com.kloudless.model.User;
import com.kloudless.model.UserCollection;
import com.kloudless.model.Group;
import com.kloudless.model.GroupCollection;
import com.kloudless.net.KloudlessResponse;

public class KloudlessTest {

	static Gson GSON = new GsonBuilder().create();
	static ArrayList<String> testAccounts = new ArrayList<String>();

	@BeforeClass
	public static void setUp() {
		Kloudless.apiKey = "INSERT API KEY HERE";

		// Override API Base, now works with http
//		Kloudless.overrideApiBase("http://localhost:8002");

		// Insert Custom Headers
		HashMap<String, String> customHeaders = new HashMap<String, String>();

		// USER IMPERSONATION
//		customHeaders.put("X-Kloudless-As-User", "INSERT USER ID HERE");

		Kloudless.addCustomHeaders(customHeaders);

		// Add Test Accounts
//		testAccounts.add("INSERT TEST ACCOUNTS HERE");
	}
/*
	// Begin Account Tests
	@Test
	public void testAccountAll() throws KloudlessException {
		AccountCollection accounts = Account.all(null);
		System.out.println(accounts);
	}

	@Test
	public void testAccountRetrieve() throws KloudlessException {
		for (String testAccount : testAccounts) {
			Account account = Account.retrieve(testAccount, null);
			System.out.println(account);
		}
	}

	@Test
	public void testAccountDelete() throws KloudlessException {
		// TODO: figure out how to test delete an account
	}

	@Test
	public void testAccountSearch() throws KloudlessException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("q", "test");

		for (String testAccount : testAccounts) {
			MetadataCollection results = Account.search(testAccount, params);
			System.out.println(results);
		}
	}

	@Test
	public void testAccountRecent() throws KloudlessException {
		for (String testAccount : testAccounts) {
			FileCollection results = Account.recent(testAccount, null);
			System.out.println(results);
		}
	}

	@Test
	public void testAccountEvents() throws KloudlessException {
		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("cursor", "after-auth");
		for (String testAccount : testAccounts) {
			EventCollection events = Account.events(testAccount, null);
			System.out.println(events);

			events = Account.events(testAccount, params);
			System.out.println(events);
		}
	}

	// Begin Bearer Token Tests
	@Test
	public void testTokenAll() throws KloudlessException {
		for (String testAccount : testAccounts) {
			TokenCollection tokens = Token.all(testAccount, null);
			System.out.println(tokens);
		}
	}

	@Test
	public void testTokenRetrieve() throws KloudlessException {
		// TODO: add token retrieval test
	}
*/
	// Begin Folder Tests
	@Test
	public void testFolderContents() throws KloudlessException {
		for (String testAccount : testAccounts) {
			MetadataCollection contents = Folder.contents("root", testAccount, null);
			System.out.println(contents);
		}
	}
/*
	@Test
	public void testFolderRetrieve() throws KloudlessException {
		// TODO: add folderRetrieval test
	}

	@Test
	public void testFolderSave() throws KloudlessException {
		// TODO: add folderSave test
	}

	@Test
	public void testFolderCreate() throws KloudlessException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("name", "new new folder");
		params.put("parent_id", "root");

		for (String testAccount : testAccounts) {
			Folder folderInfo = Folder.create(testAccount, params);
			System.out.println(folderInfo);
		}
	}

	@Test
	public void testFolderDelete() throws KloudlessException {
		// TODO: figure out how to test delete a folder
	}

	// Begin File Tests
	@Test
	public void testFileContents() throws KloudlessException, IOException {
//		KloudlessResponse response = File.contents(
//				"fL3N1cHBvcnQtc2FsZXNmb3JjZS5wbmc\u003d", "4", null);

		// For Binary Data
//		String path = "SOME OUTPUT PATH";
//		ByteArrayOutputStream outputStream = response.getResponseStream();
//		FileOutputStream out = new FileOutputStream(path);
//		outputStream.writeTo(out);
//		out.close();

		// For String Data
//		String path = "SOME OUTPUT PATH";
//		String contents = response.getResponseBody();
//		PrintWriter writer = new PrintWriter(path);
//		writer.print(contents);
//		writer.close();
//		System.out.println(contents);
	}

	@Test
	public void testFileRetrieve() throws KloudlessException {
		// TODO: add fileRetrieve test
	}

	@Test
	public void testFileSave() throws KloudlessException {
		// TODO: add fileSave test
	}

	@Test
	// TODO: test
	public void testFileCreate() throws KloudlessException, IOException {

		String text = "Hello, World!";
		String path = "/tmp/new.txt";
		PrintWriter writer = new PrintWriter(path, "UTF-8");
		writer.println(text);
		writer.close();
		java.io.File f = new java.io.File(path);
		Scanner scanner = new Scanner(f);
		String contents = scanner.next();
		scanner.close();

		HashMap<String, Object> params = new HashMap<String, Object>();
		HashMap<String, Object> metadata = new HashMap<String, Object>();
		metadata.put("name", "testtesttest.txt");
		metadata.put("parent_id", "root");
		params.put("metadata", GSON.toJson(metadata));
		params.put("file", contents.getBytes());
		System.out.println(params);

		for (String testAccount : testAccounts) {
			File fileInfo = File.create(testAccount, params);
			System.out.println(fileInfo);
		}
	}

	@Test
	public void testFileDelete() throws KloudlessException {
		// TODO: figure out how to test delete a file
	}

	// Begin Link Tests
	@Test
	public void testLinkAll() throws KloudlessException {
		for (String testAccount : testAccounts) {
			LinkCollection links = Link.all(testAccount, null);
			System.out.println(links);
		}
	}

	@Test
	public void testLinkRetrieve() throws KloudlessException {
		// TODO: figure out how to test retrieve a created link
	}

	@Test
	public void testLinkSave() throws KloudlessException {
		// TODO: figure out how to test save a created link
	}

	@Test
	public void testLinkCreate() throws KloudlessException {
		// TODO: figure out how to test create a link
	}

	@Test
	public void testLinkDelete() throws KloudlessException {
		// TODO: figure out how to test delete a file
	}

	// Begin Permissions Tests
	@Test
	public void testPermissionAll() throws KloudlessException {
//		for (String testAccount : testAccounts) {
//			PermissionCollection permissions = Permission.all("FOLDER ID", 
//					testAccount, "folder", null);
//			PermissionCollection permissions = Permission.all("FILE ID", 
//					testAccount, "file", null);
//			System.out.println(permissions);
//		}
	}

	@Test
	public void testPermissionUpdate() throws KloudlessException {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("EMAIL", "ROLE");
//		
//		for (String testAccount : testAccounts) {
//			PermissionCollection permissions = Permission.update("FOLDER ID", 
//					testAccount, "folder", params);
//			PermissionCollection permissions = Permission.update("FILE ID", 
//					testAccount, "file", params);
//			System.out.println(permissions);
//		}
	}
	
	@Test
	public void testPermissionSave() throws KloudlessException {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("EMAIL", "ROLE");
//		
//		for (String testAccount : testAccounts) {
//			PermissionCollection permissions = Permission.update("FOLDER ID", 
//					testAccount, "folder", params);
//			PermissionCollection permissions = Permission.update("FILE ID", 
//					testAccount, "file", params);
//			System.out.println(permissions);
//		}
	}

	// Begin Properties Tests
	@Test
	public void testPropertiesAll() throws KloudlessException {
		for (String testAccount : testAccounts) {
			PropertyCollection properties = Property.all("FILE ID",
					testAccount, null);
			System.out.println(properties);
		}
	}

	// TODO: include updating properties test

	@Test
	public void testPropertiesDelete() throws KloudlessException {
//		for (String testAccount : testAccounts) {
//			KloudlessResponse response = Property.delete("FILE ID",
//					testAccount, null);
//			System.out.println(response);
//		}
	}

	//Begin Team Endpoint Tests, Admin Account Required
	@Test
	public void testUserAll() throws KloudlessException {
//		UserCollection users = User.all("ADMIN ACCOUNT ID", null);
//		System.out.println(users);
	}
	
	@Test
	public void testUserRetrieve() throws KloudlessException {
//		User user = User.retrieve("USER ID", "ADMIN ACCOUNT ID", null);
//		System.out.println(user);
	}
	
	@Test
	public void testUserGroups() throws KloudlessException {
//		GroupCollection groups = User.groups("USER ID", "ADMIN ACCOUNT ID", null);
//		System.out.println(groups);
	}
	
	@Test
	public void testGroupAll() throws KloudlessException {
//		GroupCollection groups = Group.all("ADMIN ACCOUNT ID", null);
//		System.out.println(groups);
	}
	
	@Test
	public void testGroupRetrieve() throws KloudlessException {
//		Group group = Group.retrieve("GROUP ID", "ADMIN ACCOUNT ID", null);
//		System.out.println(group);
	}
	
	@Test
	public void testGroupUsers() throws KloudlessException {
//		UserCollection users = Group.users("GROUP ID", "ADMIN ACCOUNT ID", null);
//		System.out.println(users);
	}
	*/
}
package com.kloudless;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kloudless.exception.KloudlessException;
import com.kloudless.model.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.kloudless.StaticImporter.TestInfo;
import static org.assertj.core.api.Assertions.assertThat;


public class KloudlessTest extends KloudlessBaseTest {

	static Gson GSON = new GsonBuilder().create();
	static List<String> testAccounts;

	@BeforeClass
	public static void setUp() {
		Kloudless.apiKey = TestInfo.getApiKey();
		testAccounts = TestInfo.getTestAccounts();
		Kloudless.overrideApiBase(TestInfo.getApiBasedUrl());

		// Insert Custom Headers
		HashMap<String, String> customHeaders = new HashMap<String, String>();

		// USER IMPERSONATION
    // customHeaders.put("X-Kloudless-As-User", "INSERT USER ID HERE");
		Kloudless.addCustomHeaders(customHeaders);
	}

	// Begin Account Tests
	@Test
	public void testAccountAll() throws KloudlessException {
		AccountCollection accounts = Account.all(null);
		List<Account> list = accounts.objects.stream().filter(x -> testAccounts
        .contains(x.id)).collect(Collectors.toList());
		assertThat(list.size()).isGreaterThan(0);
	}

	@Test
	public void testAccountRetrieve() throws KloudlessException {
		for (String testAccount : testAccounts) {
			Account account = Account.retrieve(testAccount, null);
			String jsonTree = TestInfo.getOneTestAccountJson();
      try {
        JsonNode root = mapper.readTree(jsonTree);
        int id = root.at("/id").asInt();
        assertThat(Integer.parseInt(account.id)).isEqualTo(id);
        String acnt = root.at("/account").asText();
        assertThat(account.account).isEqualTo(acnt);
        String service = root.at("/service").asText();
        assertThat(account.service).isEqualTo(service);
      } catch (IOException e) {
        e.printStackTrace();
      }
		}
	}

	@Test
  @Ignore
	public void testAccountDelete() throws KloudlessException {
		// TODO: figure out how to test delete an account
	}

	@Test
	public void testAccountSearch() throws KloudlessException {
	  HashMap<String, Object> params = new HashMap<String, Object>();
	  final String folderName = "new test folder";

		for (String testAccount : testAccounts) {
      params.put("name",folderName);
      params.put("parent_id", "root");
		  Folder.create(testAccount, params);

      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      params.clear();
      params.put("q", folderName);
			MetadataCollection results = Account.search(testAccount, params);
			if(results.count > 0) {
			  results.objects.stream().forEach(x -> {
			    assertThat(x.name).isEqualTo(folderName);
        });
      }
		}
	}

	@Test
	public void testAccountRecent() throws KloudlessException {
	  HashMap<String, Object> params = new HashMap<>();
	  HashMap<String, Object> metadata = new HashMap<>();
	  final String fileName = "test recent file";

		for (String testAccount : testAccounts) {
      metadata.put("name", fileName);
      metadata.put("parent_id", "root");
      try {
        params.put("metadata", mapper.writeValueAsString(metadata));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      params.put("file", "Hello, World!".getBytes());
      params.put("name",fileName);
      params.put("parent_id", "root");
		  File.create(testAccount, params);

      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

			FileCollection results = Account.recent(testAccount, null);
      List<Metadata> list = results.objects.stream().filter(x->x.name.equals(fileName))
          .collect(Collectors.toList());
      assertThat(list.size()).isGreaterThan(0);
		}
	}

	@Test
  @Ignore
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

	@Test
  @Ignore
	public void testTokenRetrieve() throws KloudlessException {
		// TODO: add token retrieval test
	}

	// Begin Folder Tests
	@Test
  @Ignore
	public void testFolderContents() throws KloudlessException {
		for (String testAccount : testAccounts) {
			MetadataCollection contents = Folder.contents("root", testAccount, null);
			System.out.println(contents);
		}
	}

	@Test
  @Ignore
	public void testFolderRetrieve() throws KloudlessException {
		// TODO: add folderRetrieval test
	}

	@Test
  @Ignore
	public void testFolderSave() throws KloudlessException {
		// TODO: add folderSave test
	}

	@Test
  @Ignore
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
  @Ignore
	public void testFolderDelete() throws KloudlessException {
		// TODO: figure out how to test delete a folder
	}

	// Begin File Tests
	@Test
  @Ignore
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
  @Ignore
	public void testFileRetrieve() throws KloudlessException {
		// TODO: add fileRetrieve test
	}

	@Test
  @Ignore
	public void testFileSave() throws KloudlessException {
		// TODO: add fileSave test
	}

	@Test
  @Ignore
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
  @Ignore
	public void testFileDelete() throws KloudlessException {
		// TODO: figure out how to test delete a file
	}

	// Begin Link Tests
	@Test
  @Ignore
	public void testLinkAll() throws KloudlessException {
		for (String testAccount : testAccounts) {
			LinkCollection links = Link.all(testAccount, null);
			System.out.println(links);
		}
	}

	@Test
  @Ignore
	public void testLinkRetrieve() throws KloudlessException {
		// TODO: figure out how to test retrieve a created link
	}

	@Test
  @Ignore
	public void testLinkSave() throws KloudlessException {
		// TODO: figure out how to test save a created link
	}

	@Test
  @Ignore
	public void testLinkCreate() throws KloudlessException {
		// TODO: figure out how to test create a link
	}

	@Test
  @Ignore
	public void testLinkDelete() throws KloudlessException {
		// TODO: figure out how to test delete a file
	}

	// Begin Permissions Tests
	@Test
  @Ignore
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
  @Ignore
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
  @Ignore
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
  @Ignore
	public void testPropertiesAll() throws KloudlessException {
		for (String testAccount : testAccounts) {
			PropertyCollection properties = Property.all("FILE ID",
					testAccount, null);
			System.out.println(properties);
		}
	}

	// TODO: include updating properties test

	@Test
  @Ignore
	public void testPropertiesDelete() throws KloudlessException {
//		for (String testAccount : testAccounts) {
//			KloudlessResponse response = Property.delete("FILE ID",
//					testAccount, null);
//			System.out.println(response);
//		}
	}

	//Begin Team Endpoint Tests, Admin Account Required
	@Test
  @Ignore
	public void testUserAll() throws KloudlessException {
//		UserCollection users = User.all("ADMIN ACCOUNT ID", null);
//		System.out.println(users);
	}
	
	@Test
  @Ignore
	public void testUserRetrieve() throws KloudlessException {
//		User user = User.retrieve("USER ID", "ADMIN ACCOUNT ID", null);
//		System.out.println(user);
	}
	
	@Test
  @Ignore
	public void testUserGroups() throws KloudlessException {
//		GroupCollection groups = User.groups("USER ID", "ADMIN ACCOUNT ID", null);
//		System.out.println(groups);
	}
	
	@Test
  @Ignore
	public void testGroupAll() throws KloudlessException {
//		GroupCollection groups = Group.all("ADMIN ACCOUNT ID", null);
//		System.out.println(groups);
	}
	
	@Test
  @Ignore
	public void testGroupRetrieve() throws KloudlessException {
//		Group group = Group.retrieve("GROUP ID", "ADMIN ACCOUNT ID", null);
//		System.out.println(group);
	}
	
	@Test
  @Ignore
	public void testGroupUsers() throws KloudlessException {
//		UserCollection users = Group.users("GROUP ID", "ADMIN ACCOUNT ID", null);
//		System.out.println(users);
	}
}
package com.kloudless;

import com.fasterxml.jackson.databind.JsonNode;
import com.kloudless.exception.*;
import com.kloudless.model.*;
import com.kloudless.net.KloudlessResponse;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kloudless.StaticImporter.TestInfo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.filter;

public class KloudlessTest extends KloudlessBaseTest {

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
	public void testAccountSearch() throws KloudlessException {
	  final String folderName = "new test fold\u00e9r";
		for (String testAccount : testAccounts) {
		  createTestFolder(folderName, getRootFolderId(testAccount), testAccount);
      Map<String, Object> params = new HashMap<>();
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
	public void testAccountRecent() throws KloudlessException, IOException {
		java.io.File file1 =  new java.io.File(TestInfo.getPathUploadingFile());
		final String fileName = file1.getName();
		for (String testAccount : testAccounts) {
		  createTestFile(fileName, file1, getRootFolderId(testAccount), testAccount);
			FileCollection results = Account.recent(testAccount, null);
      List<Metadata> list = results.objects.stream().filter(x->x.name.equals(fileName))
          .collect(Collectors.toList());
      assertThat(list.size()).isGreaterThan(0);
		}
	}

	@Test
  @Ignore
	public void testAccountEvents() throws KloudlessException {
		HashMap<String, Object> params = new HashMap<>();
		//params.put("cursor", "after-auth");
		for (String testAccount : testAccounts) {
			EventCollection events = Account.events(testAccount, null);
			if(events.count > 0) {
			  //TODO: add asserts later if the event retrieval works

      }

			events = Account.events(testAccount, params);
			if(events.count > 0) {
        //TODO: add asserts later if the event retrieval works
      }
		}
	}

	// Begin Folder Tests
	@Test
	public void testFolderContents() throws KloudlessException {
	  final String fileName = "test folder content";
		for (String testAccount : testAccounts) {
		  createTestFolder(fileName, getRootFolderId(testAccount), testAccount);
      
      MetadataCollection contents = Folder.contents(getRootFolderId(testAccount)
          , testAccount, null);
      List<Metadata> list = contents.objects.stream().filter(x -> x.name.equals(fileName))
          .collect(Collectors.toList());

      // greater one is for fear of the duplication
      assertThat(list.size() >= 1);
		}
	}

	@Test
	public void testFolderRetrieve() throws KloudlessException {
    final String folderName = "test folder retrieve";
    for(String testAccount : testAccounts) {
      Folder createdFolder = createTestFolder(folderName,
          getRootFolderId(testAccount), testAccount);
      
      Folder retrieved = Folder.retrieve(createdFolder.id, testAccount, null);
      assertThat(createdFolder.name).isEqualTo(retrieved.name);
      assertThat(createdFolder.id).isEqualTo(retrieved.id);
    }
	}

	@Test
	public void testFolderSave() throws KloudlessException {
    final String folderName = "test folder save";
    final String folderNameChanged = "test folder saf\u009e";
    for(String testAccount : testAccounts) {

      Folder createdFolder = createTestFolder(folderName,
          getRootFolderId(testAccount), testAccount);
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      createdFolder.name = folderNameChanged;
      Map<String,Object> metadata = new HashMap<>();
      metadata.put("parent_id",getRootFolderId(testAccount));
      metadata.put("name",folderNameChanged);
      Folder folderNamedChanged = Folder.save(createdFolder.id, testAccount, metadata);
      
      Folder retrieved = Folder.retrieve(folderNamedChanged.id, testAccount, null);
      assertThat(retrieved.name).isEqualTo(folderNameChanged);
    }
	}

	@Test
	public void testFolderCreate() throws KloudlessException {
	  final String folderName = "test create a folder";
		for (String testAccount : testAccounts) {
	    Folder newFolder = createTestFolder(folderName,
          getRootFolderId(testAccount) ,testAccount);
	    assertThat(folderName).isEqualTo(newFolder.name);
		}
	}

	@Test
	public void testFolderDelete() throws KloudlessException {
	  final String folderName = "test folder deletion";
	  for(String account : testAccounts) {
	    Folder newFolder = createTestFolder(folderName,
          getRootFolderId(account) , account);
      
      int code = deleteTestFolder(newFolder.id, account, false, true);
      assertThat(code).isEqualTo(204);
    }
	}

	// Begin File Tests
	@Test
	public void testFileContents() throws KloudlessException, IOException {

	  // Binary file
		java.io.File file1 =  new java.io.File(TestInfo.getPathUploadingFile());
    long expectedSize = file1.length();
    for(String account : testAccounts ){
      File fileCreated = createTestFile("test file content by checking the size",
          file1, getRootFolderId(account), account);
      
      KloudlessResponse response = File.contents(fileCreated.id, account,null);
      long actualSize = response.getResponseStream().size();
      assertThat(actualSize).isEqualTo(expectedSize);
    }
    //TODO: text file
	}

	@Test
	public void testFileRetrieve() throws KloudlessException {
	  final String fileName = "test retrieving file";
	  for(String account : testAccounts) {
	    File createdFile = null;
	    java.io.File file = new java.io.File(TestInfo.getPathUploadingFile());
      try {
        createdFile = createTestFile(fileName, file , getRootFolderId(account), account);
      } catch (IOException e) {
        e.printStackTrace();
      }

      File retrievedFile = File.retrieve(createdFile.id, account,null);
      assertThat(retrievedFile.name).isEqualTo(fileName);
    }
	}

	@Test
	public void testFileSave() throws KloudlessException, IOException {
	  final String fileName = "test update a file";
	  final String fileNameChanged = "file name is changed";
		java.io.File file = new java.io.File(TestInfo.getPathUploadingFile());
	  for(String account : testAccounts) {
	    File fileCreated = createTestFile(fileName, file, getRootFolderId(account),
			    account);

      Map<String,Object> params = new HashMap<>();
      params.put("name", fileNameChanged);
      params.put("parent_id", getRootFolderId(account));
      params.put("account", account);

      File.save(fileCreated.id, account, params);

      File updatedFile = File.retrieve(fileCreated.id, account, null);
      assertThat(updatedFile.name).isEqualTo(fileNameChanged);
    }

	}

	@Test
	public void testFileCreate() throws KloudlessException, IOException {
	  final String fileName = "kloudless.key";
    java.io.File file1 = new java.io.File(TestInfo.getPathUploadingFile());
    final long size = file1.length();
	  for(String account : testAccounts) {
	    File fileCreated = createTestFile(fileName, file1, getRootFolderId(account),
          account);

      File fileRetrieved = File.retrieve(fileCreated.id, account, null);
      assertThat(fileName).isEqualTo(fileRetrieved.name);
      assertThat(fileRetrieved.size).isEqualTo(size);
    }
	}

	@Test
	public void testFileDelete() throws KloudlessException, IOException {
    final String fileName = "test file deletion";
		java.io.File file1 = new java.io.File(TestInfo.getPathUploadingFile());
    final long size = file1.length();
	  for(String account : testAccounts) {
	    File fileCreated = createTestFile(fileName, file1, getRootFolderId(account),
          account);

      KloudlessResponse response = File.delete(fileCreated.id, account,null);
      int code = response.getResponseCode();
      assertThat(code).isEqualTo(204);
    }
	}

	@Test
	public void testMultipartUpload() throws APIException, AuthenticationException,
			InvalidRequestException, APIConnectionException {

		java.io.File file =  new java.io.File(TestInfo.getPathUploadingFile());
		HashMap<String, Object> params = new HashMap<>();
		HashMap<String, String> keys = new HashMap<>();

    for(String account : testAccounts) {
    	params.clear();
    	keys.clear();

	    params.put("name", file.getName());
	    params.put("parent_id", getRootFolderId(account));
	    params.put("size", file.length());

	    keys.put("overwrite","true");

	    File.Multipart multipart = File.initializeMultipartUpload(account, params, keys);
	    assertThat(multipart.getId() > 0);
	    assertThat(multipart.getAccount()).isEqualTo(account);
	    assertThat(multipart.getPartSize()).isGreaterThan(0);
	    assertThat(multipart.getPartCount()).isEqualTo(
			    (int)Math.ceil((double)multipart.getOriginalFileSize()/multipart.getPartSize()));

	    assertThat(multipart.getPartCount()).isGreaterThanOrEqualTo(1);
	    assertThat(multipart.getPartCount()).isLessThanOrEqualTo(10000);

	    for(int part_number = 1; part_number <= multipart.getPartCount(); part_number++) {
		    params.clear();
		    keys.clear();

		    params.put("session_id", String.valueOf(multipart.getId()));
		    params.put("file", file);
		    params.put("part_number", part_number);
		    params.put("part_size", multipart.getPartSize());

		    keys.put("part_number", String.valueOf(part_number));

		    KloudlessResponse response = File.multipartUpload(account, params, keys);
		    assertThat(response.getResponseCode()).isEqualTo(200);

		    if(part_number == 1) { // test retrieve multipart upload status
			    File.Multipart multipartStatus = File.retrieveMultipartUploadInfo(account, params);
			    assertThat(multipartStatus.getId()).isEqualTo(multipart.getId());
			    assertThat(multipartStatus.getAccount()).isEqualTo(multipart.getAccount());
		    }
		    
	    }
	    File mergedFile = File.finalizeMultipartUpload(account, params);
	    assertThat(mergedFile.name).isEqualTo(file.getName());
	    assertThat(mergedFile.size).isEqualTo(file.length());

    }
	}

	@Test
	public void testAbortMultipartUplad() throws APIException,
			InvalidRequestException, AuthenticationException, APIConnectionException {

		java.io.File file =  new java.io.File(TestInfo.getPathUploadingFile());
		HashMap<String, Object> params = new HashMap<>();
		HashMap<String, String> keys = new HashMap<>();

		for(String account : testAccounts) {
			params.clear();
			keys.clear();;
			params.put("name", file.getName());
			params.put("parent_id", getRootFolderId(account));
			params.put("size", file.length());

			keys.put("overwrite","true");

			File.Multipart multipart = File.initializeMultipartUpload(account, params, keys);
			assertThat(multipart.getId() > 0);
			assertThat(multipart.getAccount()).isEqualTo(account);
			assertThat(multipart.getPartSize()).isGreaterThan(0);
			assertThat(multipart.getPartCount()).isEqualTo(
					(int)Math.ceil((double)multipart.getOriginalFileSize()/multipart.getPartSize()));

			assertThat(multipart.getPartCount()).isGreaterThanOrEqualTo(1);
			assertThat(multipart.getPartCount()).isLessThanOrEqualTo(10000);
			
			params.clear();
			params.put("session_id", multipart.getId());

			KloudlessResponse response = File.abortMultipartUpload(account, params);
			assertThat(response.getResponseCode()).isEqualTo(204);
		}
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

package com.kloudless;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kloudless.exception.KloudlessException;
import com.kloudless.model.*;
import com.kloudless.net.KloudlessResponse;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.kloudless.StaticImporter.TestInfo;
import static org.assertj.core.api.Assertions.assertThat;

public class KloudlessEventsTest {

	static Gson GSON = new GsonBuilder().create();
	static List<String> accounts = new ArrayList<>();
	HashMap<String, Object> params = new HashMap<>();
	HashMap<String, Object> metadata = new HashMap<>();
	
	@BeforeClass
	public static void setUp() {
	  Kloudless.overrideApiBase(TestInfo.getApiBasedUrl());
		Kloudless.apiKey = TestInfo.getApiKey();
		accounts = TestInfo.getTestAccounts();
	}

	@Test
	public void testCreateEvents() throws KloudlessException,
			FileNotFoundException, UnsupportedEncodingException, InterruptedException {
		for (String account : accounts) {

			// Create Folder
			params.clear();
			params.put("name", "create folder event");
			params.put("parent_id", "root");
			Folder createdFolder = Folder.create(account, params);
			assertThat(createdFolder.id).isNotEmpty();
			assertThat(createdFolder.parent.Id).isEqualTo("root");
			assertThat(createdFolder.name).isEqualTo("create folder event");

			// Create Nested Folder
			params.clear();
			params.put("name", "create nested folder event");
			params.put("parent_id", createdFolder.id);
			Folder nestedFolder = Folder.create(account, params);
			assertThat(nestedFolder.id).isNotEmpty();
			assertThat(nestedFolder.parent.Id).isEqualTo(createdFolder.id);
			assertThat(nestedFolder.name).isEqualTo("create nested folder event");
			
			// Rename Nested Folder
			params.clear();
			params.put("name", "rename nested folder event");
			Folder renamedNestedFolder = Folder.save(nestedFolder.id, account, params);
			assertThat(renamedNestedFolder.name).isEqualTo("rename nested folder event");
			
			// Delete Nested Folder
			params.clear();
			KloudlessResponse resp = Folder.delete(renamedNestedFolder.id, account, null);
			assertThat(resp.getResponseCode()).isEqualTo(204);

			// Create File
			params.clear();
			metadata.clear();
			metadata.put("name", "create_file_event.txt");
			metadata.put("parent_id", "root");
			params.put("metadata", GSON.toJson(metadata));
			params.put("file", "Hello, World!".getBytes());
			File createdFile = File.create(account, params);
			assertThat(createdFile.id).isNotEmpty();
			assertThat(createdFile.name).isEqualTo("create_file_event.txt");
			
			// Update File
			params.clear();
			params.put("body", "Hello, World! Hello, World!".getBytes());
			File updatedFile = File.update(createdFile.id, account, params);
			assertThat(updatedFile.size).isGreaterThan(createdFile.size);

			// Create Nested File
			params.clear();
			metadata.clear();
			metadata.put("name", "create_nested_file_event.txt");
			metadata.put("parent_id", createdFolder.id);
			params.put("metadata", GSON.toJson(metadata));
			params.put("file", "Hello, World!".getBytes());
			File nestedFile = File.create(account, params);
			assertThat(nestedFile.id).isNotEmpty();
			assertThat(createdFolder.id).isEqualTo(nestedFile.parent.Id);

			// Copy Nested File
			params.clear();
			params.put("parent_id", createdFolder.id);
			params.put("name", "copy_nested_file_event.txt");
			File copyNestedFile = File.copy(nestedFile.id, account, params);
			assertThat(copyNestedFile.parent.Id).isEqualTo(createdFolder.id);
			assertThat(copyNestedFile.id).isNotEqualTo(nestedFile.id);
			
			// Move Copied Nested File
			params.clear();
			params.put("parent_id", "root");
			File movedCopiedNestedFile = File.save(copyNestedFile.id, account, params);
			assertThat(movedCopiedNestedFile.parent.Id).isEqualTo("root");
			
			// Rename Nested File
			params.clear();
			params.put("name", "rename_nested_file.txt");
			File renamedNestedFile = File.save(nestedFile.id, account, params);
			assertThat(renamedNestedFile.name).isEqualTo("rename_nested_file.txt");
			
			// Delete Nested File
			params.clear();
			resp = File.delete(renamedNestedFile.id, account, null);
			assertThat(resp.getResponseCode()).isEqualTo(204);
		}
	}

	// Test Recent Files
	@Test
	public void testAccountRecent() throws KloudlessException {
		for (String account : accounts) {
			FileCollection results = Account.recent(account, null);
			if(results.count > 0) {
        System.out.println("There are " + results.count + " files or folders" +
            " belonging to account " + account + " recently changed!");
        assertThat(results.total).isEqualTo(results.objects.size());
        results.objects.stream().forEach(x -> {
          assertThat(x.account).isEqualTo(Long.valueOf(account));
        });
			}
		}
	}

	// Test Events
	@Test
	public void testEventsRetrieval() throws KloudlessException {
		for (String account : accounts) {
		  params.clear();
			EventCollection events = Account.events(account, params);
			//TODO: will test later if getting better understanding of event on backend side
			if(events.count > 0) {
			  System.out.println("There are " + events.count + " happened belonging to account " + account);
			}
		}
	}
}
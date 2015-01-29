package com.kloudless;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kloudless.exception.KloudlessException;
import com.kloudless.model.Account;
import com.kloudless.model.EventCollection;
import com.kloudless.model.File;
import com.kloudless.model.FileCollection;
import com.kloudless.model.Folder;

public class KloudlessEventsTest {

	static Gson GSON = new GsonBuilder().create();
	static ArrayList<String> accounts = new ArrayList<String>();
	HashMap<String, Object> params = new HashMap<String, Object>();
	HashMap<String, Object> metadata = new HashMap<String, Object>();
	
	@BeforeClass
	public static void setUp() {
		Kloudless.apiKey = "INSERT API KEY HERE";
		
		// Google Drive
//		accounts.add("1");
		// OneDrive
//		accounts.add("2");
		// Box
//		accounts.add("3");
		// Dropbox
//		accounts.add("4");
	}

	@Test
	public void testCreateEvents() throws KloudlessException, FileNotFoundException, UnsupportedEncodingException, InterruptedException {
		for (String account : accounts) {

			// Create Folder
			params.clear();
			params.put("name", "create folder event");
			params.put("parent_id", "root");
			Folder createdFolder = Folder.create(account, params);
			
			// Create Nested Folder
			params.clear();
			params.put("name", "create nested folder event");
			params.put("parent_id", createdFolder.id);
			Folder nestedFolder = Folder.create(account, params);
			
			// Rename Nested Folder
			params.clear();
			params.put("name", "rename nested folder event");
			Folder renamedNestedFolder = Folder.save(nestedFolder.id, account, params);
			
			// Delete Nested Folder
			params.clear();
			Folder.delete(renamedNestedFolder.id, account, null);
			
			// Create File
			params.clear();
			metadata.clear();
			metadata.put("name", "create_file_event.txt");
			metadata.put("parent_id", "root");
			params.put("metadata", GSON.toJson(metadata));
			params.put("file", "Hello, World!".getBytes());

			File createdFile = File.create(account, params);
			
			// Update File
			params.clear();
			params.put("body", "Hello, World! Hello, World!".getBytes());
			File updatedFile = File.update(createdFile.id, account, params);

			// Create Nested File
			params.clear();
			metadata.clear();
			metadata.put("name", "create_nested_file_event.txt");
			metadata.put("parent_id", createdFolder.id);
			params.put("metadata", GSON.toJson(metadata));
			params.put("file", "Hello, World!".getBytes());

			File nestedFile = File.create(account, params);

			// Copy Nested File
			params.clear();
			params.put("parent_id", createdFolder.id);
			params.put("name", "copy_nested_file_event.txt");
			File copyNestedFile = File.copy(nestedFile.id, account, params);
			
			// Move Copied Nested File
			params.clear();
			params.put("parent_id", "root");
			File movedCopiedNestedFile = File.save(copyNestedFile.id, account, params);
			
			// Rename Nested File
			params.clear();
			params.put("name", "rename_nested_file.txt");
			File renamedNestedFile = File.save(nestedFile.id, account, params);
			
			// Delete Nested File
			params.clear();
			File.delete(renamedNestedFile.id, account, null);
		}
		
	}

	// Test Recent Files
	@Test
	public void testAccountRecent() throws KloudlessException {
		for (String account : accounts) {
			FileCollection results = Account.recent(account, null);
			System.out.println(results);
		}
	}

	// Test Events
	@Test
	public void testAccountEvents() throws KloudlessException {
		for (String account : accounts) {
			params.clear();
//			params.put("cursor", 11179); // gdrive
//			params.put("cursor", 11715); // onedrive
//			params.put("cursor", 11196); // box
//			params.put("cursor", 11666); // dropbox
			
			EventCollection events = Account.events(account, params);
			System.out.println(events);
		}
	}
}
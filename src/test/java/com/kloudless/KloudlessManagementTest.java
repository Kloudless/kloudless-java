package com.kloudless;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.kloudless.model.Link;
import com.kloudless.model.LinkCollection;
import com.kloudless.model.MetadataCollection;
import com.kloudless.model.Permission;
import com.kloudless.model.Application;
import com.kloudless.model.ApplicationCollection;
import com.kloudless.model.APIKey;
import com.kloudless.model.APIKeyCollection;
import com.kloudless.model.KloudlessCollection;
import com.kloudless.net.KloudlessResponse;


public class KloudlessManagementTest {
	
	static Gson GSON = new GsonBuilder().create();
	static ArrayList<String> testAccounts = new ArrayList<String>();

	@BeforeClass
	public static void setUp() {
		Kloudless.developerKey = "INSERT DEVELOPER KEY HERE";

		// Override API Base, now works with http
//		Kloudless.overrideApiBase("http://localhost:8002");

		// Insert Custom Headers
		HashMap<String, String> customHeaders = new HashMap<String, String>();
		Kloudless.addCustomHeaders(customHeaders);
	}
	
	// Begin Management Tests
	@Test
	public void testApplicationAll() throws KloudlessException {
		ApplicationCollection applications = Application.all(null);
		System.out.println(applications);
	}
	
	@Test
	public void testManagementCreate() throws KloudlessException {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("name", "NAME OF NEW APP");
//		params.put("description", "DESCRIPTION OF NEW APP");
//		params.put("logo_url", "LOGO URL OF NEW APP");
//		Application newApp = Application.create(params);
//		System.out.println(newApp);
	}
	
	@Test
	public void testApplicationRetrieve() throws KloudlessException {
//		Application application = Application.retrieve("APP ID", null);
//		System.out.println(application);
	}
	
	@Test
	public void testApplicationUpdate() throws KloudlessException {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("name", "NEW NAME");
//		params.put("description", "NEW DESCRIPTION");
//		params.put("logo_url", "NEW LOGO URL");
//		Application application = Application.update("APP ID", params);
//		System.out.println(application);
	}
	
	@Test
	public void testApplicationDelete() throws KloudlessException {
//		KloudlessResponse response = Application.delete("APP ID", null);
//		System.out.println(response.getResponseBody());
	}
	
	@Test
	public void testAPIKeysAll() throws KloudlessException {
//		APIKeyCollection APIKeys = APIKey.all("APP ID", null);
//		System.out.println(APIKeys);
	}
	
	@Test
	public void testAPIKeysCreate() throws KloudlessException {
//		APIKey newKey = APIKey.create("APP ID", null);
//		System.out.println(newKey);
	}
	
	@Test
	public void testAPIKeysDelete() throws KloudlessException {
//		KloudlessResponse response = APIKey.delete("API KEY", "APP ID", null);
//		System.out.println(response.getResponseBody());
	}
}

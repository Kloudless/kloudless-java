package com.kloudless;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kloudless.exception.APIConnectionException;
import com.kloudless.exception.APIException;
import com.kloudless.exception.AuthenticationException;
import com.kloudless.exception.InvalidRequestException;
import com.kloudless.model.File;
import com.kloudless.model.Folder;
import org.junit.BeforeClass;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gchiou on 09/06/2017.
 */
public abstract class KloudlessBaseTest {

  protected static ObjectMapper mapper;
  private static Object lck = new Object();
  private volatile static Folder rootFolder = null;

  @BeforeClass
  public static void baseSetup() {
    synchronized (lck) {
      if(mapper == null) {
        mapper = new ObjectMapper();
      }
    }
  }

  protected synchronized String getRootFolderId(String testAccount) throws APIException,
      AuthenticationException, InvalidRequestException, APIConnectionException {
    if(rootFolder == null) {
      final String sdfPattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
      SimpleDateFormat sdf = new SimpleDateFormat(sdfPattern);
      String rootFolderName = String.format("KloudlessJavaSDK-Storage-Test-%s",
          sdf.format(new Date()));
      rootFolder = createTestFolder(rootFolderName, "root", testAccount);
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return rootFolder.id;
  }


  protected int deleteTestFile(final String id,
                                 final String testAccount,
                                 final boolean permanent) throws APIException,
      AuthenticationException, InvalidRequestException, APIConnectionException {
    Map<String, Object> params = new HashMap<>();
    params.put("permanent", permanent);
    return File.delete(id, testAccount, params).getResponseCode();
  }

  protected int deleteTestFolder(final String id, final String testAccount,
                                 final boolean recusive, final boolean permanent)
      throws APIException, AuthenticationException, InvalidRequestException,
      APIConnectionException {
    Map<String, Object> params = new HashMap<>();
    params.put("permanent", permanent);
    params.put("recursive", recusive);
    return Folder.delete(id, testAccount, params).getResponseCode();
  }

  protected Folder createTestFolder(final String folderName,
                                  final String parentId,
                                  final String testAaccount) throws APIException,
      AuthenticationException, InvalidRequestException, APIConnectionException {
    Map<String, Object> params = new HashMap<>();
    System.out.println("creating a folder called \"" + folderName + "\"");
    params.put("name", folderName);
    params.put("parent_id", parentId);
    Folder createdFolder = Folder.create(testAaccount, params);
    return createdFolder;
  }


  protected File createTestFile(final String fileName,
                                final java.io.File file,
                                final String parentId,
                                final String testAccount)
      throws APIException, AuthenticationException, InvalidRequestException,
      APIConnectionException, IOException {
    HashMap<String, Object> params = new HashMap<>();
    HashMap<String, Object> metadata = new HashMap<>();
    //TODO: logger
    System.out.println("creating a file called \"" + fileName +"\"");
    metadata.put("name", fileName);
    metadata.put("parent_id", parentId);
    try {
      params.put("metadata", mapper.writeValueAsString(metadata));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    params.put("file", file);
    File fileCreated = File.create(testAccount, params);
    return fileCreated;
  }
}

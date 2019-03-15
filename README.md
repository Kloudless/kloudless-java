# Kloudless API Java Bindings

You can sign up for a Kloudless Developer account at https://kloudless.com.

# Table of Contents
* [Requirements](#requirements)
* [Installation](＃installation)
* [Getting Started](#getting-started)
    * [Obtain bearer token](#obtain-bearer-token)
    * [Initialize an Account](#initialize-an-account)
        * [What is an Account](#what-is-an-account)
    * [Make API reqeusts](#make-api-requests)
        * [How to get Resource and ResourceList](#how-to-get-resource-and-resourcelist)
        * [Resource](#resource)
            * [Basic usages](#basic-usages)
            * [Resource supports file opeartion](#resource-supports-file-opeartion)
            * [Get RawData from upstream service](#get-rawdata-from-upstream-service)
        * [ResourceList](#resourcelist)
            * [Pagination of ResourceList](#pagination-of-resourcelist)
            * [Autopagination of ResourceList](#autopagination-of-resourcelist)
        * [ResponseRaw](#responseraw)
    * [Other usage of Account](#other-usage-of-account)
        * [Pass-through API](#pass-through-api)
* [Build](#build)


# Requirements

Java 1.8 and later.

# Installation

### Maven

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>com.kloudless</groupId>
    <artifactId>kloudless-java</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Gradle

Add this dependency to your project's build file:

```groovy
api 'com.kloudless:kloudless-java:2.0.0'
```

### Others

You'll need to manually install the Kloudless JAR, either by compiling it or
downloading it from the [Releases page](https://github.com/Kloudless/kloudless-java/releases/)

# Getting Started

## Obtain bearer token

API requests to Kloudless require Bearer Token Authentication.  We will describe two ways to obtain 
the Bearer Token via OAuth 2.0.

### Out-of-band OAuth Flow
1. Find the Application ID (App ID) located [here](https://developers.kloudless.com/applications/*/details) 
and copy it to your clipboard.
2. Visit the following URL, but replace APPLICATION_ID with the APP ID in your clipboard: 
https://api.kloudless.com/v1/oauth/?&scope=any.storage&client_id=APPLICATION_ID&response_type=token&state=13373
3. After granting access to a specific cloud service, you should be able to obtain an access token 
(A.K.A Secret Token) for the account. Please copy this onto your clipboard.

Now you can initialize the Java SDK with the following account to make requests.

```java
Account account = new Account("YOUR BEARER TOKEN");
```

### 3-Legged OAuth Flow

The Java SDK also provides a programmatic way for the developer to implement the 3-Legged OAuth Flow
 with the help of a webserver. Please see the additional documentation 
 located [here](https://developers.kloudless.com/docs/v1/authentication#oauth-2.0).

#### First Leg

1. Find the Application ID (App ID) located 
[here](https://developers.kloudless.com/applications/*/details) and copy it to your clipboard.
2. Determine the scope of what cloud services your users need to have to authenticate their account.
 The scope all includes all services Kloudless supports.
3. The Redirect URI is of the webserver that the authenticated user will be redirected from. See 
documentation [here](https://developers.kloudless.com/applications/*/details#oauth-settings).
4. Fill in the `App ID`, `Redirect URI`, and `Scope` below to retrieve the Authorization URL.
5. Store the state which could be used in the third leg step.
6. Redirect the user to this URL to begin the second leg of the OAuth flow process.

The example below shows how to retrieve a token in the context of a web application. The 
`Application` class helps generate an Authorization URL that we redirect the user to after storing 
the `state` data generated to check when the user returns to our app.


```java
@Controller
get("/", (request, response) -> {
    Map<String, String> urlAndState = Application.getAuthorizationUrl(
            "YOUR APP ID",
            "http://YOUR DOMAIN NAME/callback",
            "Scope");
    App.currentState = urlAndState.get("state");
    response.redirect(urlAndState.get("url"));
    return "";
});
```

#### Third Leg

You will need to take the response from the 2nd leg after the user is redirected and call this 
helper method in the Java SDK to obtain an access token. This access token will use the Bearer Token
 Authentication. See the documentation 
[here](https://developers.kloudless.com/docs/v1/authentication#header-authorization-code-grant-flow-1).

1. State : Obtained from the user after redirecting 
2. Code: Obtained from the user after redirecting
3. Redirect URI: The Redirect URI specified in the First Leg
4. Client ID: App ID in First Leg
5. Client Secret: Find the API Key located here.
6. Call the Java SDK Helper method with the information to retrieve the Bearer Token.

The code below continues our earlier example using a web application. The application has a 
`/callback` endpoint that receives the callback from the Kloudless server which includes the 
original `state` and a `code` to exchange for an access token using the `Application` class as 
shown below.


```java
@Controller
get("/callback", (request, response) -> {
    String accessToken = Application.retrieveToken(
            request.queryParams.get("state"), 
            "STATE FROM THE PREVIOUS STEP",
            request.queryParams.get("code"), 
            "http://YOUR DOMAIN NAME/callback",
            "YOUR APP ID",
            "YOUR API KEY");

    Account account = new Account(accessToken);
    Resource me = (Resource) account.get("");
    return me.toString();
});
```

## Initialize an Account

If you already know your bearer token, you can instantiate an instance of the `Account` class with 
the token.

### What is an Account

The Account class is the core of the Kloudless Java SDK. The account represents the authorized cloud
 service that the user connected. We've included some examples of Kloudless API requests using the 
 instantiated account object below. The Kloudless Java SDK also has three response classes: 
 `Resource`, `ResourceList`, and `ResponseRaw`. We've also included examples on how to use these 
 response objects returned from the API requests.

### Initialize Account by bearer token

```java
Account account = new Account("YOUR BEARER TOKEN");
```

### Initialize Account by API Key and Account Id

You can also use the Account Id and API Key to instantiate an Account object.

```java
Account account = new Account("accountId", "YOUR API KEY");
```

### Verifying the Account Bearer Token

We recommend verifying the token retrieved with your Application ID.

```java
boolean isMatch = Application.verifyToken("YOUR BEARER TOKEN", "YOUR APP ID");
```

## Make API Requests

### How to get Resource and ResourceList

`Resource` and `ResourceList` will be generated by our SDK, and what object is constructed depends 
on the types of data from Kloudless API server. If Kloudless API Server outputs a JSONObject list, 
Account will transfer it as ResourceList and it contains a List of Resource objects. After you got 
the Resource object, you can find what information you need inside the Resource object, for 
example id and name.

In the example below, we showcase how to retrieve all calendars of a calendar cloud service account, 
such as Google Calendar or Outlook Calendar.

```java
ResourceList calendarList = (ResourceList) account.get("cal/calendars");
for (Resource calendar : calendarList.getResources()) {
    JsonObject calendarData = calendar.getData();
    System.out.println(calendar.getId());
    System.out.println(calendarData.get("name").getAsString());
}
```
You can also make an API request to retrieve information about a specific calendar.  The SDK will 
generate a `Resource` object as the response.

```java
Resource calendar = (Resource) account.get("cal/calendars/" + calendarId);
JsonObject calendarData = calendar.getData();
System.out.println(calendar.getId());
System.out.println(calendarData.get("name").getAsString());
```

### Resource

The `Resource` class represents the basic JSON response returned by the API server. In addition, 
the class includes helper methods to make HTTP requests directly on the resource.

#### Basic usages

Please refer to the example below.

```java
//Get a calendar
Resource myCalendar = (Resource) account
                .get("cal/calendars/" + calendarId);

//Modify the name of my calendar
Map<String, Object> updateContent = new HashMap<String, Object>();
updateContent.put("name", "The NEW name of my calendar");
Resource modifiedCalendar = myCalendar.patch(updateContent);

//Delete my calendar
modifiedCalendar.delete();
```

#### Resource supports file opeartion
After you've retrieved a `Resource` object, you can also modify the contents or metadata of the 
object via the `patch` or `put` helper methods. Please refer to the example of a file type 
`Resource` below.

```java
Resource file = (Resource) account.get("storage/files/" + fileId);
// Modify the meta data of file
Map<String, Object> updateMeta = new HashMap<String, Object>();
updateMeta.put("name", "The modified name");
updateMeta.put("parent_id", fileMeta.getData().get("parent").getAsString());
updateMeta.put("account", account.getId());
Resource modifiedFile = file.patch(updateMeta);
// Modify the file content
File newFile = new File("PATH OF NEW FILE");
Resource modifiedFileContent = file.put(newFile);

// Delete file
modifiedFileContent.delete();
```

#### Get RawData from upstream service

The Kloudless API unifies data retrieved from the upstream service. However, if you would like to 
parse the raw data returned, please set the `X-Kloudless-Raw-Data` header attribute to `true`. The 
raw data will be returned in the `raw` field of the JSON response.

```java
Map<String, Object> headers = new HashMap<String, Object>();
headers.put("X-Kloudless-Raw-Data", Boolean.toString(true));
Resource calendarRaw = (Resource) account.get("cal/calendars/" + calendarId, headers);
System.out.println(calendarRaw.getData().get("raw").toString());
```

### ResourceList

The ResourceList Class is a collection of Resource objects, and it has additional helper methods 
below.

#### Pagination of ResourceList

Since the Kloudless API supports pagination when a list of resources is returned, the `ResourceList` 
Class includes helper methods to paginate through the list of `Resource` objects. The example below 
shows how to retrieve events of a specific calendar with the current page. Then, it will paginate 
for additional resources using `hasNextPage()` and `getNextPage()`.

```java
ResourceList eventList = (ResourceList) account.get("cal/calendars/" + calendarId + "/events");
// Show the page number
System.out.println(eventList.getPage())
if(eventList.hasNextPage()){
    ResourceList secondPage = eventList.getNextPage();
}
```

#### Autopagination of ResourceList

The `ResourceList` class also supports autopagination to allow you to iterate over all resources. 
The example below shows how to use the `ResourceList` `Iterator`.

```java
ResourceList eventList = (ResourceList) account.get("cal/calendars/" + calendarId + "/events");
Iterator<Resource> resourceIterator = eventList.getPageIterator();
while (resourceIterator.hasNext()) {
        Resource resource = resourceIterator.next();
        System.out.println(resource.getData().get("name").getAsString());
}
```

Moreover, the autopagination Iterator can set a limit on the number of resources traversed. The 
example below shows how to iterate over this subset.

```java
ResourceList eventList = (ResourceList) account.get("cal/calendars/" + calendarId + "/events");
resourceIterator = eventList.getPageIterator(300L);
while (resourceIterator.hasNext()) {
        Resource resource = resourceIterator.next();
        System.out.println(resource.getData().get("name").getAsString());
}
```

### ResponseRaw 

For handling different content types other than JSON, such as binary data, the SDK provides a third 
type of Resource Class named `ResponseRaw`.  `ResponseRaw` inherits from the `HttpResponse` class.

```java
Resource fileMeta = (Resource) account.get("storage/files/" + fileId);
System.out.println("Get file meta " + fileMeta.getData().toString());

ResponseRaw responseRaw = (ResponseRaw) account.get("storage/files/" + fileId + "/contents");
System.out.println(ContentType.get(responseRaw.getData().getEntity())
HttpResponse httpResponse = responseRaw.getData();
try (InputStream inputStream = httpResponse.getEntity().getContent()) {
    // Handle input stream here
}
```

## Other usage of Account 

We have provided some examples to use the `get` helper method to make API requests directly to the 
Kloudless API Server. However, the Account class also supports other HTTP methods, such as `post`, 
`put`, `patch`, and `delete`. We've included some examples below.

```java
//Use account to create a new calendar
Map<String, Object> httpContent = new HashMap<String, Object>();
httpContent.put("name", "The New Calendar");
httpContent.put("description", "This is for an new calendar");
httpContent.put("location", "San Francisco, CA");
httpContent.put("timezone", "US/Pacific");
Resource newCalendar = (Resource) account.post("cal/calendars", httpContent);
String calendarId = newCalendar.getId();
//modify calendar 
Map<String, Object> updateContent = new HashMap<String, Object>();
updateContent.put("name", "The Revised Calendar");
Resource modifiedCalendar = account.patch("cal/calendars/" + calendarId, updateContent);
//delete calendar
account.delete("cal/calendars/" + calendarId);
```

In addition, you can use the Account object to create resources with the `post` helper method.  In 
the example below, we create a file type `Resource` object with the `name` and `parent_id` metadata 
set in the header of the HTTP request.

```java
File newFile = new File("PATH OF LOCAL FILE");
String folderId = "FOLDER ID";
Map<String, Object> json = new HashMap<String, Object>();
json.put("name", newFile.getName());
json.put("parent_id", folderId);
Map<String, Object> headers = new HashMap<String, Object>();
headers.put("X-Kloudless-Metadata", new Gson().toJson(json));
Resource file = (Resource) account.post("storage/files?overwrite=false", headers, newFile);
```

### Pass-through API

You can use the Pass-through API to make HTTP requests directly to the upstream service for any 
endpoints unsupported by the Kloudless API. Please note that a raw HttpResponse object is returned 
by the SDK.

```java
HttpResponse driveInfo = account.raw("GET", "/drive/v2/about", null, null);
```


# Build

* You can create the jar, skipping tests, with `gradle build -x test`.
* You can create a jar and install it to the local maven repository with `gradle install`


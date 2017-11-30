# Kloudless API Java Bindings

You can sign up for a Kloudless Developer account at https://kloudless.com.

# Requirements

Java 1.8 and later.

# Installation

### Maven

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>com.kloudless</groupId>
    <artifactId>kloudless-java</artifactId>
    <version>1.1.0</version>
</dependency>
```

### Gradle

Add this dependency to your project's build file:

```groovy
compile 'com.kloudless:kloudless-java:1.1.0'
```

### Others

You'll need to manually install the Kloudless JAR, either by compiling it or
downloading it from the [Releases page](https://github.com/Kloudless/kloudless-java/releases/)

# Usage


See [KloudlessTest.java](https://github.com/Kloudless/kloudless-java/blob/master/src/test/java/com/kloudless/KloudlessTest.java)
for more examples.

User Impersonation can be seen found in KloudlessTest.java or the example below:

```java
HashMap<String, String> customHeaders = new HashMap<String, String>();
customHeaders.put("X-Kloudless-As-User", "<USER ID>");
Kloudless.addCustomHeaders(customHeaders);
```

## Instantiating a Kloudless Client

The 1.1.0 version of the Kloudless Java SDK includes the KClient class, which
allows you to instantiate a client with a specific bearer token and account id.

It is fairly basic to instantiate:

```java
// Instantiation
KClient storageClient = new KClient("token12345", "accountID12345", null);

// Retrieve contents of Folder with id "root"
storageClient.contents(null, Folder.class, "root");
```

If you want to add user impersonation, the 3rd argument is for any additional
headers that will be added to all requests.

See [KloudlessClientTest.java](https://github.com/Kloudless/kloudless-java/blob/master/src/test/java/com/kloudless/KloudlessClientTest.java) for more examples.


# Building


* You can create the jar, skipping tests, with `mvn install -DskipTests`.
* You can create a jar that includes dependencies with `mvn compile assembly:single`.
* You can gather dependencies for easy installation/deployment with `mvn dependency:copy-dependencies`.
  They will be located in `target/dependencies`.

# Testing

You must have Maven installed. To run the tests, simply run `mvn test`.
Before tests, the API key or bearer token can be specified by using environment 
variables, program arguments, or the property file. The property file is required
for tests.

## Environment variables

```
API_KEY=keyValue mvn test
BEARER_TOKEN=tokenValue mvn test
```

## Program arguments

```
mvn test -DapiKey=keyValue
mvn test -DbearerToken=tokenValue
```

You can run specific tests with the command-line argument `-D test=Class#method`.
For example, `-D test=KloudlessTest#testAccountAll`.

## Properties file

Before testing, please create a file called
*kloudless.configuration.properties* under the resources folder of the test
project.

The API server to direct API requests to can be specified with this file.
To use a custom server, direct traffic to it as shown below:

```
api_server_addr         = demo-api.kloudless.com 
api_server_proto        = https 
api_server_port         = 443 
```

You can specify accounts to test with the _test_accounts_ key in the properties
file:

```
# multiple test accounts can be separated with a semi-colon
test_accounts = 1234; 5678; 91011
```

A Kloudless JSON Account object to test with can be included with the
_one_test_account_json_ key.
The [Account Metadata endpoint](https://developers.kloudless.com/docs/v1/authentication#accounts-retrieve-an-account)
can be used to retrieve the JSON representation of the account.

```
test_one_test_account_json = {"id":1234,"service":.....}
```

The file to upload during tests can be specified as shown below:

```
path_uploading_file=/path/to/a/file
```

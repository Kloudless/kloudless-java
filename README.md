kloudless-java
==============
# KloudlessAPI Java Bindings

You can sign up for a Kloudless Developer account at https://developers.kloudless.com.

Requirements
============

Java 1.8 and later.

Installation
============

You'll need to manually install the following JARs:

* [The Kloudless SDK JAR from S3 with dependencies included](https://s3-us-west-2.amazonaws.com/kloudless-static-assets/p/platform/sdk/kloudless-java-0.1.6.jar)

Usage
=====

See [KloudlessTest.java](https://github.com/Kloudless/kloudless-java/blob/master/test/com/kloudless/KloudlessTest.java)
for more examples.

User Impersonation can be seen found in KloudlessTest.java or the example below:

```java
HashMap<String, String> customHeaders = new HashMap<String, String>();
customHeaders.put("X-Kloudless-As-User", "<USER ID>");
Kloudless.addCustomHeaders(customHeaders);
```

Building
==========

* You can create the jar, skipping tests, with `mvn install -DskipTests`.
* You can create a jar that includes dependencies with `mvn compile assembly:single`.
* You can gather dependencies for easy installation/deployment with `mvn dependency:copy-dependencies`.
  They will be located in `target/dependencies`.

Testing
=======
You must have Maven installed. To run the tests, simply run `mvn test`.
Before tests, api key or bearer token can be specified by using environment 
variable, program argument, and property file. 
```
# environment variable
API_KEY=keyValue mvn test
BEARER_TOKEN=tokenValue mvn test
# program argument
mvn test -DapiKey=keyValue
mvn test -DbearerToken=tokenValue
```
You can run particular tests by passing `-D test=Class#method`.
For example, `-D test=KloudlessTest#testAccountAll`.

## kloudless.configuration.properties
Before testing, please create a file called *kloudless.configuration.properties* under 
the resources folder of the test project
### accounts for the test
_test_accounts_ is the key where multiple accounts can be specified like below
```
# multiple test accounts use colon to seprate
test_accounts = 1234; 5678; 91011
```
### json data for the test
_one_test_account_json_ is the key where json data for an account can be specified.
This [endpoint](https://developers.kloudless.com/docs/v1/authentication#accounts-retrieve-an-account)
can download the json data for an account
```
test_one_test_account_json = {"id":1234,"service":.....}
```
### file to be uploaded for the test
```
path_uploading_file=/path/to/a/file
```
### server information for the test
```
api_server_addr         = localhost 
api_server_proto        = http
api_server_port         = 8002 
```

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

* [The Kloudless SDK JAR from S3 with dependencies included](https://s3-us-west-2.amazonaws.com/kloudless-static-assets/p/platform/sdk/kloudless-java-1.0.1.jar)

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
BEARER_TOKEN=tokenValue
# program argument
mvn test -DapiKey=keyValue
mvn test -DbearerToken=tokenValue
# property file
api_key = keyValue
bearer_token = keyValue
```
You can run particular tests by passing `-D test=Class#method`.
For example, `-D test=KloudlessTest#testAccountAll`.


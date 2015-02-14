kloudless-java
==============
# KloudlessAPI Java Bindings

You can sign up for a Kloudless Developer account at https://developers.kloudless.com.

Requirements
============

Java 1.5 and later.

Installation
============

You'll need to manually install the following JARs:

* The Kloudless JAR from [S3](https://s3-us-west-2.amazonaws.com/kloudless-static-assets/p/platform/sdk/kloudless-java-0.1.0.jar)
* [Google Gson](http://code.google.com/p/google-gson/) from <http://google-gson.googlecode.com/files/google-gson-2.2.4-release.zip>.

Usage
=====

See [KloudlessTest.java](https://github.com/Kloudless/kloudless-java/blob/master/test/com/kloudless/KloudlessTest.java) for more examples.

Testing
=======

You must have Maven installed. To run the tests, simply run `mvn test`. You can run particular tests by passing `-D test=Class#method` -- for example, `-D test=KloudlessTest#testAccountAll`.


# Change Log

## 2.0.1
* A Kloudless client object can now use the `getBinary` method to download
  file contents, which avoids the library trying to parse the response body
  as a JSON object.

## 2.0.0
* The API version has been updated to `v2`. This introduces backwards
  incompatible changes. Please read the new [README](README.md) for
  more information on the new version.

## 1.1.0
* A Kloudless client object can now be instantiated with a specific bearer
  token and account id using the `KClient` class. See the README for more
  information.
* The CRM API is now supported.

## 1.0.0

* The API version has been updated to `v1`. This introduces backwards
  incompatible changes. Please review the
  [migration guide](https://developers.kloudless.com/docs/v1/migration)
  for more information on migrating from `v0` to `v1`.

# The library

This is a simple library for sending [APNs](https://developer.apple.com/documentation/usernotifications) (iOS, macOS, and Safari) push notifications.

It uses Apple's HTTP/2-based APNs protocol and supports token-based authentication only. It only supports **Java 11+**.

This library is used in production by a few projects, but its API can change in the future.

# Building

Building it locally and adding to maven local.

````
./grade publishToMavenLocal
````

# Usage

### Dependencies

Maven:

````
<dependency>
  <groupId>digital.soares</groupId>
  <artifactId>apns-messaging</artifactId>
  <version>0.1.0</version>
</dependency>
````

Gradle:

````
implementation 'digital.soares:apns-messaging:0.1.0'
````

### Code

Sending a notification to APNS service:

````
var authKey = "the content of your .p8 file";
var keyId = "your key id to your .p8 file";
var teamId = "your team id";

var apnsMessaging = ApnsMessaging.of(ApnsKey.of(authKey, keyId, teamId));
apnsMessaging.send(ApnsMessage.builder()
  .setToken("the target device token")
  .setTopic("normally your app id")
  .setAps(Aps.builder()
    .setAlert(Alert.builder()
      .setTitle("Hello there")
      .setBody("This is a notification sent from apns messaging")
      .build())
    .build())
  .build());
);
````

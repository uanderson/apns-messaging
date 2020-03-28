package digital.soares.apns.messaging;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

class ApnsMessagingTest {

  static WireMockServer wireMockServer;
  static ApnsMessaging apnsMessaging;

  @BeforeAll
  static void prepare() throws Exception {
    wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
    apnsMessaging = ApnsMessaging.of(ApnsKeys.readKey(), token ->
      URI.create("http://localhost:" + wireMockServer.port() + "/3/device/" + token));

    wireMockServer.start();
  }

  @AfterAll
  static void release() {
    wireMockServer.stop();
  }

  @Test
  @DisplayName("Should send basic notification")
  void sendBasicNotification() throws IOException, InterruptedException {
    apnsMessaging.send(ApnsMessage.builder()
      .setToken("cc566d1c79f4470f96015b0e3b402abb")
      .setTopic("topic")
      .setAps(Aps.builder()
        .setAlert(Alert.builder()
          .setTitle("Title")
          .setBody("Body")
          .build())
        .build())
      .build());

    wireMockServer
      .verify(postRequestedFor(urlEqualTo("/3/device/cc566d1c79f4470f96015b0e3b402abb"))
        .withHeader("authorization", containing("bearer "))
        .withHeader("content-type", equalTo("application/json"))
        .withHeader("apns-priority", equalTo("10"))
        .withHeader("apns-topic", equalTo("topic"))
        .withRequestBody(matchingJsonPath("$.aps.alert.title", equalTo("Title")))
        .withRequestBody(matchingJsonPath("$.aps.alert.body", equalTo("Body")))
      );
  }
}

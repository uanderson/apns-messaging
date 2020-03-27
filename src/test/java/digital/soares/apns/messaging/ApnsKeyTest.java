package digital.soares.apns.messaging;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

class ApnsKeyTest {

  private static String key;

  @BeforeAll
  static void prepare() throws IOException {
    try (InputStream resource = ApnsKeyTest.class.getResourceAsStream("/key.p8")) {
      var inputStreamReader = new InputStreamReader(resource, StandardCharsets.UTF_8);
      key = new BufferedReader(inputStreamReader).lines()
        .collect(Collectors.joining());
    }
  }

  @Test
  @DisplayName("Should create the apns key")
  void shouldCreate() throws Exception {
    ApnsKey.of(key, "A1B2C3D4F5", "A1B2C3D4F5");
  }
}

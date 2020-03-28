package digital.soares.apns.messaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ApnsKeys {

  static ApnsKey readKey() throws IOException {
    try (InputStream resource = ApnsKeyTest.class.getResourceAsStream("/key.p8")) {
      var inputStreamReader = new InputStreamReader(resource, StandardCharsets.UTF_8);
      var key = new BufferedReader(inputStreamReader).lines()
        .collect(Collectors.joining());

      return ApnsKey.of(key, "A1B2C3D4F5", "A1B2C3D4F5");
    }
  }
}

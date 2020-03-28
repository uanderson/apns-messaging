package digital.soares.apns.messaging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApnsKeyTest {

  private static String key;

  @Test
  @DisplayName("Should create the apns key")
  void shouldCreate() throws Exception {
    ApnsKeys.readKey();
  }
}

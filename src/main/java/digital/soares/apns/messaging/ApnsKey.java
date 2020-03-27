package digital.soares.apns.messaging;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * Combines the signing key, the key id and team id necessary
 * to generate a JWT token responsible to grant access to
 * Apple's APNS service.
 *
 * @author <a href="https://usoar.es">Uanderson Soares</a>
 */
public class ApnsKey {

  private final Key key;
  private final String keyId;
  private final String teamId;

  private ApnsKey(Key key, String keyId, String teamId) {
    this.key = key;
    this.keyId = keyId;
    this.teamId = teamId;
  }

  public static ApnsKey of(@NonNull String key, @NonNull String keyId, @NonNull String teamId) {
    Objects.requireNonNull(key, "Key must not be null");
    Objects.requireNonNull(keyId, "Key id must not be null");
    Objects.requireNonNull(teamId, "Team id must not be null");

    var cleanKey = key
      .replaceAll("\\r\\n|\\r|\\n", "")
      .replaceAll("-----.*?-----", "");

    try {
      var keyBytes = Base64.getDecoder().decode(cleanKey.getBytes());
      var spec = new PKCS8EncodedKeySpec(keyBytes);
      var keyFactory = KeyFactory.getInstance("EC");
      var privateKey = keyFactory.generatePrivate(spec);

      return new ApnsKey(privateKey, keyId, teamId);
    } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  public Key getKey() {
    return key;
  }

  public String getKeyId() {
    return keyId;
  }

  public String getTeamId() {
    return teamId;
  }
}

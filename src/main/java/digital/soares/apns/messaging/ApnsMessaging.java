package digital.soares.apns.messaging;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * This class is the entry point for all server-side Apple APNS actions.
 *
 * @author <a href="https://usoar.es">Uanderson Soares</a>
 */
public class ApnsMessaging {

  private static final int CONNECTION_TIMEOUT = 30;
  private static final long CACHE_TTL = 55 * 60 * 1000;

  private final ApnsKey apnsKey;
  private final Environment environment;
  private final HttpClient httpClient;
  private final ObjectWriter objectWriter;
  private long tokenTimestamp;
  private String cachedToken;

  private ApnsMessaging(ApnsKey apnsKey, Environment environment) {
    this.apnsKey = apnsKey;
    this.environment = environment;
    this.httpClient = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_2)
      .build();

    this.objectWriter = new ObjectMapper()
      .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
      .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
      .writerWithView(ApnsView.class);
  }

  public static ApnsMessaging of(@NonNull ApnsKey apnsKey) {
    Objects.requireNonNull(apnsKey, "Apns key must not be null");
    return of(apnsKey, AppleEnvironment.PRODUCTION);
  }

  public static ApnsMessaging of(@NonNull ApnsKey apnsKey, @NonNull Environment environment) {
    Objects.requireNonNull(apnsKey, "Apns key must not be null");
    Objects.requireNonNull(environment, "Environment must not be null");
    return new ApnsMessaging(apnsKey, environment);
  }

  /**
   * Sends a notification to Apple's APNS.
   *
   * @param message to be sent
   * @return a {@link HttpResponse} with the result
   * @throws NullPointerException in case of {@code message} is null
   * @throws IOException          in case of something goes wrong
   * @throws InterruptedException in case of something goes wrong
   */
  public HttpResponse<String> send(ApnsMessage message) throws IOException, InterruptedException {
    Objects.requireNonNull(message, "Message must not be null");
    return httpClient.send(createRequest(message), HttpResponse.BodyHandlers.ofString());
  }

  /**
   * Sends a notification to Apple's APNS.
   *
   * @param message to be sent
   * @return a {@link CompletableFuture} to be called when done
   * @throws NullPointerException in case of {@code message} is null
   * @throws IOException          in case of something goes wrong
   */
  public CompletableFuture<HttpResponse<String>> sendAsync(ApnsMessage message) throws IOException {
    Objects.requireNonNull(message, "Message must not be null");
    return httpClient.sendAsync(createRequest(message), HttpResponse.BodyHandlers.ofString());
  }

  /**
   * Build a {@link HttpRequest}.
   *
   * @param message with the pertinent data
   * @return a new {@link HttpRequest}
   */
  private HttpRequest createRequest(ApnsMessage message) throws IOException {
    if (Objects.isNull(cachedToken) || isTokenExpired()) {
      cachedToken = generateToken();
    }

    HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
      .uri(environment.createUri(message.getToken()))
      .timeout(Duration.ofSeconds(CONNECTION_TIMEOUT))
      .header("authorization", "bearer " + cachedToken)
      .header("content-type", "application/json")
      .header("apns-priority", message.getPriority().getCode())
      .header("apns-topic", message.getTopic())
      .POST(BodyPublishers.ofString(objectWriter.writeValueAsString(message)));

    if (message.isIdentifiable()) {
      requestBuilder.header("apns-id", String.valueOf(message.getId()));
    }

    if (message.isCollapsable()) {
      requestBuilder.header("apns-collapse-id", message.getCollapseId());
    }

    if (message.hasExpiration()) {
      requestBuilder.header("apns-expiration", String.valueOf(message.getId()));
    }

    return requestBuilder.build();
  }

  /**
   * Checks whether the cached token is expired or not.
   *
   * @return {@code true} if expired, {@code false} otherwise
   */
  private boolean isTokenExpired() {
    return System.currentTimeMillis() - tokenTimestamp > CACHE_TTL;
  }

  /**
   * Generates a new JWT token.
   *
   * @return the new generate token
   */
  private String generateToken() {
    try {
      tokenTimestamp = System.currentTimeMillis();
      return Jwts.builder()
        .claim("iss", apnsKey.getTeamId())
        .claim("iat", Long.toString(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")).toEpochSecond()))
        .setHeaderParam("kid", apnsKey.getKeyId())
        .signWith(apnsKey.getKey(), SignatureAlgorithm.ES256)
        .compact();
    } catch (Exception e) {
      throw new IllegalStateException("Wasn't possible to generate the token");
    }
  }

  /**
   * Defines URL's to either production and development
   * environments.
   */
  public enum AppleEnvironment implements Environment {

    DEVELOPMENT("https://api.development.push.apple.com/3/device/"),
    PRODUCTION("https://api.push.apple.com/3/device/");

    private final String url;

    private AppleEnvironment(String url) {
      this.url = url;
    }

    /**
     * Creates an URI based on the current environment and the device token.
     *
     * @param token of the device
     * @return a {@link URI} with the device token added
     */
    public URI createUri(@NonNull String token) {
      Objects.requireNonNull(token, "Token must not be null");
      return URI.create(url + token);
    }
  }
}

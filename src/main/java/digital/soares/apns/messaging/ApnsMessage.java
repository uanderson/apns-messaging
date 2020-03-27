package digital.soares.apns.messaging;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a message that can be sent via Apple's APNS service.
 * Contains payload information as well as the device and behavior of the
 * delivered message.
 *
 * @author <a href="https://usoar.es">Uanderson Soares</a>
 * @see <a href="https://developer.apple.com/library/archive/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/CommunicatingwithAPNs.html">Communicating with APNS</a>
 */
public class ApnsMessage {

  private final String token;
  private final UUID id;
  private final long expiration;
  private final Priority priority;
  private final String topic;
  private final String collapseId;
  private final Map<String, String> data;

  @JsonView(ApnsView.class)
  private final Aps aps;

  private ApnsMessage(Builder builder) {
    if (builder.token == null || builder.token.length() == 0) {
      throw new IllegalArgumentException("Token must not be empty");
    }

    this.token = builder.token;
    this.id = builder.id;
    this.expiration = builder.expiration;
    this.priority = builder.priority == null
      ? Priority.IMMEDIATE : builder.priority;

    this.topic = builder.topic == null
      ? "" : builder.topic;

    this.collapseId = builder.collapseId;
    this.data = builder.data;
    this.aps = builder.aps;
  }

  public String getToken() {
    return token;
  }

  @Nullable
  public UUID getId() {
    return id;
  }

  public long getExpiration() {
    return expiration;
  }

  public Priority getPriority() {
    return priority;
  }

  public String getTopic() {
    return topic;
  }

  @Nullable
  public String getCollapseId() {
    return collapseId;
  }

  @JsonAnyGetter
  public Map<String, String> getData() {
    return Map.copyOf(data);
  }

  public Aps getAps() {
    return aps;
  }

  /**
   * Checks whether the message has an assigned id.
   *
   * @return true if has an assigned id, false otherwise
   */
  @JsonIgnore
  public boolean isIdentifiable() {
    return id != null;
  }

  /**
   * Checks whether the message can be collapsed.
   *
   * @return true if can collapse, false otherwise
   */
  @JsonIgnore
  public boolean isCollapsable() {
    return collapseId != null && collapseId.length() > 0;
  }

  /**
   * Checks whether the message expires.
   *
   * @return true if expiration is higher than -1, false otherwise
   */
  public boolean hasExpiration() {
    return expiration > -1;
  }

  /**
   * Creates a new {@link ApnsMessage.Builder}.
   *
   * @return A {@link ApnsMessage.Builder} instance.
   */
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private String token;
    private UUID id;
    private int expiration = -1;
    private Priority priority = Priority.IMMEDIATE;
    private String topic;
    private String collapseId;
    private Aps aps;
    private final Map<String, String> data = new HashMap<>();

    public Builder setToken(String token) {
      this.token = token;
      return this;
    }

    public Builder setId(UUID id) {
      this.id = id;
      return this;
    }

    public Builder setExpiration(int expiration) {
      this.expiration = expiration;
      return this;
    }

    public Builder setPriority(Priority priority) {
      this.priority = priority;
      return this;
    }

    public Builder setTopic(String topic) {
      this.topic = topic;
      return this;
    }

    public Builder setCollapseId(String collapseId) {
      this.collapseId = collapseId;
      return this;
    }

    public Builder setAps(Aps aps) {
      this.aps = aps;
      return this;
    }

    public Builder putData(String key, String value) {
      this.data.put(key, value);
      return this;
    }

    public Builder putAllData(Map<String, String> allData) {
      this.data.putAll(allData);
      return this;
    }

    /**
     * Creates a new {@link ApnsMessage} instance.
     *
     * @return A new {@link ApnsMessage} instance.
     * @throws IllegalArgumentException If any of the parameters set on the builder are invalid.
     */
    public ApnsMessage build() {
      return new ApnsMessage(this);
    }
  }

  /**
   * Represents the priority in which the message
   * should be delivered.
   */
  public enum Priority {
    IMMEDIATE("10"),
    THROTTLED("5");

    private final String code;

    private Priority(String code) {
      this.code = code;
    }

    /**
     * Gets the priority code specified by Apple's
     * APNS service.
     *
     * @return the priority code
     */
    public String getCode() {
      return this.code;
    }
  }
}

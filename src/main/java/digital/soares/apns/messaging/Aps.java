package digital.soares.apns.messaging;

import com.fasterxml.jackson.annotation.JsonView;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Contains the keys used by Apple to deliver the notifications
 * to the user's device. It specifies the type of interactions you
 * want the system to use when delivering it to the user.
 *
 * @author <a href="https://usoar.es">Uanderson Soares</a>
 * @see <a href="https://developer.apple.com/library/archive/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/PayloadKeyReference.html#//apple_ref/doc/uid/TP40008194-CH17-SW1">APS Dictionary Keys</a>
 */
@JsonView(ApnsView.class)
public class Aps {

  private final Alert alert;
  private final Integer badge;
  private final String sound;
  private final Integer contentAvailable;
  private final String category;
  private final String threadId;

  private Aps(Builder builder) {
    if (builder.alert == null) {
      throw new IllegalArgumentException("Alert must not be null");
    }

    this.alert = builder.alert;
    this.badge = builder.badge;
    this.sound = builder.sound;
    this.contentAvailable = builder.contentAvailable;
    this.category = builder.category;
    this.threadId = builder.threadId;
  }

  @NonNull
  public Alert getAlert() {
    return alert;
  }

  @Nullable
  public Integer getBadge() {
    return badge;
  }

  @Nullable
  public String getSound() {
    return sound;
  }

  @Nullable
  public Integer getContentAvailable() {
    return contentAvailable;
  }

  @Nullable
  public String getCategory() {
    return category;
  }

  @Nullable
  public String getThreadId() {
    return threadId;
  }

  /**
   * Creates a new {@link Aps.Builder}.
   *
   * @return A {@link Aps.Builder} instance.
   */
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private Alert alert;
    private Integer badge;
    private String sound;
    private Integer contentAvailable;
    private String category;
    private String threadId;

    public Builder setAlert(Alert alert) {
      this.alert = alert;
      return this;
    }

    public Builder setBadge(int badge) {
      this.badge = badge;
      return this;
    }

    public Builder setSound(String sound) {
      this.sound = sound;
      return this;
    }

    public Builder setContentAvailable(int contentAvailable) {
      this.contentAvailable = contentAvailable;
      return this;
    }

    public Builder setCategory(String category) {
      this.category = category;
      return this;
    }

    public Builder setThreadId(String threadId) {
      this.threadId = threadId;
      return this;
    }

    /**
     * Creates a new {@link Aps} instance.
     *
     * @return A new {@link Aps} instance.
     * @throws IllegalArgumentException If any of the parameters set on the builder are invalid.
     */
    public Aps build() {
      return new Aps(this);
    }
  }
}

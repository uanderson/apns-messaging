package digital.soares.apns.messaging;

import com.fasterxml.jackson.annotation.JsonView;
import edu.umd.cs.findbugs.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="https://usoar.es">Uanderson Soares</a>
 * @see <a href="https://developer.apple.com/library/archive/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/PayloadKeyReference.html#//apple_ref/doc/uid/TP40008194-CH17-SW1">APS Dictionary Keys</a>
 */
@JsonView(ApnsView.class)
public class Alert {

  private final String title;
  private final String body;
  private final String titleLocKey;
  private final List<String> titleLocArgs;
  private final String actionLocKey;
  private final String locKey;
  private final List<String> locArgs;
  private final String launchImage;

  public Alert(Builder builder) {
    this.title = builder.title;
    this.body = builder.body;
    this.titleLocKey = builder.titleLocKey;
    this.titleLocArgs = builder.titleLocArgs;
    this.actionLocKey = builder.actionLocKey;
    this.locKey = builder.locKey;
    this.locArgs = builder.locArgs;
    this.launchImage = builder.launchImage;
  }

  @Nullable
  public String getTitle() {
    return title;
  }

  @Nullable
  public String getBody() {
    return body;
  }

  @Nullable
  public String getTitleLocKey() {
    return titleLocKey;
  }

  public List<String> getTitleLocArgs() {
    if (Objects.isNull(titleLocArgs)) {
      return List.of();
    }

    return List.copyOf(titleLocArgs);
  }

  @Nullable
  public String getActionLocKey() {
    return actionLocKey;
  }

  @Nullable
  public String getLocKey() {
    return locKey;
  }

  public List<String> getLocArgs() {
    if (Objects.isNull(locArgs)) {
      return List.of();
    }

    return List.copyOf(locArgs);
  }

  @Nullable
  public String getLaunchImage() {
    return launchImage;
  }

  /**
   * Creates a new {@link Alert.Builder}.
   *
   * @return A {@link Alert.Builder} instance.
   */
  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private String title;
    private String body;
    private String titleLocKey;
    private List<String> titleLocArgs;
    private String actionLocKey;
    private String locKey;
    private List<String> locArgs;
    private String launchImage;

    public Builder setTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder setBody(String body) {
      this.body = body;
      return this;
    }

    public Builder setTitleLocKey(String titleLocKey) {
      this.titleLocKey = titleLocKey;
      return this;
    }

    public Builder addTitleLocArg(String titleLocArg) {
      if (Objects.isNull(this.titleLocArgs)) {
        this.titleLocArgs = new ArrayList<>();
      }

      this.titleLocArgs.add(titleLocArg);
      return this;
    }

    public Builder setActionLocKey(String actionLocKey) {
      this.actionLocKey = actionLocKey;
      return this;
    }

    public Builder setLocKey(String locKey) {
      this.locKey = locKey;
      return this;
    }

    public Builder addLocArg(String locArg) {
      if (Objects.isNull(this.locArgs)) {
        this.locArgs = new ArrayList<>();
      }

      this.locArgs.add(locArg);
      return this;
    }

    public Builder setLaunchImage(String launchImage) {
      this.launchImage = launchImage;
      return this;
    }

    /**
     * Creates a new {@link Alert} instance.
     *
     * @return A new {@link Alert} instance.
     */
    public Alert build() {
      return new Alert(this);
    }
  }
}

package digital.soares.apns.messaging;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.net.URI;

public interface Environment {

  URI createUri(@NonNull String token);
}

package io.appmetrica.analytics.push.provider.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Interface for various push services adapter.
 */
public interface PushServiceController {

    /**
     * Should initialize push service.
     * Called during Push SDK activation.
     *
     * @return true if register is successful and false otherwise.
     */
    boolean register();

    /**
     * Should request token from push service and return it synchronously.
     *
     * @return token of push service or null if it is not available.
     */
    @Nullable
    public String getToken();

    /**
     * @return title of push service for logging.
     */
    @NonNull
    String getTitle();

}

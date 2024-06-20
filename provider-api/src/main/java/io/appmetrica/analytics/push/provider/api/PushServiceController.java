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
    String getToken();

    /**
     * @return the name of the push transport that is processed by this controller
     */
    @NonNull
    String getTransportId();

    /**
     * @return restrictions and limitations of push service
     */
    @NonNull
    PushServiceExecutionRestrictions getExecutionRestrictions();

    /**
     * Check if token is actual and attributed to current active project of push service
     * @param token push token to check
     * @return true if token is actual and attributed to current active project
     */
    boolean shouldSendToken(@NonNull String token);
}

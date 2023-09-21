package io.appmetrica.analytics.push.location;

import androidx.annotation.NonNull;

/**
 * Interface for custom location provider.
 * Push SDK will use only this provider if it is set.
 */
public interface LocationProvider {

    /**
     * Called when AppMetrica Push SDK wants to get location.
     *
     * @param provider preferred location provider to use
     * @param requestTimeoutSeconds after this amount of seconds the response will be ignored
     * @param locationVerifier verifier to check if location meet conditions
     * @return location with its status
     */
    @NonNull
    DetailedLocation getLocation(
        @NonNull final String provider,
        final long requestTimeoutSeconds,
        @NonNull final LocationVerifier locationVerifier
    );
}

package io.appmetrica.analytics.push.location;

import android.location.Location;
import androidx.annotation.NonNull;

/**
 * Verifies that location meet some restrictions.
 */
public interface LocationVerifier {

    /**
     * Verifies that location meet some restrictions.
     *
     * @param location {@link Location} to verify
     * @return {@link LocationStatus} object
     */
    @NonNull
    LocationStatus verifyLocation(
        @NonNull Location location
    );
}

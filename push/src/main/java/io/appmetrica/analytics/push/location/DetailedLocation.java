package io.appmetrica.analytics.push.location;

import android.location.Location;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Contains location with its status.
 */
public class DetailedLocation {

    @Nullable
    private final Location location;
    @NonNull
    private final LocationStatus locationStatus;

    /**
     * Constructor for {@link DetailedLocation}.
     *
     * @param location {@link Location} or null
     * @param locationStatus {@link LocationStatus} object
     */
    public DetailedLocation(
        @Nullable Location location,
        @NonNull LocationStatus locationStatus
    ) {
        this.location = location;
        this.locationStatus = locationStatus;
    }

    /**
     * @return location
     */
    @Nullable
    public Location getLocation() {
        return location;
    }

    /**
     * @return location status
     */
    @NonNull
    public LocationStatus getLocationStatus() {
        return locationStatus;
    }
}

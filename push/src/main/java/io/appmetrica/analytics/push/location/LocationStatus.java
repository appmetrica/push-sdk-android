package io.appmetrica.analytics.push.location;

import android.location.Location;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.AppMetricaPush;
import java.util.List;
import java.util.Locale;

/**
 * Location status.
 */
public class LocationStatus {

    @NonNull
    private final String category;
    @NonNull
    private final String details;

    /**
     * Constructor for {@link LocationStatus}.
     * Status category should not depend on parameters.
     *
     * @param category {@link String} with status category
     * @param details {@link String} with status details
     */
    public LocationStatus(
        @NonNull String category,
        @NonNull String details
    ) {
        this.category = category;
        this.details = details;
    }

    /**
     * Returns status category.
     * @return status category
     */
    @NonNull
    public String getCategory() {
        return category;
    }

    /**
     * Returns status details.
     * @return status details
     */
    @NonNull
    public String getDetails() {
        return details;
    }

    /**
     * Checks if Push SDK should treat current status as success.
     * @return true if Push SDK should treat current status as success and false otherwise
     */
    public boolean isSuccess() {
        return false;
    }

    /**
     * Subclass of {@link LocationStatus} for the case when location is successfully obtained.
     */
    public static class Success extends LocationStatus {

        private static final String CATEGORY = "Success";
        private static final String DETAILS = "";

        /**
         * Constructor for {@link Success}.
         */
        public Success() {
            super(CATEGORY, DETAILS);
        }

        @Override
        public boolean isSuccess() {
            return true;
        }
    }

    /**
     * Subclass of {@link LocationStatus} for the case when location provider returned null.
     */
    public static class LocationProviderReturnedNull extends LocationStatus {

        private static final String CATEGORY = "Location provider returned null";
        private static final String DETAILS_PATTERN = "Location provider \"%s\" returned null";

        /**
         * Constructor for {@link LocationProviderReturnedNull}.
         *
         * @param providerName provider name
         */
        public LocationProviderReturnedNull(
            @NonNull final String providerName
        ) {
            super(CATEGORY, String.format(Locale.ENGLISH, DETAILS_PATTERN, providerName));
        }
    }

    /**
     * Subclass of {@link LocationStatus} for the case when {@link LocationManager} is null.
     */
    public static class LocationManagerIsNull extends LocationStatus {

        private static final String CATEGORY = "Location manager is null";
        private static final String DETAILS = "";

        /**
         * Constructor for {@link LocationManagerIsNull}.
         */
        public LocationManagerIsNull() {
            super(CATEGORY, DETAILS);
        }
    }

    /**
     * Subclass of {@link LocationStatus} for the case when custom {@link LocationProvider} is null.
     * Custom location provider can be set using {@link AppMetricaPush#setLocationProvider(LocationProvider)} method.
     */
    public static class CustomLocationProviderIsNull extends LocationStatus {

        private static final String CATEGORY = "Custom location provider is null";
        private static final String DETAILS = "";

        /**
         * Constructor for {@link CustomLocationProviderIsNull}.
         */
        public CustomLocationProviderIsNull() {
            super(CATEGORY, DETAILS);
        }
    }

    /**
     * Subclass of {@link LocationStatus} for the case when proper permission is not granted.
     */
    public static class PermissionIsNotGranted extends LocationStatus {

        private static final String CATEGORY = "Location permissions is not granted";
        private static final String DETAILS_PATTERN = "Location permissions is not granted for %s";

        /**
         * Constructor for {@link PermissionIsNotGranted}.
         *
         * @param provider {@link String} with system name of location provider
         */
        public PermissionIsNotGranted(@NonNull String provider) {
            super(CATEGORY, String.format(Locale.ENGLISH, DETAILS_PATTERN, provider));
        }
    }

    /**
     * Subclass of {@link LocationStatus} for the case when location is not accurate.
     */
    public static class LocationIsNotAccurate extends LocationStatus {

        private static final String CATEGORY = "Location is not accurate";
        private static final String DETAILS_PATTERN = "Got accuracy [%f], maximum allowed [%d]";

        /**
         * Constructor for {@link LocationIsNotAccurate}.
         *
         * @param actualAccuracy actual location accuracy
         * @param minAccuracy required location accuracy
         */
        public LocationIsNotAccurate(
            float actualAccuracy,
            long minAccuracy
        ) {
            super(CATEGORY, String.format(Locale.ENGLISH, DETAILS_PATTERN, actualAccuracy, minAccuracy));
        }
    }

    /**
     * Subclass of {@link LocationStatus} for the case when location is not recent.
     */
    public static class LocationIsNotRecent extends LocationStatus {

        private static final String CATEGORY = "Location is not recent";
        private static final String DETAILS_PATTERN = "Got recency [%d], minimum allowed [%d]";

        /**
         * Constructor for {@link LocationIsNotRecent}.
         *
         * @param actualRecency actual location recency
         * @param minRecency required location recency
         */
        public LocationIsNotRecent(
            long actualRecency,
            long minRecency
        ) {
            super(CATEGORY, String.format(Locale.ENGLISH, DETAILS_PATTERN, actualRecency, minRecency));
        }
    }

    /**
     * Subclass of {@link LocationStatus} for the case when location is not near points.
     */
    public static class LocationIsNotNearPoints extends LocationStatus {

        private static final String CATEGORY = "Location is not near points";
        private static final String DETAILS_PATTERN = "Location is not near points [%s] with radius [%f]";

        /**
         * Constructor for {@link LocationIsNotNearPoints}.
         *
         * @param points required points
         * @param radius required radius
         */
        public LocationIsNotNearPoints(
            List<Location> points,
            float radius
        ) {
            super(CATEGORY, String.format(Locale.ENGLISH, DETAILS_PATTERN, points, radius));
        }
    }

    /**
     * Subclass of {@link LocationStatus} for the case when location is not near points.
     */
    public static class ExpiredByTimeout extends LocationStatus {

        private static final String CATEGORY = "Request for location expired by timeout";
        private static final String DETAILS = "";

        /**
         * Constructor for {@link ExpiredByTimeout}.
         */
        public ExpiredByTimeout() {
            super(CATEGORY, DETAILS);
        }
    }
}

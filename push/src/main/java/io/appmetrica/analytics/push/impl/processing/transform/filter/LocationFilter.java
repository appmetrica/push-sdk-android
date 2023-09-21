package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.location.Location;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.impl.location.provider.FilterLocationProvider;
import io.appmetrica.analytics.push.impl.location.verifier.AccuracyVerifier;
import io.appmetrica.analytics.push.impl.location.verifier.CompositeVerifier;
import io.appmetrica.analytics.push.impl.location.verifier.PointsVerifier;
import io.appmetrica.analytics.push.impl.location.verifier.RecencyVerifier;
import io.appmetrica.analytics.push.location.DetailedLocation;
import io.appmetrica.analytics.push.location.LocationProvider;
import io.appmetrica.analytics.push.location.LocationStatus;
import io.appmetrica.analytics.push.model.Coordinates;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import java.util.List;
import java.util.concurrent.TimeUnit;

class LocationFilter implements PushFilter {

    private static final long REQUEST_TIMEOUT_SECONDS = 30L;

    private static final int DEFAULT_RADIUS = 2000;
    private static final long DEFAULT_MIN_RECENCY = TimeUnit.DAYS.toSeconds(1);
    private static final long DEFAULT_MIN_ACCURACY = 500L;
    private static final boolean DEFAULT_IS_PASSIVE_LOCATION = true;

    @NonNull
    private final LocationProvider locationProvider;

    @VisibleForTesting
    LocationFilter(@NonNull LocationProvider locationProvider) {
        this.locationProvider = locationProvider;
    }

    public LocationFilter() {
        this(new FilterLocationProvider());
    }

    @NonNull
    @Override
    public FilterResult filter(@NonNull PushMessage pushMessage) {
        Filters filters = pushMessage.getFilters();
        Coordinates coordinates = filters == null ? null : filters.getCoordinates();
        List<Location> points = coordinates == null ? null : coordinates.getPoints();
        if (points == null || points.isEmpty()) {
            return FilterResult.show();
        }

        DetailedLocation detailedLocation = locationProvider.getLocation(
            getProvider(filters),
            REQUEST_TIMEOUT_SECONDS,
            new CompositeVerifier(
                new AccuracyVerifier(getMinAccuracy(filters)),
                new RecencyVerifier(getMinRecency(filters)),
                new PointsVerifier(points, getRadius(coordinates))
            )
        );

        LocationStatus locationStatus = detailedLocation.getLocationStatus();
        if (locationStatus.isSuccess()) {
            return FilterResult.show();
        } else {
            return FilterResult.silence(locationStatus.getCategory(), locationStatus.getDetails());
        }
    }

    private static int getRadius(@NonNull Coordinates coordinates) {
        Integer radius = coordinates.getRadius();
        return radius != null ? radius : DEFAULT_RADIUS;
    }

    private static Long getMinRecency(@NonNull Filters filters) {
        Long minRecency = filters.getMinRecency();
        return minRecency != null ? minRecency : DEFAULT_MIN_RECENCY;
    }

    private static Long getMinAccuracy(@NonNull Filters filters) {
        Integer minAccuracy = filters.getMinAccuracy();
        return minAccuracy != null ? minAccuracy : DEFAULT_MIN_ACCURACY;
    }

    @NonNull
    private static String getProvider(@NonNull Filters filters) {
        Boolean isPassiveLocation = filters.getPassiveLocation();
        if (isPassiveLocation != null) {
            if (isPassiveLocation) {
                return LocationManager.PASSIVE_PROVIDER;
            }
        } else {
            if (DEFAULT_IS_PASSIVE_LOCATION) {
                return LocationManager.PASSIVE_PROVIDER;
            }
        }
        return "";
    }
}

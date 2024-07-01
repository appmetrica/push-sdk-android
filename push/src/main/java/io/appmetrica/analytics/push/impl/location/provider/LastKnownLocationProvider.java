package io.appmetrica.analytics.push.impl.location.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.utils.PermissionHelper;
import io.appmetrica.analytics.push.impl.utils.SystemServiceHelper;
import io.appmetrica.analytics.push.location.DetailedLocation;
import io.appmetrica.analytics.push.location.LocationProvider;
import io.appmetrica.analytics.push.location.LocationStatus;
import io.appmetrica.analytics.push.location.LocationVerifier;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;

public class LastKnownLocationProvider implements LocationProvider {

    private static final String PROVIDER_NAME = "LastKnownLocationProvider";

    @NonNull
    private final Context context;

    public LastKnownLocationProvider(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DetailedLocation getLocation(
        @NonNull final String provider,
        final long requestTimeoutSeconds,
        @NonNull final LocationVerifier locationVerifier
    ) {
        PublicLogger.INSTANCE.info("Trying get last known location");
        final LocationManager manager = SystemServiceHelper.getLocationManager(context);
        if (manager == null) {
            PublicLogger.INSTANCE.info("LocationManager is null");
            return new DetailedLocation(null, new LocationStatus.LocationManagerIsNull());
        }
        for (String locationProvider : manager.getProviders(true)) {
            Location location = getLastKnownLocation(manager, locationProvider);
            if (location != null && locationVerifier.verifyLocation(location).isSuccess()) {
                return new DetailedLocation(location, new LocationStatus.Success());
            }
        }
        return new DetailedLocation(null, new LocationStatus.LocationProviderReturnedNull(PROVIDER_NAME));
    }

    @Nullable
    @SuppressLint("MissingPermission") // lint doesn't see the permissions check
    private Location getLastKnownLocation(@NonNull LocationManager manager, @NonNull String provider) {
        try {
            if (PermissionHelper.isAccessLocationGranted(context, provider)) {
                return manager.getLastKnownLocation(provider);
            }
        } catch (Throwable e) {
            PublicLogger.INSTANCE.error(e, "Failed to get last known location");
        }
        return null;
    }
}

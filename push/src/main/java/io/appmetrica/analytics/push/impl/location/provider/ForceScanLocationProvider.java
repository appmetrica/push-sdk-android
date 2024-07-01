package io.appmetrica.analytics.push.impl.location.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.utils.PermissionHelper;
import io.appmetrica.analytics.push.impl.utils.SystemServiceHelper;
import io.appmetrica.analytics.push.impl.utils.executers.SingleExecutor;
import io.appmetrica.analytics.push.location.DetailedLocation;
import io.appmetrica.analytics.push.location.LocationProvider;
import io.appmetrica.analytics.push.location.LocationStatus;
import io.appmetrica.analytics.push.location.LocationVerifier;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ForceScanLocationProvider implements LocationProvider {

    private static final String TAG = "[ForceScanLocationProvider]";
    private static final String PROVIDER_NAME = "ForceScanLocationProvider";

    @NonNull
    private final Context context;

    @Nullable
    private PushLocationListener listener = null;

    public ForceScanLocationProvider(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DetailedLocation getLocation(
        @NonNull final String provider,
        final long requestTimeoutSeconds,
        @NonNull final LocationVerifier locationVerifier
    ) {
        PublicLogger.INSTANCE.info("Trying request new location from %s provider", provider);
        if (!PermissionHelper.isAccessLocationGranted(context, provider)) {
            PublicLogger.INSTANCE.info("Location permissions is not granted for %s", provider);
            return new DetailedLocation(null, new LocationStatus.PermissionIsNotGranted(provider));
        }
        final LocationManager manager = SystemServiceHelper.getLocationManager(context);
        if (manager == null) {
            PublicLogger.INSTANCE.info("LocationManager is null");
            return new DetailedLocation(null, new LocationStatus.LocationManagerIsNull());
        }
        new SingleExecutor(new SingleExecutor.Runnable() {
            @Override
            @SuppressLint("MissingPermission")
            public void run(@NonNull CountDownLatch countDownLatch) {
                removeListener(manager);
                listener = new PushLocationListener(countDownLatch, locationVerifier);
                try {
                    manager.requestLocationUpdates(provider, 0, 0, listener, getLooper());
                } catch (Throwable e) {
                    PublicLogger.INSTANCE.error(e, e.getMessage());
                }
            }
        }).run(requestTimeoutSeconds, TimeUnit.SECONDS);
        Location location = listener != null ? listener.getLocation() : null;
        removeListener(manager);
        if (location != null) {
            return new DetailedLocation(location, new LocationStatus.Success());
        } else {
            return new DetailedLocation(null, new LocationStatus.LocationProviderReturnedNull(PROVIDER_NAME));
        }
    }

    @SuppressLint("MissingPermission")
    private void removeListener(@NonNull LocationManager manager) {
        if (listener != null) {
            manager.removeUpdates(listener);
        }
        listener = null;
    }

    private static class PushLocationListener implements LocationListener {

        @NonNull
        private final CountDownLatch countDownLatch;
        @NonNull
        private final LocationVerifier locationVerifier;

        @Nullable
        private volatile Location deviceLocation = null;

        public PushLocationListener(
            @NonNull final CountDownLatch countDownLatch,
            @NonNull final LocationVerifier locationVerifier
        ) {
            this.countDownLatch = countDownLatch;
            this.locationVerifier = locationVerifier;
        }

        @Nullable
        public Location getLocation() {
            return deviceLocation;
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            DebugLogger.INSTANCE.info(TAG, "Get location %s", location.toString());
            if (locationVerifier.verifyLocation(location).isSuccess()) {
                deviceLocation = location;
                countDownLatch.countDown();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
        }
    }
}

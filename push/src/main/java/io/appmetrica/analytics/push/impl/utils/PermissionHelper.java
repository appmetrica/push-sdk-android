package io.appmetrica.analytics.push.impl.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import java.util.Arrays;
import java.util.List;

public abstract class PermissionHelper {

    private static final List<String> FINE_LOCATION_REQUIRED_PROVIDERS = Arrays.asList(
        LocationManager.PASSIVE_PROVIDER,
        LocationManager.GPS_PROVIDER
    );

    public static boolean isAccessLocationGranted(@NonNull Context context) {
        return isAccessLocationGranted(context, null);
    }

    public static boolean isAccessLocationGranted(@NonNull Context context, @Nullable String provider) {
        if (provider != null && FINE_LOCATION_REQUIRED_PROVIDERS.contains(provider)) {
            return isAccessFineLocationGranted(context);
        } else {
            return isAccessFineLocationGranted(context) || isAccessCoarseLocationGranted(context);
        }
    }

    public static boolean isAccessFineLocationGranted(@NonNull Context context) {
        return isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static boolean isAccessCoarseLocationGranted(@NonNull Context context) {
        return isPermissionGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private static boolean isPermissionGranted(@NonNull Context context, @NonNull String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}

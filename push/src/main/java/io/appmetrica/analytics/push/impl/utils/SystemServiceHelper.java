package io.appmetrica.analytics.push.impl.utils;

import android.content.Context;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger;

public abstract class SystemServiceHelper {

    @Nullable
    public static LocationManager getLocationManager(@NonNull Context context) {
        try {
            return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        } catch (Throwable t) {
            PublicLogger.e("Failed to get location manager", t);
        }
        return null;
    }
}

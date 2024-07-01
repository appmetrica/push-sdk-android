package io.appmetrica.analytics.push.impl.utils;

import android.content.Context;
import android.location.LocationManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;

public abstract class SystemServiceHelper {

    @Nullable
    public static LocationManager getLocationManager(@NonNull Context context) {
        try {
            return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        } catch (Throwable t) {
            PublicLogger.INSTANCE.error(t, "Failed to get location manager");
        }
        return null;
    }
}

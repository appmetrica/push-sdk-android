package io.appmetrica.analytics.push.impl.location;

import android.location.Location;
import androidx.annotation.NonNull;
import java.util.concurrent.TimeUnit;

abstract class LocationUtilsPreJellyBean {

    static long getRecency(@NonNull Location location) {
        return TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis() - location.getTime());
    }
}

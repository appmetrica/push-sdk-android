package io.appmetrica.analytics.push.impl.location;

import android.location.Location;
import android.os.Build;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import io.appmetrica.analytics.push.coreutils.internal.utils.DoNotInline;

@DoNotInline
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
abstract class LocationUtilsPostJellyBean {

    static long getRecency(@NonNull Location location) {
        return SystemClock.elapsedRealtimeNanos() - location.getElapsedRealtimeNanos();
    }
}

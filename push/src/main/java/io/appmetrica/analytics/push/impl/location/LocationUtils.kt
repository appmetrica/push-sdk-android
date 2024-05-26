package io.appmetrica.analytics.push.impl.location

import android.location.Location
import android.os.SystemClock

object LocationUtils {

    @JvmStatic
    fun getRecency(location: Location): Long = SystemClock.elapsedRealtimeNanos() - location.elapsedRealtimeNanos
}

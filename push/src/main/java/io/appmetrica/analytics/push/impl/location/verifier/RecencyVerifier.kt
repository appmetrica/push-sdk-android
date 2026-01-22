package io.appmetrica.analytics.push.impl.location.verifier

import android.location.Location
import io.appmetrica.analytics.push.impl.location.LocationUtils
import io.appmetrica.analytics.push.location.LocationStatus
import io.appmetrica.analytics.push.location.LocationVerifier
import java.util.concurrent.TimeUnit

internal class RecencyVerifier(
    private val minRecency: Long?
) : LocationVerifier {

    private val minRecencyInNanos = minRecency?.let { TimeUnit.SECONDS.toNanos(minRecency) }

    override fun verifyLocation(location: Location): LocationStatus {
        val recency = LocationUtils.getRecency(location)
        if (minRecencyInNanos != null && recency > minRecencyInNanos) {
            return LocationStatus.LocationIsNotRecent(recency, minRecencyInNanos)
        }
        return LocationStatus.Success()
    }
}

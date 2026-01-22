package io.appmetrica.analytics.push.impl.location.verifier

import android.location.Location
import io.appmetrica.analytics.push.location.LocationStatus
import io.appmetrica.analytics.push.location.LocationVerifier

internal class AccuracyVerifier(
    private val minAccuracy: Long?
) : LocationVerifier {

    override fun verifyLocation(location: Location): LocationStatus {
        if (minAccuracy != null && location.accuracy > minAccuracy) {
            return LocationStatus.LocationIsNotAccurate(location.accuracy, minAccuracy)
        }
        return LocationStatus.Success()
    }
}

package io.appmetrica.analytics.push.impl.location.verifier

import android.location.Location
import io.appmetrica.analytics.push.location.LocationStatus
import io.appmetrica.analytics.push.location.LocationVerifier

class PointsVerifier(
    private val points: List<Location?>,
    private val radius: Float
) : LocationVerifier {

    override fun verifyLocation(location: Location): LocationStatus {
        if (!isLocationNearPoints(location)) {
            return LocationStatus.LocationIsNotNearPoints(points, radius)
        }
        return LocationStatus.Success()
    }

    private fun isLocationNearPoints(
        location: Location
    ): Boolean {
        points.filterNotNull().forEach { point ->
            if (location.distanceTo(point) <= radius) {
                return true
            }
        }
        return false
    }
}

package io.appmetrica.analytics.push.impl.location.provider

import io.appmetrica.analytics.push.location.DetailedLocation
import io.appmetrica.analytics.push.location.LocationProvider
import io.appmetrica.analytics.push.location.LocationVerifier

class FilterLocationProvider : LocationProvider {

    override fun getLocation(
        provider: String,
        requestTimeoutSeconds: Long,
        locationVerifier: LocationVerifier
    ): DetailedLocation {
        return CustomLocationProvider().getLocation(
            provider,
            requestTimeoutSeconds,
            locationVerifier
        )
    }
}

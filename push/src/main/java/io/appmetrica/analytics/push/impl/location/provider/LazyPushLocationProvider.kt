package io.appmetrica.analytics.push.impl.location.provider

import android.content.Context
import io.appmetrica.analytics.push.location.DetailedLocation
import io.appmetrica.analytics.push.location.LocationProvider
import io.appmetrica.analytics.push.location.LocationStatus
import io.appmetrica.analytics.push.location.LocationVerifier

class LazyPushLocationProvider(
    private val context: Context
) : LocationProvider {

    private val providerName = "LazyPushLocationProvider"

    override fun getLocation(
        provider: String,
        requestTimeoutSeconds: Long,
        locationVerifier: LocationVerifier
    ): DetailedLocation {
        getLocationProviders().forEach {
            val detailedLocation = it.getLocation(provider, requestTimeoutSeconds, locationVerifier)
            val location = detailedLocation.location
            if (location != null) {
                return detailedLocation
            }
        }
        return DetailedLocation(null, LocationStatus.LocationProviderReturnedNull(providerName))
    }

    private fun getLocationProviders() = listOfNotNull(
        CustomLocationProvider(),
        LastKnownLocationProvider(context),
        ForceScanLocationProvider(context),
        LastKnownLocationProvider(context)
    )
}

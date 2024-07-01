package io.appmetrica.analytics.push.impl.location.provider

import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.impl.location.LocationProviderHolder
import io.appmetrica.analytics.push.impl.utils.executers.SingleExecutor
import io.appmetrica.analytics.push.location.DetailedLocation
import io.appmetrica.analytics.push.location.LocationProvider
import io.appmetrica.analytics.push.location.LocationStatus
import io.appmetrica.analytics.push.location.LocationVerifier
import io.appmetrica.analytics.push.logger.internal.PublicLogger
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class CustomLocationProvider : LocationProvider {

    private val providerName = "CustomLocationProvider"

    @Volatile
    private var detailedLocation: DetailedLocation? = null

    override fun getLocation(
        provider: String,
        requestTimeoutSeconds: Long,
        locationVerifier: LocationVerifier
    ): DetailedLocation {
        val customLocationProvider = LocationProviderHolder.provider
            ?: return DetailedLocation(null, LocationStatus.CustomLocationProviderIsNull())

        SingleExecutor(object : SingleExecutor.Runnable() {
            override fun run(countDownLatch: CountDownLatch) {
                try {
                    detailedLocation = customLocationProvider.getLocation(
                        provider,
                        requestTimeoutSeconds,
                        locationVerifier
                    )
                } catch (e: Throwable) {
                    PublicLogger.error(e, "Custom location provider failed to get location")
                    TrackersHub.getInstance().reportError("Custom location provider failed to get location", e)
                }
                countDownLatch.countDown()
            }
        }).run(requestTimeoutSeconds, TimeUnit.SECONDS)

        val currentDetailedLocation = detailedLocation
            ?: return DetailedLocation(null, LocationStatus.ExpiredByTimeout())

        val currentLocationStatus = currentDetailedLocation.locationStatus
        if (!currentLocationStatus.isSuccess) {
            return currentDetailedLocation
        }

        val currentLocation = currentDetailedLocation.location
            ?: return DetailedLocation(null, LocationStatus.LocationProviderReturnedNull(providerName))
        val actualLocationStatus = locationVerifier.verifyLocation(currentLocation)
        return if (!actualLocationStatus.isSuccess) {
            DetailedLocation(null, actualLocationStatus)
        } else {
            currentDetailedLocation
        }
    }
}

package io.appmetrica.analytics.push.impl.lazypush.rule

import android.content.Context
import android.location.Location
import android.location.LocationManager
import io.appmetrica.analytics.push.impl.location.provider.LazyPushLocationProvider
import io.appmetrica.analytics.push.impl.location.verifier.AccuracyVerifier
import io.appmetrica.analytics.push.impl.location.verifier.CompositeVerifier
import io.appmetrica.analytics.push.impl.location.verifier.RecencyVerifier
import io.appmetrica.analytics.push.impl.processing.transform.TransformFailureException
import io.appmetrica.analytics.push.lazypush.LazyPushTransformRule
import io.appmetrica.analytics.push.model.LocationRequestInfo
import java.util.concurrent.TimeUnit

class LocationTransformRule(
    context: Context,
    private val locationRequestInfo: LocationRequestInfo?
) : LazyPushTransformRule {

    private var cachedLocation = NOT_REQUEST_LOCATION
    private val lazyPushLocationProvider = LazyPushLocationProvider(context)

    override fun getPatternList() = listOf(LAT_PATTERN, LON_PATTERN)

    override fun getNewValue(pattern: String): String {
        if (cachedLocation === NOT_REQUEST_LOCATION) {
            val detailedLocation = lazyPushLocationProvider.getLocation(
                getLocationProvider(locationRequestInfo),
                getRequestTimeoutSeconds(locationRequestInfo),
                CompositeVerifier(
                    AccuracyVerifier(getMinAccuracy(locationRequestInfo)),
                    RecencyVerifier(getMinRecency(locationRequestInfo))
                )
            )
            val location = detailedLocation.location
            val locationStatus = detailedLocation.locationStatus
            if (location == null) {
                throw TransformFailureException(locationStatus.category, locationStatus.details)
            }
            cachedLocation = location
        }
        return if (LAT_PATTERN == pattern) {
            "${cachedLocation.latitude}"
        } else if (LON_PATTERN == pattern) {
            "${cachedLocation.longitude}"
        } else {
            ""
        }
    }

    private fun getLocationProvider(location: LocationRequestInfo?): String {
        val locationProvider = location?.provider
        return locationProvider ?: LocationManager.NETWORK_PROVIDER
    }

    private fun getRequestTimeoutSeconds(info: LocationRequestInfo?): Long {
        val requestTimeoutSeconds = info?.requestTimeoutSeconds
        return requestTimeoutSeconds ?: DEFAULT_REQUEST_TIMEOUT_SECONDS
    }

    private fun getMinRecency(info: LocationRequestInfo?): Long {
        val minRecency = info?.minRecency
        return minRecency ?: DEFAULT_MIN_RECENCY
    }

    private fun getMinAccuracy(info: LocationRequestInfo?): Long {
        val minAccuracy = info?.minAccuracy
        return minAccuracy?.toLong() ?: DEFAULT_MIN_ACCURACY.toLong()
    }

    companion object {
        private const val LAT_PATTERN = "lat"
        private const val LON_PATTERN = "lon"
        private const val DEFAULT_REQUEST_TIMEOUT_SECONDS: Long = 30
        private val DEFAULT_MIN_RECENCY = TimeUnit.MINUTES.toSeconds(5)
        private const val DEFAULT_MIN_ACCURACY = 500
        private val NOT_REQUEST_LOCATION = Location("")
    }
}

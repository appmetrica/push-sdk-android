package io.appmetrica.analytics.push.impl.location.provider

import io.appmetrica.analytics.push.location.DetailedLocation
import io.appmetrica.analytics.push.location.LocationStatus
import io.appmetrica.analytics.push.location.LocationVerifier
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.MockedConstructionRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class LazyPushLocationProviderTest : CommonTest() {

    private val provider = "provider"
    private val requestTimeoutSeconds = 12342L
    private val locationVerifier: LocationVerifier = mock()

    private val customDetailedLocation: DetailedLocation = mock()
    @get:Rule
    val customLocationProviderRule = MockedConstructionRule(CustomLocationProvider::class.java) { mock, _ ->
        whenever(
            mock.getLocation(
                provider,
                requestTimeoutSeconds,
                locationVerifier
            )
        ).thenReturn(customDetailedLocation)
    }

    private val forceScanDetailedLocation: DetailedLocation = mock()
    @get:Rule
    val forceScanLocationProviderRule = MockedConstructionRule(ForceScanLocationProvider::class.java) { mock, _ ->
        whenever(
            mock.getLocation(
                provider,
                requestTimeoutSeconds,
                locationVerifier
            )
        ).thenReturn(forceScanDetailedLocation)
    }

    private val firstLastKnownDetailedLocation: DetailedLocation = mock()
    private val secondLastKnownDetailedLocation: DetailedLocation = mock()
    @get:Rule
    val lastKnownLocationProviderRule = MockedConstructionRule(LastKnownLocationProvider::class.java) { mock, context ->
        whenever(
            mock.getLocation(
                provider,
                requestTimeoutSeconds,
                locationVerifier
            )
        ).thenReturn(
            when (context.count) {
                1 -> firstLastKnownDetailedLocation
                2 -> secondLastKnownDetailedLocation
                else -> null
            }
        )
    }

    private val locationProvider = LazyPushLocationProvider(mock())

    @Test
    fun getLocationFromCustomLocationProvider() {
        whenever(customDetailedLocation.location).thenReturn(mock())

        assertThat(locationProvider.getLocation(provider, requestTimeoutSeconds, locationVerifier))
            .isSameAs(customDetailedLocation)
    }

    @Test
    fun getLocationFromFirstLastKnownLocationProvider() {
        whenever(firstLastKnownDetailedLocation.location).thenReturn(mock())

        assertThat(locationProvider.getLocation(provider, requestTimeoutSeconds, locationVerifier))
            .isSameAs(firstLastKnownDetailedLocation)
    }

    @Test
    fun getLocationFromForceScanLocationProvider() {
        whenever(forceScanDetailedLocation.location).thenReturn(mock())

        assertThat(locationProvider.getLocation(provider, requestTimeoutSeconds, locationVerifier))
            .isSameAs(forceScanDetailedLocation)
    }

    @Test
    fun getLocationFromSecondLastKnownLocationProvider() {
        whenever(secondLastKnownDetailedLocation.location).thenReturn(mock())

        assertThat(locationProvider.getLocation(provider, requestTimeoutSeconds, locationVerifier))
            .isSameAs(secondLastKnownDetailedLocation)
    }

    @Test
    fun getLocationIfAllLocationIsNull() {
        val detailedLocation = locationProvider.getLocation(provider, requestTimeoutSeconds, locationVerifier)
        assertThat(detailedLocation.location).isNull()
        assertThat(detailedLocation.locationStatus)
            .isInstanceOf(LocationStatus.LocationProviderReturnedNull::class.java)
    }
}

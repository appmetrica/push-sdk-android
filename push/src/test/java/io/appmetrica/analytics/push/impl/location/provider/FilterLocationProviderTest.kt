package io.appmetrica.analytics.push.impl.location.provider

import io.appmetrica.analytics.push.location.DetailedLocation
import io.appmetrica.analytics.push.location.LocationVerifier
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.MockedConstructionRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class FilterLocationProviderTest : CommonTest() {

    private val provider = "provider"
    private val requestTimeoutSeconds = 12342L
    private val locationVerifier: LocationVerifier = mock()

    private val detailedLocation: DetailedLocation = mock()

    @get:Rule
    val customLocationProviderRule = MockedConstructionRule(CustomLocationProvider::class.java) { mock, _ ->
        whenever(
            mock.getLocation(
                provider,
                requestTimeoutSeconds,
                locationVerifier
            )
        ).thenReturn(detailedLocation)
    }

    private val locationProvider = FilterLocationProvider()

    @Test
    fun getLocation() {
        assertThat(
            locationProvider.getLocation(
                provider,
                requestTimeoutSeconds,
                locationVerifier
            )
        ).isSameAs(detailedLocation)
    }
}

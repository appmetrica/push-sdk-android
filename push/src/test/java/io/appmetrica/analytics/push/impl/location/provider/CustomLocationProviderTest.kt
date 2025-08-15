package io.appmetrica.analytics.push.impl.location.provider

import android.location.Location
import io.appmetrica.analytics.push.impl.location.LocationProviderHolder
import io.appmetrica.analytics.push.location.DetailedLocation
import io.appmetrica.analytics.push.location.LocationProvider
import io.appmetrica.analytics.push.location.LocationStatus
import io.appmetrica.analytics.push.location.LocationVerifier
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class CustomLocationProviderTest : CommonTest() {

    private val provider = "provider"
    private val requestTimeoutSeconds = 2L

    private val locationStatusFromProvider: LocationStatus = mock {
        whenever(it.isSuccess).thenReturn(true)
    }
    private val locationFromProvider: Location = mock()
    private val detailedLocationFromProvider: DetailedLocation = mock {
        whenever(it.location).thenReturn(locationFromProvider)
        whenever(it.locationStatus).thenReturn(locationStatusFromProvider)
    }

    private val locationStatusFromVerifier: LocationStatus = mock {
        whenever(it.isSuccess).thenReturn(true)
    }
    private val locationVerifier: LocationVerifier = mock {
        whenever(it.verifyLocation(locationFromProvider)).thenReturn(locationStatusFromVerifier)
    }

    private val customLocationProvider: LocationProvider = mock {
        whenever(it.getLocation(provider, requestTimeoutSeconds, locationVerifier))
            .thenReturn(detailedLocationFromProvider)
    }

    @get:Rule
    val locationProviderHolderRule = MockedStaticRule(LocationProviderHolder::class.java)

    private val locationProvider = CustomLocationProvider()

    @Before
    fun setUp() {
        whenever(LocationProviderHolder.provider).thenReturn(customLocationProvider)
    }

    @Test
    fun getLocation() {
        val currentDetailedLocation = locationProvider.getLocation(
            provider,
            requestTimeoutSeconds,
            locationVerifier
        )
        SoftAssertions().apply {
            assertThat(currentDetailedLocation.location).isEqualTo(locationFromProvider)
            assertThat(currentDetailedLocation.locationStatus).isEqualTo(locationStatusFromProvider)
            assertAll()
        }
    }

    @Test
    fun getLocationIfStatusIsNotSuccess() {
        whenever(locationStatusFromProvider.isSuccess).thenReturn(false)

        val currentDetailedLocation = locationProvider.getLocation(
            provider,
            requestTimeoutSeconds,
            locationVerifier
        )
        Assertions.assertThat(currentDetailedLocation).isSameAs(detailedLocationFromProvider)
    }

    @Test
    fun getLocationIfLocationIsNull() {
        whenever(detailedLocationFromProvider.location).thenReturn(null)

        val currentDetailedLocation = locationProvider.getLocation(
            provider,
            requestTimeoutSeconds,
            locationVerifier
        )
        SoftAssertions().apply {
            assertThat(currentDetailedLocation.location).isNull()
            assertThat(currentDetailedLocation.locationStatus)
                .isInstanceOf(LocationStatus.LocationProviderReturnedNull::class.java)
            assertAll()
        }
    }

    @Test
    fun getLocationIfLocationVerificationFailed() {
        whenever(locationStatusFromVerifier.isSuccess).thenReturn(false)

        val currentDetailedLocation = locationProvider.getLocation(
            provider,
            requestTimeoutSeconds,
            locationVerifier
        )
        SoftAssertions().apply {
            assertThat(currentDetailedLocation.location).isEqualTo(null)
            assertThat(currentDetailedLocation.locationStatus).isEqualTo(locationStatusFromVerifier)
            assertAll()
        }
    }

    @Test
    fun getLocationIfTimeout() {
        val lateProvider = LocationProvider { _, requestTimeoutSeconds, _ ->
            Thread.sleep(TimeUnit.SECONDS.toMillis(requestTimeoutSeconds + 1))
            DetailedLocation(null, mock<LocationStatus.LocationIsNotRecent>())
        }
        whenever(LocationProviderHolder.provider).thenReturn(lateProvider)

        val currentDetailedLocation = locationProvider.getLocation(
            provider,
            2L,
            locationVerifier
        )
        SoftAssertions().apply {
            assertThat(currentDetailedLocation.location).isEqualTo(null)
            assertThat(currentDetailedLocation.locationStatus).isInstanceOf(LocationStatus.ExpiredByTimeout::class.java)
            assertAll()
        }
    }
}

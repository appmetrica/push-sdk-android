package io.appmetrica.analytics.push.impl.lazypush.rule

import android.content.Context
import android.location.Location
import io.appmetrica.analytics.push.impl.location.provider.LazyPushLocationProvider
import io.appmetrica.analytics.push.impl.location.verifier.AccuracyVerifier
import io.appmetrica.analytics.push.impl.location.verifier.CompositeVerifier
import io.appmetrica.analytics.push.impl.location.verifier.RecencyVerifier
import io.appmetrica.analytics.push.impl.processing.transform.TransformFailureException
import io.appmetrica.analytics.push.location.DetailedLocation
import io.appmetrica.analytics.push.location.LocationStatus
import io.appmetrica.analytics.push.model.LocationRequestInfo
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.MockedConstructionRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class LocationTransformRuleTest : CommonTest() {

    private val context: Context = mock()

    private val locationProvider = "locationProvider"
    private val requestTimeoutSeconds = 123L
    private val minAccuracy = 214124L
    private val minRecency = 2346L
    private val locationRequestInfo: LocationRequestInfo = mock {
        whenever(it.provider).thenReturn(locationProvider)
        whenever(it.requestTimeoutSeconds).thenReturn(requestTimeoutSeconds)
        whenever(it.minAccuracy).thenReturn(minAccuracy.toInt())
        whenever(it.minRecency).thenReturn(minRecency)
    }

    private val latitude = 2345342.toDouble()
    private val longitude = 124322L.toDouble()
    private val location: Location = mock {
        whenever(it.latitude).thenReturn(latitude)
        whenever(it.longitude).thenReturn(longitude)
    }
    private val locationStatus: LocationStatus = mock()
    private val detailedLocation: DetailedLocation = mock {
        whenever(it.location).thenReturn(location)
        whenever(it.locationStatus).thenReturn(locationStatus)
    }

    private lateinit var lazyPushLocationProvider: LazyPushLocationProvider
    @get:Rule
    val lazyPushLocationProviderRule = MockedConstructionRule(LazyPushLocationProvider::class.java) { mock, _ ->
        lazyPushLocationProvider = mock
        whenever(
            mock.getLocation(
                eq(locationProvider),
                eq(requestTimeoutSeconds),
                any()
            )
        ).thenReturn(detailedLocation)
    }

    @get:Rule
    val accuracyVerifierRule = MockedConstructionRule(AccuracyVerifier::class.java)
    @get:Rule
    val recencyVerifierRule = MockedConstructionRule(RecencyVerifier::class.java)
    @get:Rule
    val compositeVerifierRule = MockedConstructionRule(CompositeVerifier::class.java)

    private lateinit var locationTransformRule: LocationTransformRule

    @Before
    fun setUp() {
        locationTransformRule = LocationTransformRule(
            context,
            locationRequestInfo
        )
    }

    @Test
    fun getNewLatValue() {
        val value = locationTransformRule.getNewValue("lat")
        assertThat(value).isEqualTo(latitude.toString())
    }

    @Test
    fun getNewLonValue() {
        val value = locationTransformRule.getNewValue("lon")
        assertThat(value).isEqualTo(longitude.toString())
    }

    @Test
    fun getNewValueIfWrongPattern() {
        val value = locationTransformRule.getNewValue("some_pattern")
        assertThat(value).isEqualTo("")
    }

    @Test(expected = TransformFailureException::class)
    fun getNewValueIfLocationIsNull() {
        whenever(detailedLocation.location).thenReturn(null)
        locationTransformRule.getNewValue("lat")
    }

    @Test
    fun getLocationCalledOnlyOnce() {
        locationTransformRule.getNewValue("lat")
        locationTransformRule.getNewValue("lat")
        verify(lazyPushLocationProvider, times(1)).getLocation(any(), any(), any())
    }

    @Test
    fun correctVerifier() {
        locationTransformRule.getNewValue("lat")

        val compositeVerifier = compositeVerifierRule.constructionMock.constructed().firstOrNull()
        val accuracyVerifier = accuracyVerifierRule.constructionMock.constructed().firstOrNull()
        val recencyVerifier = recencyVerifierRule.constructionMock.constructed().firstOrNull()

        assertThat(compositeVerifierRule.argumentInterceptor.flatArguments())
            .containsExactly(
                arrayOf(
                    accuracyVerifier,
                    recencyVerifier
                )
            )
        assertThat(accuracyVerifierRule.argumentInterceptor.flatArguments())
            .containsExactly(
                minAccuracy
            )
        assertThat(recencyVerifierRule.argumentInterceptor.flatArguments())
            .containsExactly(
                minRecency
            )
        verify(lazyPushLocationProvider).getLocation(
            locationProvider,
            requestTimeoutSeconds,
            compositeVerifier!!
        )
    }

    @Test
    fun defaultLocationRequestInfoValues() {
        val locationRequestInfo: LocationRequestInfo = mock {
            whenever(it.provider).thenReturn(null)
            whenever(it.requestTimeoutSeconds).thenReturn(null)
            whenever(it.minAccuracy).thenReturn(null)
            whenever(it.minRecency).thenReturn(null)
        }
        val locationTransformRule = LocationTransformRule(
            context,
            locationRequestInfo
        )

        whenever(
            lazyPushLocationProvider.getLocation(
                any(),
                any(),
                any()
            )
        ).thenReturn(detailedLocation)

        locationTransformRule.getNewValue("lat")

        val compositeVerifier = compositeVerifierRule.constructionMock.constructed().firstOrNull()

        assertThat(accuracyVerifierRule.argumentInterceptor.flatArguments())
            .containsExactly(
                500L
            )
        assertThat(recencyVerifierRule.argumentInterceptor.flatArguments())
            .containsExactly(
                300L
            )
        verify(lazyPushLocationProvider).getLocation(
            "network",
            30L,
            compositeVerifier!!
        )
    }
}

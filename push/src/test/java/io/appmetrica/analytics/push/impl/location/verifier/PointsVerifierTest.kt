package io.appmetrica.analytics.push.impl.location.verifier

import android.location.Location
import io.appmetrica.analytics.push.location.LocationStatus
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PointsVerifierTest : CommonTest() {

    private val radius = 100.toFloat()

    private val nearPoint: Location = mock()
    private val exactPoint: Location = mock()
    private val farPoint: Location = mock()
    private val nullPoint: Location? = null

    private val location: Location = mock {
        whenever(it.distanceTo(nearPoint)).thenReturn(radius - 1)
        whenever(it.distanceTo(exactPoint)).thenReturn(radius)
        whenever(it.distanceTo(farPoint)).thenReturn(radius + 1)
    }

    @Test
    fun verifyLocation() {
        val verifier = PointsVerifier(
            listOf(nearPoint, exactPoint, farPoint),
            radius
        )

        assertThat(verifier.verifyLocation(location)).isInstanceOf(LocationStatus.Success::class.java)
    }

    @Test
    fun verifyLocationIfHasNullPoint() {
        val verifier = PointsVerifier(
            listOf(nullPoint, nearPoint, exactPoint, farPoint),
            radius
        )

        assertThat(verifier.verifyLocation(location)).isInstanceOf(LocationStatus.Success::class.java)
    }

    @Test
    fun verifyLocationIfNoNearPoint() {
        val verifier = PointsVerifier(
            listOf(exactPoint, farPoint),
            radius
        )

        assertThat(verifier.verifyLocation(location)).isInstanceOf(LocationStatus.Success::class.java)
    }

    @Test
    fun verifyLocationIfAllPointaAreFar() {
        val verifier = PointsVerifier(
            listOf(farPoint),
            radius
        )

        val status = verifier.verifyLocation(location)
        SoftAssertions().apply {
            assertThat(status).isInstanceOf(LocationStatus.LocationIsNotNearPoints::class.java)
            assertThat(status.category).isEqualTo("Location is not near points")
            assertAll()
        }
    }

    @Test
    fun verifyLocationIfHasOnlyNullPoint() {
        val verifier = PointsVerifier(
            listOf(nullPoint),
            radius
        )

        val status = verifier.verifyLocation(location)
        SoftAssertions().apply {
            assertThat(status).isInstanceOf(LocationStatus.LocationIsNotNearPoints::class.java)
            assertThat(status.category).isEqualTo("Location is not near points")
            assertAll()
        }
    }
}

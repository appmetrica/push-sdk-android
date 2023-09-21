package io.appmetrica.analytics.push.impl.location.verifier

import android.location.Location
import io.appmetrica.analytics.push.location.LocationStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AccuracyVerifierTest {

    private val location: Location = mock()

    @Test
    fun verifyLocation() {
        val minAccuracy = 100L
        val verifier = AccuracyVerifier(minAccuracy)

        whenever(location.accuracy).thenReturn((minAccuracy - 1).toFloat())

        assertThat(verifier.verifyLocation(location)).isInstanceOf(LocationStatus.Success::class.java)
    }

    @Test
    fun verifyLocationIfInaccurate() {
        val minAccuracy = 100L
        val verifier = AccuracyVerifier(minAccuracy)

        whenever(location.accuracy).thenReturn((minAccuracy + 1).toFloat())

        val status = verifier.verifyLocation(location)
        assertThat(status).isInstanceOf(LocationStatus.LocationIsNotAccurate::class.java)
        assertThat(status.details).isEqualTo("Got accuracy [101.000000], maximum allowed [100]")
    }

    @Test
    fun verifyLocationIfMinAccuracyIsNull() {
        val minAccuracy = null
        val verifier = AccuracyVerifier(minAccuracy)

        assertThat(verifier.verifyLocation(location)).isInstanceOf(LocationStatus.Success::class.java)
    }
}

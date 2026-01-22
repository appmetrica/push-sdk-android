package io.appmetrica.analytics.push.impl.location.verifier

import android.location.Location
import io.appmetrica.analytics.push.impl.location.LocationUtils
import io.appmetrica.analytics.push.location.LocationStatus
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.concurrent.TimeUnit

internal class RecencyVerifierTest : CommonTest() {

    private val minRecency = 100L
    private val minRecencyInNanos = TimeUnit.SECONDS.toNanos(minRecency)

    private val location: Location = mock()

    @get:Rule
    val locationUtilsRule = MockedStaticRule(LocationUtils::class.java)

    @Test
    fun verifyLocation() {
        val verifier = RecencyVerifier(minRecency)

        whenever(LocationUtils.getRecency(location)).thenReturn(minRecencyInNanos - 1)

        assertThat(verifier.verifyLocation(location)).isInstanceOf(LocationStatus.Success::class.java)
    }

    @Test
    fun verifyLocationIfNotRecent() {
        val verifier = RecencyVerifier(minRecency)

        whenever(LocationUtils.getRecency(location)).thenReturn(minRecencyInNanos + 1)

        val status = verifier.verifyLocation(location)
        SoftAssertions().apply {
            assertThat(status).isInstanceOf(LocationStatus.LocationIsNotRecent::class.java)
            assertThat(status.category).isEqualTo("Location is not recent")
            assertThat(status.details).isEqualTo("Got recency [100000000001], minimum allowed [100000000000]")
            assertAll()
        }
    }

    @Test
    fun verifyLocationIfRecencyIsNull() {
        val verifier = RecencyVerifier(null)

        assertThat(verifier.verifyLocation(location)).isInstanceOf(LocationStatus.Success::class.java)
    }
}

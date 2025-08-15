package io.appmetrica.analytics.push.impl.location.verifier

import io.appmetrica.analytics.push.location.LocationStatus
import io.appmetrica.analytics.push.location.LocationVerifier
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CompositeVerifierTest : CommonTest() {

    private val firstVerifierStatus: LocationStatus = mock {
        whenever(it.isSuccess).thenReturn(true)
    }
    private val secondVerifierStatus: LocationStatus = mock {
        whenever(it.isSuccess).thenReturn(true)
    }
    private val thirdVerifierStatus: LocationStatus = mock {
        whenever(it.isSuccess).thenReturn(true)
    }

    private val firstVerifier: LocationVerifier = mock {
        whenever(it.verifyLocation(any())).thenReturn(firstVerifierStatus)
    }
    private val secondVerifier: LocationVerifier = mock {
        whenever(it.verifyLocation(any())).thenReturn(secondVerifierStatus)
    }
    private val thirdVerifier: LocationVerifier = mock {
        whenever(it.verifyLocation(any())).thenReturn(thirdVerifierStatus)
    }

    private val verifier = CompositeVerifier(firstVerifier, secondVerifier, thirdVerifier)

    @Test
    fun verifyLocation() {
        assertThat(verifier.verifyLocation(mock())).isInstanceOf(LocationStatus.Success::class.java)
    }

    @Test
    fun verifyLocationIfFirstVerifierFailed() {
        whenever(firstVerifierStatus.isSuccess).thenReturn(false)

        assertThat(verifier.verifyLocation(mock())).isSameAs(firstVerifierStatus)
    }

    @Test
    fun verifyLocationIfSecondVerifierFailed() {
        whenever(secondVerifierStatus.isSuccess).thenReturn(false)

        assertThat(verifier.verifyLocation(mock())).isSameAs(secondVerifierStatus)
    }

    @Test
    fun verifyLocationIfLastVerifierFailed() {
        whenever(thirdVerifierStatus.isSuccess).thenReturn(false)

        assertThat(verifier.verifyLocation(mock())).isSameAs(thirdVerifierStatus)
    }
}

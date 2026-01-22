package io.appmetrica.analytics.push.impl.token.filter

import io.appmetrica.analytics.push.impl.token.event.TokenEvent
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock

internal class IsForceTokenEventFilterTest : CommonTest() {

    private val filter = IsForceTokenEventFilter()

    @Test
    fun shouldSend() {
        val tokenEvent: TokenEvent = mock {
            on { isForce }.thenReturn(true)
        }
        assertThat(filter.shouldSend(tokenEvent)).isTrue()
    }

    @Test
    fun shouldNotSend() {
        val tokenEvent: TokenEvent = mock {
            on { isForce }.thenReturn(false)
        }
        assertThat(filter.shouldSend(tokenEvent)).isFalse()
    }
}

package io.appmetrica.analytics.push.impl.token.filter

import io.appmetrica.analytics.push.impl.storage.Token
import io.appmetrica.analytics.push.impl.token.TokenManager
import io.appmetrica.analytics.push.impl.token.event.TokenEvent
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.concurrent.TimeUnit

internal class MinIntervalTokenEventFilterTest : CommonTest() {

    private val provider = "provider"
    private val oldToken = "old token"
    private val newToken = "new token"

    private val tokenManager: TokenManager = mock()

    private val filter = MinIntervalTokenEventFilter(tokenManager)

    @Test
    fun shouldSend() {
        val tokenEvent: TokenEvent = mock {
            on { provider }.thenReturn(provider)
            on { token }.thenReturn(newToken)
        }

        whenever(tokenManager.getToken(provider)).thenReturn(Token(oldToken, System.currentTimeMillis()))
        assertThat(filter.shouldSend(tokenEvent)).isTrue()
    }

    @Test
    fun shouldSendIfTokenManagerIsNull() {
        val tokenEvent: TokenEvent = mock {
            on { provider }.thenReturn(provider)
            on { token }.thenReturn(newToken)
        }

        assertThat(MinIntervalTokenEventFilter(null).shouldSend(tokenEvent)).isTrue()
        verify(tokenManager, never()).getToken(provider)
    }

    @Test
    fun shouldSendIfOldTokenIsNull() {
        val tokenEvent: TokenEvent = mock {
            on { provider }.thenReturn(provider)
            on { token }.thenReturn(newToken)
        }

        whenever(tokenManager.getToken(provider)).thenReturn(null)
        assertThat(filter.shouldSend(tokenEvent)).isTrue()
    }

    @Test
    fun shouldSendIfTokensAreEqual() {
        val tokenEvent: TokenEvent = mock {
            on { provider }.thenReturn(provider)
            on { token }.thenReturn(oldToken)
        }

        whenever(tokenManager.getToken(provider)).thenReturn(Token(oldToken, System.currentTimeMillis()))
        assertThat(filter.shouldSend(tokenEvent)).isFalse()
    }

    @Test
    fun shouldSendIfTokensAreEqualButOld() {
        val tokenEvent: TokenEvent = mock {
            on { provider }.thenReturn(provider)
            on { token }.thenReturn(oldToken)
        }

        whenever(tokenManager.getToken(provider)).thenReturn(
            Token(
                oldToken,
                System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)
            )
        )
        assertThat(filter.shouldSend(tokenEvent)).isTrue()
    }
}

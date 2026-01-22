package io.appmetrica.analytics.push.impl.token.filter

import io.appmetrica.analytics.push.impl.PushServiceControllerComposite
import io.appmetrica.analytics.push.impl.token.event.TokenEvent
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class PushServiceControllerTokenEventFilterTest : CommonTest() {

    private val provider = "provider"
    private val token = "token"
    private val tokenEvent: TokenEvent = mock {
        on { provider }.thenReturn(provider)
        on { token }.thenReturn(token)
    }

    private val pushServiceController: PushServiceControllerComposite = mock()

    private val filter = PushServiceControllerTokenEventFilter(pushServiceController)

    @Test
    fun shouldSend() {
        whenever(pushServiceController.shouldSendTokenForProvider(token, provider)).thenReturn(true)
        assertThat(filter.shouldSend(tokenEvent)).isTrue()
    }

    @Test
    fun shouldSendControllerReturnsFalse() {
        whenever(pushServiceController.shouldSendTokenForProvider(token, provider)).thenReturn(false)
        assertThat(filter.shouldSend(tokenEvent)).isFalse()
    }

    @Test
    fun shouldSendIfControllerIsNull() {
        assertThat(PushServiceControllerTokenEventFilter(null).shouldSend(tokenEvent)).isTrue()
    }

    @Test
    fun shouldSendIfNoToken() {
        val newTokenEvent: TokenEvent = mock {
            on { provider }.thenReturn(provider)
        }

        whenever(pushServiceController.shouldSendTokenForProvider("", provider)).thenReturn(false)
        assertThat(filter.shouldSend(newTokenEvent)).isFalse()
    }
}

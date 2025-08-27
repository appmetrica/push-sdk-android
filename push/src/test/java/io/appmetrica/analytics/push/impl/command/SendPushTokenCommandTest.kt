package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.token.event.TokenEvent
import io.appmetrica.analytics.push.impl.token.event.TokenEventProvider
import io.appmetrica.analytics.push.impl.token.filter.TokenEventFilter
import io.appmetrica.analytics.push.impl.token.filter.provider.TokenEventFilterProvider
import io.appmetrica.analytics.push.impl.token.processor.TokenEventProcessor
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SendPushTokenCommandTest : CommonTest() {

    private val context: Context = mock()

    private val appMetricaPushCore: AppMetricaPushCore = mock {
        on { isInitialized } doReturn true
    }
    @get:Rule
    val appMetricaPushCoreRule = staticRule<AppMetricaPushCore> {
        on { AppMetricaPushCore.getInstance(context) } doReturn appMetricaPushCore
    }

    private val tokenEventProvider: TokenEventProvider = mock()
    private val tokenEventFilterProvider: TokenEventFilterProvider = mock()
    private val tokenEventProcessor: TokenEventProcessor = mock()

    private val command = SendPushTokenCommand(
        tokenEventProvider,
        tokenEventFilterProvider,
        tokenEventProcessor
    )

    @Test
    fun execute() {
        val bundle = Bundle()

        val tokenEvent: TokenEvent = mock()
        whenever(tokenEventProvider.getTokenEvent(context, bundle)).thenReturn(tokenEvent)

        val tokenEventFilter: TokenEventFilter = mock()
        whenever(tokenEventFilterProvider.getTokenEventFilter(context, bundle)).thenReturn(tokenEventFilter)

        whenever(tokenEventFilter.shouldSend(tokenEvent)).thenReturn(true)

        command.execute(context, bundle)

        verify(tokenEventProcessor).process(context, tokenEvent)
    }

    @Test
    fun executeIfShouldNotSend() {
        val bundle = Bundle()

        val tokenEvent: TokenEvent = mock()
        whenever(tokenEventProvider.getTokenEvent(context, bundle)).thenReturn(tokenEvent)

        val tokenEventFilter: TokenEventFilter = mock()
        whenever(tokenEventFilterProvider.getTokenEventFilter(context, bundle)).thenReturn(tokenEventFilter)

        whenever(tokenEventFilter.shouldSend(tokenEvent)).thenReturn(false)

        command.execute(context, bundle)

        verifyNoInteractions(tokenEventProcessor)
    }

    @Test
    fun executeIfTokenEventIsNull() {
        val bundle = Bundle()

        whenever(tokenEventProvider.getTokenEvent(context, bundle)).thenReturn(null)

        command.execute(context, bundle)

        verifyNoInteractions(tokenEventFilterProvider, tokenEventProcessor)
    }

    @Test
    fun executeIfNotInitialized() {
        whenever(appMetricaPushCore.isInitialized).thenReturn(false)
        command.execute(context, Bundle())

        verifyNoInteractions(tokenEventProvider, tokenEventFilterProvider, tokenEventProcessor)
    }
}

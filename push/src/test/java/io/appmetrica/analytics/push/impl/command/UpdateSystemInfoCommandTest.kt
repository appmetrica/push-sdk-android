package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.system.event.SystemInfoEvent
import io.appmetrica.analytics.push.impl.system.event.SystemInfoEventProvider
import io.appmetrica.analytics.push.impl.system.processor.SystemInfoEventProcessor
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
internal class UpdateSystemInfoCommandTest : CommonTest() {

    private val context: Context = mock()

    private val appMetricaPushCore: AppMetricaPushCore = mock {
        on { isInitialized } doReturn true
    }
    @get:Rule
    val appMetricaPushCoreRule = staticRule<AppMetricaPushCore> {
        on { AppMetricaPushCore.getInstance(context) } doReturn appMetricaPushCore
    }

    private val systemInfoEventProvider: SystemInfoEventProvider = mock()
    private val systemInfoEventProcessor: SystemInfoEventProcessor = mock()

    private val command = UpdateSystemInfoCommand(
        systemInfoEventProvider,
        systemInfoEventProcessor
    )

    @Test
    fun execute() {
        val bundle = Bundle()

        val tokenEvent: SystemInfoEvent = mock()
        whenever(systemInfoEventProvider.getSystemInfoEvent(context, bundle)).thenReturn(tokenEvent)

        command.execute(context, bundle)

        verify(systemInfoEventProcessor).process(context, tokenEvent)
    }

    @Test
    fun executeIfTokenEventIsNull() {
        val bundle = Bundle()

        whenever(systemInfoEventProvider.getSystemInfoEvent(context, bundle)).thenReturn(null)

        command.execute(context, bundle)

        verifyNoInteractions(systemInfoEventProcessor)
    }

    @Test
    fun executeIfNotInitialized() {
        val bundle = Bundle()

        whenever(appMetricaPushCore.isInitialized).thenReturn(false)

        command.execute(context, bundle)

        verifyNoInteractions(systemInfoEventProvider, systemInfoEventProcessor)
    }
}

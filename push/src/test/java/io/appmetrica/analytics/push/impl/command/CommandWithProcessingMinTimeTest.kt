package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle
import android.os.Handler
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.LogRule
import io.appmetrica.analytics.push.testutils.constructionRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.clearInvocations
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class CommandWithProcessingMinTimeTest : CommonTest() {

    @get:Rule
    val logRule = LogRule()

    private val transport = "Some transport"
    private val delay = 14L

    private val context: Context = mock()
    private val command: Command = mock()

    private val pushBundle = Bundle().apply {
        putString(CoreConstants.EXTRA_TRANSPORT, transport)
        putLong(CoreConstants.MIN_PROCESSING_DELAY, delay)
    }

    @get:Rule
    val processPushCommandMinTimeProviderMockedConstructionRule = constructionRule<ProcessPushCommandMinTimeProvider>()

    @get:Rule
    val handlerMockedConstructionRule = constructionRule<Handler> {
        on { postDelayed(any(), any()) } doAnswer {
            Thread {
                Thread.sleep(it.arguments[1] as Long)
                (it.arguments[0] as Runnable).run()
            }.start()
            true
        }
    }

    private val delaySeconds = 5L

    private val commandProcessingMinTimeProvider: CommandProcessingMinTimeProvider = mock {
        on { get(context, pushBundle) } doReturn delaySeconds
    }

    private val commandWithProcessingMinTime: CommandWithProcessingMinTime by setUp {
        CommandWithProcessingMinTime(command, commandProcessingMinTimeProvider)
    }

    @Test
    fun `execute with zero delay`() {
        whenever(commandProcessingMinTimeProvider.get(any(), any())).thenReturn(0L)

        val startTime = System.currentTimeMillis()
        commandWithProcessingMinTime.execute(context, pushBundle)
        checkExecutionDelta(startTime, 0, 1)
        verify(command).execute(context, pushBundle)
    }

    @Test
    fun `execute with delay`() {
        var startTime = System.currentTimeMillis()
        commandWithProcessingMinTime.execute(context, pushBundle)
        checkExecutionDelta(startTime, delaySeconds - 1, delaySeconds + 1)
        verify(command).execute(context, pushBundle)

        clearInvocations(command)

        startTime = System.currentTimeMillis()
        commandWithProcessingMinTime.execute(context, pushBundle)
        checkExecutionDelta(startTime, 0, 1)
        verify(command).execute(context, pushBundle)
    }

    private fun checkExecutionDelta(startTime: Long, minLimit: Long, maxLimit: Long) {
        val finishTime = System.currentTimeMillis()
        val delta = finishTime - startTime
        assertThat(delta).isGreaterThanOrEqualTo(TimeUnit.SECONDS.toMillis(minLimit))
        assertThat(delta).isLessThanOrEqualTo(TimeUnit.SECONDS.toMillis(maxLimit))
    }
}

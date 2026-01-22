package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.PushServiceControllerComposite
import io.appmetrica.analytics.push.provider.api.PushServiceExecutionRestrictions
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.robolectric.ParameterizedRobolectricTestRunner

@RunWith(ParameterizedRobolectricTestRunner::class)
internal class ProcessPushCommandMinTimeProviderTest(
    private val delayFromPush: Long?,
    private val transportLimit: Long?,
    private val expectedValue: Long
) : CommonTest() {

    companion object {

        private const val transport = "Some transport"

        @ParameterizedRobolectricTestRunner.Parameters(name = "{0} && transport = {1} -> {2}")
        @JvmStatic
        fun data(): List<Array<Any?>> = listOf(
            arrayOf(null, null, 0L),
            arrayOf(null, 10L, 0L),
            arrayOf(null, 0L, 0L),
            arrayOf(0L, null, 0L),
            arrayOf(5L, null, 0L),
            arrayOf(5L, 10L, 5L),
            arrayOf(10L, 5L, 5L),
        )
    }

    private val context: Context = mock()

    private val pushServiceExecutionRestrictions: PushServiceExecutionRestrictions = mock {
        transportLimit?.let {
            on { maxTaskExecutionDurationSeconds } doReturn it
        }
    }

    private val pushServiceController: PushServiceControllerComposite = mock {
        if (transportLimit != null) {
            on { getExecutionRestrictions(transport) } doReturn pushServiceExecutionRestrictions
        } else {
            on { getExecutionRestrictions(transport) } doReturn null
        }
    }

    private val appMetricaPushCore: AppMetricaPushCore = mock {
        on { pushServiceController } doReturn pushServiceController
    }

    @get:Rule
    val appmetricaCoreMockedStaticRule = staticRule<AppMetricaPushCore> {
        on { AppMetricaPushCore.getInstance(context) } doReturn appMetricaPushCore
    }

    private val pushBundle = Bundle().apply {
        putString(CoreConstants.EXTRA_TRANSPORT, transport)
        delayFromPush?.let {
            putLong(CoreConstants.MIN_PROCESSING_DELAY, it)
        }
    }

    private val processPushCommandMinTimeProvider: ProcessPushCommandMinTimeProvider by setUp {
        ProcessPushCommandMinTimeProvider()
    }

    @Test
    fun normalize() {
        assertThat(processPushCommandMinTimeProvider.get(context, pushBundle)).isEqualTo(expectedValue)
    }
}

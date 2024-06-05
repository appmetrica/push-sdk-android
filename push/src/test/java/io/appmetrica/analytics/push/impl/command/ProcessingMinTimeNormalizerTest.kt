package io.appmetrica.analytics.push.impl.command

import android.content.Context
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
import org.junit.runners.Parameterized
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@RunWith(Parameterized::class)
class ProcessingMinTimeNormalizerTest(
    private val inputValue: Long,
    private val transportLimit: Long?,
    private val expectedValue: Long
) : CommonTest() {

    companion object {

        private const val defaultLimitSeconds = 0L
        private const val transportLimit = 15L
        private const val transport = "Some transport"

        @Parameterized.Parameters(name = "{0} && transport = {1} -> {2}")
        @JvmStatic
        fun data(): List<Array<Any?>> = listOf(
            arrayOf(-1L, transportLimit, defaultLimitSeconds),
            arrayOf(0L, transportLimit, 0L),
            arrayOf(5L, transportLimit, 5L),
            arrayOf(transportLimit + 1, transportLimit, transportLimit),
            arrayOf(-1L, null, defaultLimitSeconds),
            arrayOf(0L, null, defaultLimitSeconds),
            arrayOf(5L, null, defaultLimitSeconds),
            arrayOf(-1L, 0L, defaultLimitSeconds),
            arrayOf(0L, 0L, 0L),
            arrayOf(5L, 0L, 0L),
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

    private val processingMinTimeNormalizer: ProcessingMinTimeNormalizer by setUp { ProcessingMinTimeNormalizer() }

    @Test
    fun normalize() {
        assertThat(processingMinTimeNormalizer.normalize(context, inputValue, transport)).isEqualTo(expectedValue)
    }

}

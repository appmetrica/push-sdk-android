package io.appmetrica.analytics.push.impl.token.processor

import android.content.Context
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.token.TokenManager
import io.appmetrica.analytics.push.impl.token.event.TokenEvent
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.json.JSONObject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class UpdateTokenEventProcessorTest : CommonTest() {

    private val provider = "provider"
    private val token = "token"
    private val tokenEventJson = JSONObject()
        .put("data", "some_data")
    private val tokenEvent: TokenEvent = mock {
        on { provider }.thenReturn(provider)
        on { token }.thenReturn(token)
        on { toJson() } doReturn tokenEventJson
    }

    private val context: Context = mock()

    private val tokenManager: TokenManager = mock()
    private val appMetricaPushCore: AppMetricaPushCore = mock {
        on { tokenManager } doReturn tokenManager
    }
    @get:Rule
    val appMetricaPushCoreRule = staticRule<AppMetricaPushCore> {
        on { AppMetricaPushCore.getInstance(context) } doReturn appMetricaPushCore
    }

    private val pushMessageTrackerHub: PushMessageTrackerHub = mock()
    @get:Rule
    val pushMessageTrackerHubRule = staticRule<PushMessageTrackerHub> {
        on { PushMessageTrackerHub.getInstance() } doReturn pushMessageTrackerHub
    }

    private val processor = UpdateTokenEventProcessor()

    @Test
    fun process() {
        processor.process(context, tokenEvent)

        verify(tokenManager).saveToken(eq(token), eq(provider), any())
        verify(pushMessageTrackerHub).onPushTokenUpdated(tokenEventJson.toString(), provider)
    }
}

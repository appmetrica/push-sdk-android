package io.appmetrica.analytics.push.impl.system.processor

import android.content.Context
import io.appmetrica.analytics.push.impl.system.event.SystemInfoEvent
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.json.JSONObject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DefaultSystemInfoEventProcessorTest : CommonTest() {

    private val context: Context = mock()
    private val systemInfoEventJson = JSONObject()
        .put("data", "some_data")
    private val systemInfoEvent: SystemInfoEvent = mock {
        on { toJson() } doReturn systemInfoEventJson
    }

    private val pushMessageTrackerHub: PushMessageTrackerHub = mock()
    @get:Rule
    val pushMessageTrackerHubRule = staticRule<PushMessageTrackerHub> {
        on { PushMessageTrackerHub.getInstance() } doReturn pushMessageTrackerHub
    }

    private val processor = DefaultSystemInfoEventProcessor()

    @Test
    fun processWithSystemInfoEvent() {
        processor.process(context, systemInfoEvent)

        verify(pushMessageTrackerHub).onSystemInfoUpdated(
            "{\"data\":\"some_data\"}"
        )
    }
}

package io.appmetrica.analytics.push.impl.system.event

import io.appmetrica.analytics.push.impl.notification.NotificationStatus
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.Test
import org.mockito.kotlin.mock

class SystemInfoEventTest : CommonTest() {

    @Test
    fun toJsonWithNullNotificationStatus() {
        val event = SystemInfoEvent.Builder().build()
        val result = event.toJson()
        assertThat(result.toString()).isEqualTo("{}")
    }

    @Test
    fun toJsonWithNotificationStatus() {
        val notificationStatusJson = JSONObject()
            .put("data", "some_data")
        val notificationStatus: NotificationStatus = mock {
            on { toJson() }.thenReturn(notificationStatusJson)
        }
        val expectedJson = JSONObject()
            .put("notifications_status", notificationStatusJson)

        val event = SystemInfoEvent.Builder()
            .withNotificationStatus(notificationStatus)
            .build()
        val result = event.toJson()

        assertThat(result.toString()).isEqualTo(expectedJson.toString())
    }
}

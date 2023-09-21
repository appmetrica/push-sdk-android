package io.appmetrica.analytics.push.coreutils.internal.model

import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TransportPushMessageTest {

    @Test
    fun testExtractServiceType() {
        val root = JSONObject(
            mapOf(
                CoreConstants.PushMessage.SERVICE_TYPE to ServiceType.APPMETRICA_PUSH_SERVICE.value
            )
        )
        val bundle = Bundle().apply {
            putString(CoreConstants.PushMessage.ROOT_ELEMENT, root.toString())
        }

        val pushMessage = TransportPushMessage(bundle)
        assertThat(pushMessage.serviceType).isEqualTo(ServiceType.APPMETRICA_PUSH_SERVICE)
        assertThat(pushMessage.isOwnPush).isTrue
    }

    @Test
    fun testExtractUnknownServiceType() {
        val root = JSONObject(
            mapOf(
                CoreConstants.PushMessage.SERVICE_TYPE to -42
            )
        )
        val bundle = Bundle().apply {
            putString(CoreConstants.PushMessage.ROOT_ELEMENT, root.toString())
        }

        val pushMessage = TransportPushMessage(bundle)
        assertThat(pushMessage.serviceType).isEqualTo(ServiceType.UNKNOWN)
        assertThat(pushMessage.isOwnPush).isTrue
    }

    @Test
    fun testExtractRootElementIfPushMessageRootDoesNotExist() {
        val pushMessage = TransportPushMessage(Bundle())
        assertThat(pushMessage.serviceType).isEqualTo(ServiceType.UNKNOWN)
    }
}

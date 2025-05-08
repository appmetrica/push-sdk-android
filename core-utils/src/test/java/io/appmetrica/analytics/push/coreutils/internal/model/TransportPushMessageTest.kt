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
    fun extractServiceType() {
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
    fun extractUnknownServiceType() {
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
    fun extractRootElementIfPushMessageRootDoesNotExist() {
        val pushMessage = TransportPushMessage(Bundle())
        assertThat(pushMessage.serviceType).isEqualTo(ServiceType.UNKNOWN)
    }

    @Test
    fun processingMinTime() {
        val value = 124L
        val root = JSONObject(mapOf(CoreConstants.PushMessage.PROCESSING_MIN_TIME to value))

        val bundle = Bundle()
        bundle.putString(CoreConstants.PushMessage.ROOT_ELEMENT, root.toString())
        val pushMessage = TransportPushMessage(bundle)
        assertThat(pushMessage.processingMinTime).isEqualTo(value)
        assertThat(pushMessage.isOwnPush).isTrue()
    }

    @Test
    fun `processingMinTime for missing value`() {
        val root = JSONObject()
        val bundle = Bundle()
        bundle.putString(CoreConstants.PushMessage.ROOT_ELEMENT, root.toString())
        val pushMessage = TransportPushMessage(bundle)
        assertThat(pushMessage.processingMinTime).isNull()
        assertThat(pushMessage.isOwnPush).isTrue()
    }

    @Test
    fun `processingMinTime for invalid value`() {
        val root = JSONObject().apply { mapOf(CoreConstants.PushMessage.PROCESSING_MIN_TIME to "wron value") }
        val bundle = Bundle()
        bundle.putString(CoreConstants.PushMessage.ROOT_ELEMENT, root.toString())
        val pushMessage = TransportPushMessage(bundle)
        assertThat(pushMessage.processingMinTime).isNull()
        assertThat(pushMessage.isOwnPush).isTrue()
    }

    @Test
    fun `processingMinTime for missing root`() {
        val pushMessage = TransportPushMessage(Bundle())
        assertThat(pushMessage.processingMinTime).isNull()
        assertThat(pushMessage.isOwnPush).isFalse()
    }
}

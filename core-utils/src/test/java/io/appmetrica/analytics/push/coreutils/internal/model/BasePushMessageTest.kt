package io.appmetrica.analytics.push.coreutils.internal.model

import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants
import io.appmetrica.analytics.push.testutils.CommonTest
import org.assertj.core.api.SoftAssertions
import org.json.JSONObject
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class BasePushMessageTest : CommonTest() {

    @Test
    fun correctBasePushMessage() {
        val root = JSONObject(
            mapOf(
                "key" to "value"
            )
        )
        val bundle: Bundle = mock {
            on { getString(CoreConstants.PushMessage.ROOT_ELEMENT) } doReturn root.toString()
        }

        val pushMessage = BasePushMessage(bundle)
        val softly = SoftAssertions()
        softly.assertThat(pushMessage.rootString).isEqualTo(root.toString())
        softly.assertThat(pushMessage.root).isEqualToComparingFieldByField(root)
        softly.assertThat(pushMessage.isOwnPush).isTrue
        softly.assertAll()
    }

    @Test
    fun incorrectBasePushMessage() {
        val root = "root string"
        val bundle: Bundle = mock {
            on { getString(CoreConstants.PushMessage.ROOT_ELEMENT) } doReturn root
        }

        val pushMessage = BasePushMessage(bundle)
        val softly = SoftAssertions()
        softly.assertThat(pushMessage.rootString).isEqualTo(root)
        softly.assertThat(pushMessage.root).isNull()
        softly.assertThat(pushMessage.isOwnPush).isFalse
        softly.assertAll()
    }
}

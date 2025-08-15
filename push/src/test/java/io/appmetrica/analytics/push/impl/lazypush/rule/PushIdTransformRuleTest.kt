package io.appmetrica.analytics.push.impl.lazypush.rule

import io.appmetrica.analytics.push.impl.processing.transform.TransformFailureException
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.Rand.randomString
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PushIdTransformRuleTest : CommonTest() {

    private val pushId: String = randomString()

    @Test
    fun getNewValue() {
        val pushIdTransformRule = PushIdTransformRule(pushId)

        assertThat(pushIdTransformRule.getNewValue("pushId")).isEqualTo(pushId)
    }

    @Test(expected = TransformFailureException::class)
    fun getNewValueIfPushIdIsEmpty() {
        val pushIdTransformRule = PushIdTransformRule("")

        pushIdTransformRule.getNewValue("pushId")
    }

    @Test(expected = TransformFailureException::class)
    fun getNewValueIfPushIdIsNull() {
        val pushIdTransformRule = PushIdTransformRule(null)

        pushIdTransformRule.getNewValue("pushId")
    }
}

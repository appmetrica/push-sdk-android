package io.appmetrica.analytics.push.impl.lazypush.rule

import android.content.Context
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.push.testutils.MockedStaticRule
import io.appmetrica.analytics.push.testutils.Rand
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UuidTransformRuleTest {

    private val context: Context = mock()

    private val uuidTransformRule = UuidTransformRule(context)

    @get:Rule
    val appMetricaRule = MockedStaticRule(AppMetrica::class.java)

    @Test
    fun getNewValue() {
        val uuid = Rand.randomString()
        whenever(AppMetrica.getUuid(context)).thenReturn(uuid)

        assertThat(uuidTransformRule.getNewValue("uuid")).isEqualTo(uuid)
    }

    @Test
    fun getNewValueIfAppMetricaReturnNull() {
        whenever(AppMetrica.getUuid(context)).thenReturn(null)

        assertThat(uuidTransformRule.getNewValue("uuid")).isEqualTo("")
    }

    @Test
    fun getNewValueIfAppMetricaThrowsException() {
        whenever(AppMetrica.getUuid(context)).thenThrow(IllegalAccessError())

        assertThat(uuidTransformRule.getNewValue("uuid")).isEqualTo("")
    }
}

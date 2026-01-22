package io.appmetrica.analytics.push.impl.utils

import android.content.Context
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.PreferenceManager
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

private const val ACTION_REQUEST_CODE_MIN_VALUE = 1512312345
private const val ACTION_REQUEST_CODE_MAX_VALUE = 1512322345

internal class RequestCodeUtilsTest : CommonTest() {

    private val context: Context = mock()
    private val preferenceManager: PreferenceManager = mock()

    private val pushImpl: AppMetricaPushCore = mock {
        on { preferenceManager } doReturn preferenceManager
    }

    @get:Rule
    val pushImplMockedStaticRule = staticRule<AppMetricaPushCore> {
        on { AppMetricaPushCore.getInstance(context) } doReturn pushImpl
    }

    @Test
    fun incrementAndGet() {
        val code = ACTION_REQUEST_CODE_MIN_VALUE + 10
        val expectedCode = code + 1
        whenever(preferenceManager.getPendingIntentId(0)).thenReturn(code)

        assertThat(RequestCodeUtils.incrementAndGet(context)).isEqualTo(expectedCode)
        verify(preferenceManager).savePendingIntentId(expectedCode)
    }

    @Test
    fun incrementAndGetLessThanMin() {
        val code = ACTION_REQUEST_CODE_MIN_VALUE - 10
        val expectedCode = ACTION_REQUEST_CODE_MIN_VALUE + 1
        whenever(preferenceManager.getPendingIntentId(0)).thenReturn(code)

        assertThat(RequestCodeUtils.incrementAndGet(context)).isEqualTo(expectedCode)
        verify(preferenceManager).savePendingIntentId(expectedCode)
    }

    @Test
    fun incrementAndGetMoreThanMax() {
        val code = ACTION_REQUEST_CODE_MAX_VALUE + 10
        val expectedCode = ACTION_REQUEST_CODE_MIN_VALUE + 1
        whenever(preferenceManager.getPendingIntentId(0)).thenReturn(code)

        assertThat(RequestCodeUtils.incrementAndGet(context)).isEqualTo(expectedCode)
        verify(preferenceManager).savePendingIntentId(expectedCode)
    }
}

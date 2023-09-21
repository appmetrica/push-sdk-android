package io.appmetrica.analytics.push.impl.utils

import android.content.Context
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.PreferenceManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

private const val ACTION_REQUEST_CODE_MIN_VALUE = 1512312345
private const val ACTION_REQUEST_CODE_MAX_VALUE = 1512322345

class RequestCodeUtilsTest {

    private val context: Context = mock()
    private val preferenceManager: PreferenceManager = mock()

    private val pushImpl = AppMetricaPushCore.getInstance(context)

    @Before
    fun setUp() {
        pushImpl.preferenceManager = preferenceManager
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

package io.appmetrica.analytics.push.impl.utils

import android.content.Context
import io.appmetrica.analytics.push.impl.AppMetricaPushCore

private const val ACTION_REQUEST_CODE_MIN_VALUE = 1512312345
private const val ACTION_REQUEST_CODE_MAX_VALUE = 1512322345

internal object RequestCodeUtils {

    @JvmStatic
    fun incrementAndGet(context: Context): Int {
        val preferenceManager = AppMetricaPushCore.getInstance(context).preferenceManager
        var value = preferenceManager.getPendingIntentId(0)
        if (value < ACTION_REQUEST_CODE_MIN_VALUE || value > ACTION_REQUEST_CODE_MAX_VALUE - 2) {
            value = ACTION_REQUEST_CODE_MIN_VALUE
        }
        preferenceManager.savePendingIntentId(++value)
        return value
    }
}

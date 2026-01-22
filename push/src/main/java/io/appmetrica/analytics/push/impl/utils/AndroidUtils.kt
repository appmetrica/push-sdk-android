package io.appmetrica.analytics.push.impl.utils

import android.annotation.SuppressLint
import android.os.Build

internal object AndroidUtils {

    @SuppressLint("AnnotateVersionCheck")
    @JvmStatic
    fun isApiAchieved(api: Int): Boolean {
        return Build.VERSION.SDK_INT >= api
    }
}

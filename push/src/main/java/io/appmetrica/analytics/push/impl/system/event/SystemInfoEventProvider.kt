package io.appmetrica.analytics.push.impl.system.event

import android.content.Context
import android.os.Bundle

interface SystemInfoEventProvider {

    fun getSystemInfoEvent(
        context: Context,
        bundle: Bundle
    ): SystemInfoEvent?
}

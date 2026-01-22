package io.appmetrica.analytics.push.impl.system.processor

import android.content.Context
import io.appmetrica.analytics.push.impl.system.event.SystemInfoEvent

internal interface SystemInfoEventProcessor {

    fun process(
        context: Context,
        systemInfoEvent: SystemInfoEvent
    )
}

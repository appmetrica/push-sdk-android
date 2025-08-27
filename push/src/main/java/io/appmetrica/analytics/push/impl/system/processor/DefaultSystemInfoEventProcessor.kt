package io.appmetrica.analytics.push.impl.system.processor

import android.content.Context
import io.appmetrica.analytics.push.impl.system.event.SystemInfoEvent
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub

class DefaultSystemInfoEventProcessor : SystemInfoEventProcessor {

    override fun process(context: Context, systemInfoEvent: SystemInfoEvent) {
        PushMessageTrackerHub.getInstance().onSystemInfoUpdated(
            systemInfoEvent.toJson().toString()
        )
    }
}

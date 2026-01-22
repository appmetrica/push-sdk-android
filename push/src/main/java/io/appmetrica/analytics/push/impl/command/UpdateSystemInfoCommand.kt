package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.system.event.SystemInfoEventProvider
import io.appmetrica.analytics.push.impl.system.processor.SystemInfoEventProcessor
import io.appmetrica.analytics.push.logger.internal.PublicLogger

internal class UpdateSystemInfoCommand(
    private val systemInfoEventProvider: SystemInfoEventProvider,
    private val systemInfoEventProcessor: SystemInfoEventProcessor
) : Command {

    override fun execute(context: Context, bundle: Bundle) {
        PublicLogger.info("Update system info")

        if (!AppMetricaPushCore.getInstance(context).isInitialized) {
            PublicLogger.warning("Failed to update system info. AppMetricaPush is not activated")
            TrackersHub.getInstance().reportEvent("Failed to update system info. AppMetricaPush is not activated")
            return
        }

        val event = systemInfoEventProvider.getSystemInfoEvent(context, bundle)
        PublicLogger.info("System info event: ${event?.toJson()}")
        if (event == null) {
            PublicLogger.info("System info event is null")
            return
        }
        systemInfoEventProcessor.process(context, event)
    }
}

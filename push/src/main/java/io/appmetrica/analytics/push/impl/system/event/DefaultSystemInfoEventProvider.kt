package io.appmetrica.analytics.push.impl.system.event

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.commands.Commands
import io.appmetrica.analytics.push.coreutils.internal.commands.SystemInfoCommandInfo
import io.appmetrica.analytics.push.impl.AppMetricaPushCore

internal class DefaultSystemInfoEventProvider : SystemInfoEventProvider {

    override fun getSystemInfoEvent(context: Context, bundle: Bundle): SystemInfoEvent? {
        val notificationStatusProvider =
            AppMetricaPushCore.getInstance(context).notificationStatusProvider
        val notificationStatus = notificationStatusProvider.notificationStatus

        val systemInfoCommandInfo = bundle.getBundle(Commands.UpdateSystemInfo.EXTRA_INFO)?.let {
            SystemInfoCommandInfo.fromBundle(it)
        }

        systemInfoCommandInfo?.statusChangeTime?.let {
            notificationStatus.setChangedTime(it)
        }

        return SystemInfoEvent.Builder()
            .withNotificationStatus(notificationStatus)
            .build()
    }
}

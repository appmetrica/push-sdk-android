package io.appmetrica.analytics.push.coreutils.internal.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub

private const val PUSH_SERVICE = "io.appmetrica.analytics.push.internal.service.FakeService"

class NoServiceController(
    private val context: Context
) : PushServiceCommandLauncher {

    override fun launchService(extras: Bundle) {
        PLog.d("Launch command with extras: $extras")
        val intent = Intent()
            .setComponent(ComponentName(context.packageName, PUSH_SERVICE))
            .putExtras(extras)
        try {
            Class.forName(PUSH_SERVICE)
                .getMethod("onStartCommand", Context::class.java, Intent::class.java)
                .invoke(null, context, intent)
        } catch (error: Throwable) {
            PLog.e(error, error.message)
            TrackersHub.getInstance().reportError(
                "Calling FakeService for command ${extras.getString(PushServiceFacade.EXTRA_COMMAND)} failed",
                error
            )
        }
    }
}

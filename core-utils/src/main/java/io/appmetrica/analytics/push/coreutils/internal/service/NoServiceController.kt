package io.appmetrica.analytics.push.coreutils.internal.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.commands.Commands
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.logger.internal.DebugLogger

private const val PUSH_SERVICE = "io.appmetrica.analytics.push.internal.service.FakeService"

class NoServiceController(
    private val context: Context
) : PushServiceCommandLauncher {

    private val tag = "[NoServiceController]"

    override fun launchService(extras: Bundle) {
        DebugLogger.info(tag, "Launch command with extras: $extras")
        val intent = Intent()
            .setComponent(ComponentName(context.packageName, PUSH_SERVICE))
            .putExtras(extras)
        try {
            Class.forName(PUSH_SERVICE)
                .getMethod("onStartCommand", Context::class.java, Intent::class.java)
                .invoke(null, context, intent)
        } catch (error: Throwable) {
            DebugLogger.error(tag, error, error.message)
            TrackersHub.getInstance().reportError(
                "Calling FakeService for command ${extras.getString(Commands.EXTRA_COMMAND)} failed",
                error
            )
        }
    }
}

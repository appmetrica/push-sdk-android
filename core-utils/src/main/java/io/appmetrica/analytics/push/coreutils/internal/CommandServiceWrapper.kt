package io.appmetrica.analytics.push.coreutils.internal

import android.content.Context
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import io.appmetrica.analytics.push.coreutils.internal.service.PushServiceControllerProvider
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.logger.internal.DebugLogger

class CommandServiceWrapper {

    private val tag = "[CommandServiceWrapper]"

    private var pushServiceControllerProvider: PushServiceControllerProvider? = null

    fun startCommand(
        context: Context,
        bundle: Bundle,
        needService: Boolean = true
    ) {
        try {
            getPushServiceControllerProvider(context)
                .getPushServiceCommandLauncher(needService)
                .launchService(bundle)
        } catch (th: Throwable) {
            val msg = "Start failed"
            DebugLogger.error(tag, th, msg)
            TrackersHub.getInstance().reportError(msg, th)
        }
    }

    @Synchronized
    private fun getPushServiceControllerProvider(
        context: Context
    ): PushServiceControllerProvider {
        return pushServiceControllerProvider ?: PushServiceControllerProvider(context).also {
            pushServiceControllerProvider = it
        }
    }

    @VisibleForTesting
    fun setPushServiceControllerProvider(provider: PushServiceControllerProvider) {
        pushServiceControllerProvider = provider
    }
}

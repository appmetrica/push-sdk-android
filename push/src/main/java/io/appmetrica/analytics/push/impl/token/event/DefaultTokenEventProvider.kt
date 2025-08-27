package io.appmetrica.analytics.push.impl.token.event

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.commands.Commands
import io.appmetrica.analytics.push.coreutils.internal.commands.PushTokenCommandInfo
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.logger.internal.PublicLogger

class DefaultTokenEventProvider : TokenEventProvider {

    override fun getTokenEvent(
        context: Context,
        bundle: Bundle
    ): TokenEvent? {
        val pushTokenCommandInfo = bundle.getBundle(Commands.SendPushToken.EXTRA_INFO)?.let {
            PushTokenCommandInfo.fromBundle(it)
        } ?: run {
            PublicLogger.warning("Failed to get token event from bundle $bundle")
            TrackersHub.getInstance().reportEvent("Failed to get token event from bundle $bundle")
            return null
        }

        val token = pushTokenCommandInfo.token ?: AppMetricaPushCore.getInstance(context)
            .pushServiceController
            ?.getToken(pushTokenCommandInfo.provider)

        return TokenEvent.Builder(pushTokenCommandInfo.provider)
            .withToken(token)
            .withIsForce(pushTokenCommandInfo.force)
            .build()
    }
}

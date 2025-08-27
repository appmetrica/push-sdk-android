package io.appmetrica.analytics.push.impl.command

import android.content.Context
import android.os.Bundle
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.token.event.TokenEventProvider
import io.appmetrica.analytics.push.impl.token.filter.provider.TokenEventFilterProvider
import io.appmetrica.analytics.push.impl.token.processor.TokenEventProcessor
import io.appmetrica.analytics.push.logger.internal.PublicLogger

internal class SendPushTokenCommand(
    private val tokenEventProvider: TokenEventProvider,
    private val tokenEventFilterProvider: TokenEventFilterProvider,
    private val tokenEventProcessor: TokenEventProcessor
) : Command {

    override fun execute(
        context: Context,
        bundle: Bundle
    ) {
        PublicLogger.info("Trying to get tokens")

        if (!AppMetricaPushCore.getInstance(context).isInitialized) {
            PublicLogger.warning("Failed to send push token. AppMetricaPush is not activated")
            TrackersHub.getInstance().reportEvent("Failed to send push token. AppMetricaPush is not activated")
            return
        }

        val tokenEvent = tokenEventProvider.getTokenEvent(context, bundle)
        if (tokenEvent == null) {
            PublicLogger.warning("Failed to send push token. Token event is null")
            return
        }

        val tokenEventFilter = tokenEventFilterProvider.getTokenEventFilter(context, bundle)

        PublicLogger.info("Found token " + tokenEvent.provider + ": " + tokenEvent.token)
        if (tokenEventFilter.shouldSend(tokenEvent)) {
            PublicLogger.info("Processing token " + tokenEvent.provider + ": " + tokenEvent.token)
            tokenEventProcessor.process(context, tokenEvent)
        } else {
            PublicLogger.info("Skipping token " + tokenEvent.provider + ": " + tokenEvent.token)
        }
    }
}

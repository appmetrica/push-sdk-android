package io.appmetrica.analytics.push.impl.token.filter

import io.appmetrica.analytics.push.impl.PushServiceControllerComposite
import io.appmetrica.analytics.push.impl.token.event.TokenEvent
import io.appmetrica.analytics.push.logger.internal.DebugLogger

internal class PushServiceControllerTokenEventFilter(
    private val pushServiceController: PushServiceControllerComposite?,
) : TokenEventFilter {

    private val tag = "[PushServiceControllerTokenEventFilter]"

    override val id = "shouldSendTokenForProvider"

    override fun shouldSend(tokenEvent: TokenEvent): Boolean {
        if (pushServiceController == null) {
            DebugLogger.warning(tag, "PushServiceController is null")
            return true
        }
        return pushServiceController.shouldSendTokenForProvider(tokenEvent.token ?: "", tokenEvent.provider)
    }
}

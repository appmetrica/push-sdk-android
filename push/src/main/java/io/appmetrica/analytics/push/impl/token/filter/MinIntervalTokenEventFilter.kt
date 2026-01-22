package io.appmetrica.analytics.push.impl.token.filter

import io.appmetrica.analytics.push.impl.token.TokenManager
import io.appmetrica.analytics.push.impl.token.event.TokenEvent
import io.appmetrica.analytics.push.logger.internal.DebugLogger
import java.util.concurrent.TimeUnit

internal class MinIntervalTokenEventFilter(
    private val tokenManager: TokenManager?
) : TokenEventFilter {

    private val tag = "[MinIntervalTokenEventFilter]"

    override val id = "minInterval"

    override fun shouldSend(tokenEvent: TokenEvent): Boolean {
        if (tokenManager == null) {
            DebugLogger.warning(tag, "TokenManager is null")
            return true
        }
        val oldToken = tokenManager.getToken(tokenEvent.provider)
        if (oldToken == null || oldToken.token != tokenEvent.token) {
            return true
        }
        val currentTime = System.currentTimeMillis()
        return currentTime - oldToken.lastUpdateTime > TimeUnit.DAYS.toMillis(1)
    }
}

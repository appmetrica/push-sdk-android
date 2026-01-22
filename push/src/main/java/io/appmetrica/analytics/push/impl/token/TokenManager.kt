package io.appmetrica.analytics.push.impl.token

import android.content.Context
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.storage.Token
import io.appmetrica.analytics.push.logger.internal.PublicLogger

internal class TokenManager(
    private val context: Context,
    private val providerIds: Set<String>
) {

    private val _tokens: MutableMap<String, Token> by lazy {
        loadTokensFromPreferences()
    }

    @Synchronized
    fun init() {
        _tokens
        notifyListener()
    }

    @Synchronized
    fun saveToken(token: String?, provider: String, currentTime: Long) {
        if (!providerIds.contains(provider)) {
            PublicLogger.info("TokenManager: token for provider $provider is not supported")
        } else {
            _tokens[provider] = Token(token, currentTime)
            AppMetricaPushCore.getInstance(context).preferenceManager.saveTokens(Token.saveToString(_tokens))
            notifyListener()
        }
    }

    @Synchronized
    fun getToken(provider: String): Token? {
        return _tokens[provider]
    }

    @Synchronized
    fun getTokens(): Map<String, Token> {
        return _tokens
    }

    @Synchronized
    fun getPlainTokens(): Map<String, String?> {
        return getTokens().mapValues { it.value.token }
    }

    private fun loadTokensFromPreferences(): MutableMap<String, Token> {
        PublicLogger.info("TokenManager: load tokens from preferences")
        val tokens = Token.parseTokens(AppMetricaPushCore.getInstance(context).preferenceManager.tokens)
            ?.filter { providerIds.contains(it.key) }
            ?.toMutableMap() ?: mutableMapOf()
        PublicLogger.info("TokenManager: load tokens from preferences result: $tokens")
        return tokens
    }

    private fun notifyListener() {
        AppMetricaPushCore.getInstance(context).tokenUpdateListener?.onTokenUpdated(
            _tokens.mapValues { it.value.token }
        ) ?: PublicLogger.info("TokenManager: token update listener is null")
    }
}

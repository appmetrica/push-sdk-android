package io.appmetrica.analytics.push.impl.token.event

import org.json.JSONObject

class TokenEvent private constructor(
    val provider: String,
    val token: String?,
    val isForce: Boolean = false
) {

    private val tokenKey = "token"

    fun toJson() = JSONObject().apply {
        put(tokenKey, token)
    }

    class Builder(
        val provider: String,
    ) {
        private var token: String? = null
        private var isForce: Boolean = false

        fun withToken(token: String?): Builder {
            this.token = token
            return this
        }

        fun withIsForce(isForce: Boolean): Builder {
            this.isForce = isForce
            return this
        }

        fun build(): TokenEvent {
            return TokenEvent(
                provider = provider,
                token = token,
                isForce = isForce
            )
        }
    }
}

package io.appmetrica.analytics.push.coreutils.internal.commands

import android.os.Bundle

class PushTokenCommandInfo private constructor(
    val token: String?,
    val provider: String,
    val force: Boolean
) {
    fun toBundle() = Bundle().apply {
        putString(TOKEN_KEY, token)
        putString(PROVIDER_KEY, provider)
        putBoolean(FORCE_KEY, force)
    }

    companion object {
        private const val TOKEN_KEY = "TOKEN"

        private const val PROVIDER_KEY = "PROVIDER_KEY"
        private const val PROVIDER_DEFAULT = ""

        private const val FORCE_KEY = "FORCE"
        private const val FORCE_DEFAULT = false

        fun fromBundle(bundle: Bundle) = PushTokenCommandInfo(
            bundle.getString(TOKEN_KEY),
            bundle.getString(PROVIDER_KEY, PROVIDER_DEFAULT),
            bundle.getBoolean(FORCE_KEY, FORCE_DEFAULT)
        )
    }

    class Builder(
        private val provider: String
    ) {

        private var token: String? = null
        private var force: Boolean = FORCE_DEFAULT

        fun withToken(token: String?) = apply {
            this.token = token
        }

        fun withForce(force: Boolean) = apply {
            this.force = force
        }

        fun build() = PushTokenCommandInfo(
            token,
            provider,
            force
        )
    }
}

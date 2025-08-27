package io.appmetrica.analytics.push.coreutils.internal.commands

import android.os.Bundle

class SystemInfoCommandInfo private constructor(
    val statusChangeTime: Long?
) {

    fun toBundle() = Bundle().apply {
        statusChangeTime?.let {
            putLong(STATUS_CHANGE_TIME_KEY, statusChangeTime)
        }
    }

    companion object {
        private const val STATUS_CHANGE_TIME_KEY = "STATUS_CHANGE_TIME_KEY"

        fun fromBundle(bundle: Bundle) = SystemInfoCommandInfo(
            if (bundle.containsKey(STATUS_CHANGE_TIME_KEY)) {
                bundle.getLong(STATUS_CHANGE_TIME_KEY)
            } else {
                null
            }
        )
    }

    class Builder {

        private var statusChangeTime: Long? = null

        fun withStatusChangeTime(statusChangeTime: Long?) = apply {
            this.statusChangeTime = statusChangeTime
        }

        fun build() = SystemInfoCommandInfo(
            statusChangeTime = statusChangeTime
        )
    }
}

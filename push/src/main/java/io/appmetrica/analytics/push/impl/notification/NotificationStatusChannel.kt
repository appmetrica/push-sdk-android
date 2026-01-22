package io.appmetrica.analytics.push.impl.notification

import org.json.JSONObject

internal class NotificationStatusChannel(
    val id: String,
    val enabled: Boolean,
    val changed: Boolean
) {

    private val enabledKey = "enabled"
    private val changedKey = "changed"

    fun toJson() =
        JSONObject().apply {
            put(enabledKey, enabled)
            put(changedKey, if (changed) true else null)
        }
}

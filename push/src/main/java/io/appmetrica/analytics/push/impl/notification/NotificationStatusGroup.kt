package io.appmetrica.analytics.push.impl.notification

import org.json.JSONObject

class NotificationStatusGroup @JvmOverloads constructor(
    val id: String,
    val enabled: Boolean,
    val changed: Boolean,
    val channels: Set<NotificationStatusChannel> = setOf()
) {

    private val enabledKey = "enabled"
    private val changedKey = "changed"
    private val channelsKey = "channels"

    fun toJson() =
        JSONObject().apply {
            put(enabledKey, enabled)
            put(changedKey, if (changed) true else null)
            put(
                channelsKey,
                JSONObject().apply {
                    for (channel in channels) {
                        put(channel.id, channel.toJson())
                    }
                }
            )
        }
}

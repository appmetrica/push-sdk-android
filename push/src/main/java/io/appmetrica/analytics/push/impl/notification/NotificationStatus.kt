package io.appmetrica.analytics.push.impl.notification

import org.json.JSONObject

internal class NotificationStatus @JvmOverloads constructor(
    val enabled: Boolean,
    val changed: Boolean,
    val groups: Set<NotificationStatusGroup> = setOf(),
    val channelsWithoutGroup: Set<NotificationStatusChannel> = setOf()
) {

    private val enabledKey = "enabled"
    private val changedKey = "changed"
    private val groupsKey = "groups"
    private val channelsWithoutGroupKey = "channels"
    private val systemNotifyTimeKey = "system_notify_time"

    private var changedTime: Long? = null

    fun getChangedTime() = changedTime

    fun setChangedTime(changedTime: Long?) {
        this.changedTime = changedTime
    }

    fun toJson() =
        JSONObject().apply {
            put(enabledKey, enabled)
            put(systemNotifyTimeKey, changedTime)
            put(changedKey, if (changed) true else null)

            if (groups.isNotEmpty()) {
                put(
                    groupsKey,
                    JSONObject().apply {
                        for (group in groups) {
                            put(group.id, group.toJson())
                        }
                    }
                )
            }

            if (channelsWithoutGroup.isNotEmpty()) {
                put(
                    channelsWithoutGroupKey,
                    JSONObject().apply {
                        for (channel in channelsWithoutGroup) {
                            put(channel.id, channel.toJson())
                        }
                    }
                )
            }
        }
}

package io.appmetrica.analytics.push.impl.tracking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.impl.notification.NotificationStatus;
import org.json.JSONException;
import org.json.JSONObject;

public class AppMetricaPushTokenEventSerializer {

    @VisibleForTesting
    static class JsonKeys {

        static final String TOKEN_KEY = "token";
        static final String NOTIFICATION_STATUS_KEY = "notifications_status";

        static class NotificationStatus {

            static final String ENABLED_KEY = "enabled";
            static final String CHANGED_KEY = "changed";
            static final String GROUPS_KEY = "groups";
            static final String CHANNELS_WITHOUT_GROUP_KEY = "channels";
            static final String SYSTEM_NOTIFY_TIME = "system_notify_time";

            static class Group {

                static final String ENABLED_KEY = "enabled";
                static final String CHANGED_KEY = "changed";
                static final String CHANNELS_KEY = "channels";
            }

            static class Channel {

                static final String ENABLED_KEY = "enabled";
                static final String CHANGED_KEY = "changed";
            }
        }
    }

    public AppMetricaPushTokenEventSerializer() {}

    @NonNull
    public String toJson(@Nullable final String pushToken, @NonNull final NotificationStatus notificationStatus) {
        try {
            final JSONObject json = new JSONObject();
            json.put(JsonKeys.TOKEN_KEY, pushToken);
            json.put(JsonKeys.NOTIFICATION_STATUS_KEY, notificationStatusToJson(notificationStatus));
            return json.toString();
        } catch (JSONException ignored) {
        }
        return "";
    }

    @Nullable
    private JSONObject notificationStatusToJson(@NonNull final NotificationStatus notificationStatus) {
        try {
            final JSONObject notificationsStatusJson = new JSONObject();
            notificationsStatusJson.put(JsonKeys.NotificationStatus.ENABLED_KEY, notificationStatus.enabled);
            notificationsStatusJson.put(JsonKeys.NotificationStatus.SYSTEM_NOTIFY_TIME,
                notificationStatus.getChangedTime());
            notificationsStatusJson.put(JsonKeys.NotificationStatus.CHANGED_KEY,
                notificationStatus.changed ? true : null);
            if (notificationStatus.groups.size() != 0) {
                final JSONObject groupsJson = new JSONObject();
                for (NotificationStatus.Group group : notificationStatus.groups) {
                    groupsJson.put(group.id, notificationGroupToJson(group));
                }
                notificationsStatusJson.put(JsonKeys.NotificationStatus.GROUPS_KEY, groupsJson);
            }
            if (notificationStatus.channelsWithoutGroup.size() != 0) {
                final JSONObject channelsJson = new JSONObject();
                for (NotificationStatus.Channel channel : notificationStatus.channelsWithoutGroup) {
                    channelsJson.put(channel.id, notificationChannelToJson(channel));
                }
                notificationsStatusJson.put(JsonKeys.NotificationStatus.CHANNELS_WITHOUT_GROUP_KEY, channelsJson);
            }
            return notificationsStatusJson;
        } catch (JSONException ignored) {
        }
        return null;
    }

    @NonNull
    private JSONObject notificationGroupToJson(@NonNull final NotificationStatus.Group group) throws JSONException {
        final JSONObject groupJson = new JSONObject();
        groupJson.put(JsonKeys.NotificationStatus.Group.ENABLED_KEY, group.enabled);
        groupJson.put(JsonKeys.NotificationStatus.Group.CHANGED_KEY, group.changed ? true : null);
        final JSONObject channelsJson = new JSONObject();
        for (NotificationStatus.Channel channel : group.channels) {
            channelsJson.put(channel.id, notificationChannelToJson(channel));
        }
        groupJson.put(JsonKeys.NotificationStatus.Group.CHANNELS_KEY, channelsJson);
        return groupJson;
    }

    @NonNull
    private JSONObject notificationChannelToJson(@NonNull final NotificationStatus.Channel channel)
        throws JSONException {
        final JSONObject channelJson = new JSONObject();
        channelJson.put(JsonKeys.NotificationStatus.Channel.ENABLED_KEY, channel.enabled);
        channelJson.put(JsonKeys.NotificationStatus.Channel.CHANGED_KEY, channel.changed ? true : null);
        return channelJson;
    }
}

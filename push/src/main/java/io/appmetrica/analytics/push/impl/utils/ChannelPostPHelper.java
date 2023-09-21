package io.appmetrica.analytics.push.impl.utils;

import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import io.appmetrica.analytics.push.coreutils.internal.utils.DoNotInline;

@DoNotInline
@RequiresApi(Build.VERSION_CODES.P)
public class ChannelPostPHelper {

    public static boolean isNotificationChannelGroupBlocked(@NonNull final NotificationManager notificationManager,
                                                            @Nullable final String channelId) {
        final String groupId = ChannelHelper.getNotificationChannelGroupId(notificationManager, channelId);
        final NotificationChannelGroup group = notificationManager.getNotificationChannelGroup(groupId);
        return group != null && group.isBlocked();
    }
}

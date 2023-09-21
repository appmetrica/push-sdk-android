package io.appmetrica.analytics.push.impl.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.DoNotInline;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.impl.notification.NotificationChannelController;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.model.PushMessage;

@DoNotInline
@RequiresApi(Build.VERSION_CODES.O)
public class ChannelHelper {

    @Nullable
    public static String getNotificationChannelGroupId(@NonNull final NotificationManager notificationManager,
                                                       @Nullable final String channelId) {
        final NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
        return channel == null ? null : channel.getGroup();
    }

    public static int getNotificationChannelImportance(@NonNull final NotificationManager notificationManager,
                                                       @Nullable final String channelId) {
        final NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
        return channel != null ? channel.getImportance() : NotificationManager.IMPORTANCE_UNSPECIFIED;
    }

    public static boolean doesChannelExistForNotification(@NonNull final Context context,
                                                          @NonNull final Notification notification) {
        final NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return true;
        }
        final NotificationChannel channel = notificationManager.getNotificationChannel(notification.getChannelId());
        return channel != null;
    }

    public static void reportIgnoredSinceChannelIsAbsent(@NonNull final PushMessage pushMessage,
                                                         @NonNull final Notification notification) {
        if (!CoreUtils.isEmpty(pushMessage.getNotificationId())) {
            PushMessageTrackerHub.getInstance()
                .onNotificationIgnored(
                    pushMessage.getNotificationId(),
                    Constants.IGNORED_CATEGORY_CHANNEL_IS_MISSING,
                    notification.getChannelId(),
                    pushMessage.getPayload(),
                    pushMessage.getTransport()
                );
        }
    }

    @NonNull
    public static NotificationChannel restoreOrCreateDefaultChannel(
        @NonNull final NotificationManager notificationManager
    ) {
        NotificationChannel channel =
            notificationManager.getNotificationChannel(NotificationChannelController.DEFAULT_CHANNEL_ID);
        if (channel == null) {
            channel = new NotificationChannel(
                NotificationChannelController.DEFAULT_CHANNEL_ID,
                NotificationChannelController.DEFAULT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            );
        }
        return channel;
    }

    public static void createDefaultChannel(@NonNull final NotificationManager notificationManager,
                                            @Nullable final NotificationChannel defaultChannel) {
        if (defaultChannel != null) {
            notificationManager.createNotificationChannel(defaultChannel);
        }
    }
}

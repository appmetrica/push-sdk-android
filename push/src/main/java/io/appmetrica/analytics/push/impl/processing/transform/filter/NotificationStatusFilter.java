package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.NotificationManagerCompat;
import io.appmetrica.analytics.push.impl.notification.NotificationChannelController;
import io.appmetrica.analytics.push.impl.utils.ChannelHelper;
import io.appmetrica.analytics.push.impl.utils.ChannelPostPHelper;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.settings.PushFilter;

class NotificationStatusFilter implements PushFilter {

    private static final String DISABLED_SYSTEM_NOTIFICATION = "Disabled system notification";

    @NonNull
    private final NotificationManager notificationManager;
    @NonNull
    private final NotificationManagerCompat notificationManagerCompat;

    public NotificationStatusFilter(@NonNull Context context) {
        this(
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE),
            NotificationManagerCompat.from(context)
        );
    }

    @NonNull
    @Override
    public FilterResult filter(@NonNull PushMessage pushMessage) {
        PushNotification pushNotification = pushMessage.getNotification();
        String channelId = pushNotification == null ? null : pushNotification.getChannelId();
        if (TextUtils.isEmpty(channelId)) {
            channelId = NotificationChannelController.DEFAULT_CHANNEL_ID;
        }

        if (!notificationManagerCompat.areNotificationsEnabled()) {
            return FilterResult.silence(DISABLED_SYSTEM_NOTIFICATION, "Disabled all notifications");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final int importance = ChannelHelper.getNotificationChannelImportance(notificationManager, channelId);
            if (importance == NotificationManager.IMPORTANCE_NONE) {
                return FilterResult.silence(
                    DISABLED_SYSTEM_NOTIFICATION,
                    String.format("Disabled notifications for \"%s\" channel", channelId)
                );
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (ChannelPostPHelper.isNotificationChannelGroupBlocked(notificationManager, channelId)) {
                    String groupId = ChannelHelper.getNotificationChannelGroupId(notificationManager, channelId);
                    return FilterResult.silence(
                        DISABLED_SYSTEM_NOTIFICATION,
                        String.format("Disabled notifications for \"%s\" group", groupId)
                    );
                }
            }
        }

        return FilterResult.show();
    }

    @VisibleForTesting
    NotificationStatusFilter(@NonNull NotificationManager notificationManager,
                             @NonNull NotificationManagerCompat notificationManagerCompat) {
        this.notificationManager = notificationManager;
        this.notificationManagerCompat = notificationManagerCompat;
    }
}

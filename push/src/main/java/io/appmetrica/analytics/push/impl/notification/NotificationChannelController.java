package io.appmetrica.analytics.push.impl.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.utils.ChannelHelper;
import io.appmetrica.analytics.push.impl.utils.Utils;

public class NotificationChannelController {

    public static final String DEFAULT_CHANNEL_ID = "appmetrica_push";
    public static final String DEFAULT_CHANNEL_NAME = "Default";

    @NonNull
    private final NotificationManager notificationManager;
    @Nullable
    private final NotificationChannel defaultChannel;

    public NotificationChannelController(@NonNull Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Utils.isApiAchived(Build.VERSION_CODES.O)) {
            defaultChannel = ChannelHelper.restoreOrCreateDefaultChannel(notificationManager);
        } else {
            defaultChannel = null;
        }
    }

    @Nullable
    public NotificationChannel getDefaultChannel() {
        return defaultChannel;
    }

    public void createDefaultChannel() {
        if (Utils.isApiAchived(Build.VERSION_CODES.O)) {
            ChannelHelper.createDefaultChannel(notificationManager, defaultChannel);
        }
    }
}

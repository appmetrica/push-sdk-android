package io.appmetrica.analytics.push.impl.utils;

import android.app.NotificationManager;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import io.appmetrica.analytics.push.coreutils.internal.utils.DoNotInline;

@DoNotInline
@RequiresApi(Build.VERSION_CODES.M)
public class StatusBarPostMHelper {

    public static boolean hasNotificationInStatusBar(@NonNull final NotificationManager notificationManager,
                                                     @Nullable String tag,
                                                     int id) {
        for (StatusBarNotification activeNotification : notificationManager.getActiveNotifications()) {
            if (activeNotification.getId() == id &&
                ((tag == null && activeNotification.getTag() == null) || tag.equals(activeNotification.getTag()))) {
                return true;
            }
        }
        return false;
    }
}

package io.appmetrica.analytics.push.impl.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.impl.utils.AndroidUtils;
import io.appmetrica.analytics.push.impl.utils.StatusBarPostMHelper;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;

public class PushIdFinder {

    @NonNull
    private final Context context;

    public PushIdFinder(@NonNull Context context) {
        this.context = context;
    }

    @Nullable
    public String findActive(@Nullable String notificationTag, int notificationId) {
        PushMessageHistory pushMessageHistory = AppMetricaPushCore.getInstance(context).getPushMessageHistory();
        PushMessageHistory.PushInfo pushInfo =
            pushMessageHistory.getPushInfoByNotificationTagAndNotificationId(notificationTag, notificationId);
        String replacedPushId = pushInfo == null ? null : pushInfo.pushId;
        boolean isActive = pushInfo != null && Boolean.TRUE.equals(pushInfo.isActive);
        if (AndroidUtils.isApiAchieved(Build.VERSION_CODES.M)) {
            final NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            boolean isActiveFromStatusBar =
                StatusBarPostMHelper.hasNotificationInStatusBar(
                    notificationManager,
                    notificationTag,
                    notificationId
                );
            if (isActiveFromStatusBar != isActive) {
                String message = String.format("Failed get active status for notification [%s, %s]. " +
                        "Preferences has pushId %s (%sactive), but notification in status bar is %sactive",
                    notificationTag, notificationId, replacedPushId, isActive ? "" : "not ",
                    isActiveFromStatusBar ? "" : "not ");
                TrackersHub.getInstance().reportError(message, null);
                PublicLogger.INSTANCE.error(message);
                isActive = isActiveFromStatusBar;
            }
        }
        return isActive ? replacedPushId : null;
    }
}

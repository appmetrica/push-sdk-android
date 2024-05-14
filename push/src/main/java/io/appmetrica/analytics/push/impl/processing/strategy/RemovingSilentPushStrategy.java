package io.appmetrica.analytics.push.impl.processing.strategy;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.impl.utils.AndroidUtils;
import io.appmetrica.analytics.push.impl.utils.StatusBarPostMHelper;
import io.appmetrica.analytics.push.model.PushMessage;
import java.util.List;

public class RemovingSilentPushStrategy implements PushStrategy {

    private static final String OK = "Ok";
    private static final String REMOVED_BY_USER = "Removed by user";
    private static final String REPLACED = "Notification was replaced";
    private static final String NOT_FOUND = "Notification not found";

    @Override
    public void processPush(@NonNull Context context, @NonNull PushMessage pushMessage) {
        if (!CoreUtils.isEmpty(pushMessage.getNotificationId())) {
            final PushMessageHistory pushMessageHistory =
                AppMetricaPushCore.getInstance(context).getPushMessageHistory();
            final String pushId = pushMessage.getPushIdToRemove();
            if (pushId != null) {
                final PushMessageHistory.PushInfo pushInfo = getPushInfo(pushMessageHistory, pushId);
                if (pushInfo != null) {
                    removeNotification(context, pushInfo, pushMessage);
                    return;
                }
            }
            PLog.e("Push with pushId %s not found", pushId);

            final List<String> pushIds = pushMessageHistory.getPushIds();
            if (pushIds.contains(pushId)) {
                reportRemovingSilentPushProcessed(pushMessage, NOT_FOUND, REPLACED);
            } else {
                reportRemovingSilentPushProcessed(pushMessage, NOT_FOUND, null);
            }
        }
    }

    @Nullable
    private PushMessageHistory.PushInfo getPushInfo(@NonNull final PushMessageHistory pushMessageHistory,
                                                    @NonNull final String pushId) {
        final List<PushMessageHistory.PushInfo> pushInfoList = pushMessageHistory.getPushInfoList();
        for (final PushMessageHistory.PushInfo pushInfo : pushInfoList) {
            if (pushInfo.pushId.equals(pushId)) {
                return pushInfo;
            }
        }
        return null;
    }

    private void removeNotification(@NonNull final Context context,
                                    @NonNull final PushMessageHistory.PushInfo pushInfo,
                                    @NonNull final PushMessage pushMessage) {
        final NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (isAlreadyRemovedByUser(notificationManager, pushInfo)) {
                reportRemovingSilentPushProcessed(pushMessage, NOT_FOUND, REMOVED_BY_USER);
            } else {
                notificationManager.cancel(pushInfo.notificationTag, pushInfo.notificationId);
            }
            reportRemovingSilentPushProcessed(pushMessage, OK, null);
            AppMetricaPushCore.getInstance(context)
                .getPushMessageHistory()
                .setPushActive(pushInfo.pushId, false);
        } else {
            PLog.e("Notification manager is not available");
        }
    }

    private boolean isAlreadyRemovedByUser(@NonNull final NotificationManager notificationManager,
                                           @NonNull final PushMessageHistory.PushInfo pushInfo) {
        if (AndroidUtils.isApiAchieved(Build.VERSION_CODES.M)) {
            return !StatusBarPostMHelper.hasNotificationInStatusBar(
                notificationManager,
                pushInfo.notificationTag,
                pushInfo.notificationId
            );
        } else {
            return false;
        }
    }

    private void reportRemovingSilentPushProcessed(@NonNull final PushMessage pushMessage,
                                                   @NonNull final String category,
                                                   @Nullable final String details) {
        PushMessageTrackerHub.getInstance().onRemovingSilentPushProcessed(
            pushMessage.getNotificationId(),
            category,
            details,
            pushMessage.getPayload(),
            pushMessage.getTransport()
        );
    }
}

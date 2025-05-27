package io.appmetrica.analytics.push.impl.notification.processing;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.notification.OpenActivityStrategy;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.impl.utils.AndroidUtils;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import java.util.HashMap;

public class AdditionalActionProcessingStrategy extends OpenActivityStrategy {

    @Override
    public void doAction(@NonNull Context context, @NonNull Intent intent) {
        final NotificationActionInfo actionInfo = intent.getParcelableExtra(AppMetricaPush.EXTRA_ACTION_INFO);
        if (actionInfo != null) {
            final boolean autoTracking = AppMetricaPushCore.getInstance(context).getPushServiceProvider()
                .getAutoTrackingConfiguration().isTrackingAdditionalAction(actionInfo.actionId);
            final String pushId = actionInfo.pushId;
            if (!CoreUtils.isEmpty(pushId) && autoTracking) {
                PushMessageTrackerHub.getInstance()
                    .onNotificationAdditionalAction(
                        pushId,
                        actionInfo.actionId,
                        actionInfo.payload,
                        actionInfo.transport,
                        actionInfo.targetActionUri
                    );
            }
            if (!actionInfo.doNothing) {
                openActionOrDefaultActivity(context, actionInfo);
            }

            hideQuickControlPanelIfNecessary(context, actionInfo);
            clearNotificationIfNecessary(context, actionInfo);
        } else {
            TrackersHub.getInstance().reportEvent("No action info for AdditionalActionProcessingStrategy");
        }
    }

    @SuppressLint("MissingPermission")
    private void hideQuickControlPanelIfNecessary(@NonNull final Context context,
                                                  @NonNull final NotificationActionInfo actionInfo) {
        // https://nda.ya.ru/t/-E8RoEdH6Wz9zF
        if (AndroidUtils.isApiAchieved(Build.VERSION_CODES.S)) {
            return;
        }
        if (actionInfo.hideQuickControlPanel) {
            context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        }
    }

    private void clearNotificationIfNecessary(@NonNull final Context context,
                                              @NonNull final NotificationActionInfo actionInfo) {
        if (actionInfo.dismissOnAdditionalAction) {
            NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.cancel(actionInfo.notificationTag, actionInfo.notificationId);
                AppMetricaPushCore.getInstance(context).getPushMessageHistory().setPushActive(actionInfo.pushId, false);
                TrackersHub.getInstance().reportEvent("Clear notification by button",
                    new HashMap<String, Object>() {{
                        put("actionId", actionInfo.actionId);
                        put("notificationId", actionInfo.notificationId);
                        put("notificationTag", actionInfo.notificationTag);
                        put("pushId", actionInfo.pushId);
                    }});
            } else {
                TrackersHub.getInstance().reportEvent("No notificationManager to clear notification by button",
                    new HashMap<String, Object>() {{
                        put("actionId", actionInfo.actionId);
                        put("pushId", actionInfo.pushId);
                    }});
            }
        }
    }
}

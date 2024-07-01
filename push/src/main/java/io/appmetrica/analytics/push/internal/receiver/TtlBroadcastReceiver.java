package io.appmetrica.analytics.push.internal.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;

public class TtlBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "[TtlBroadcastReceiver]";
    public static final String EXPIRED_BY_TTL_ACTION = "io.appmetrica.analytics.push.action.EXPIRED_BY_TTL_ACTION";

    @Override
    public void onReceive(@NonNull final Context context, @NonNull final Intent intent) {
        try {
            final String intentAction = intent.getAction();
            if (EXPIRED_BY_TTL_ACTION.equals(intentAction)) {
                final Bundle extras = intent.getExtras();
                if (extras != null) {
                    onReceive(context, extras);
                } else {
                    DebugLogger.INSTANCE.error(TAG, "Extras is null or empty");
                }
            } else {
                DebugLogger.INSTANCE.info(TAG, intentAction);
                DebugLogger.INSTANCE.info(TAG, "Bundle: " + intent.getExtras());
            }
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to handle ttl", e);
        }
    }

    private void onReceive(@NonNull Context context, @NonNull Bundle extras) {
        final String pushId = extras.getString(Constants.PUSH_ID);
        final int notificationId = extras.getInt(Constants.NOTIFICATION_ID);
        final String notificationTag = extras.getString(Constants.NOTIFICATION_TAG);
        final String payload = extras.getString(Constants.PAYLOAD);
        final String provider = extras.getString(CoreConstants.EXTRA_TRANSPORT);
        final NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            PublicLogger.INSTANCE.info("Canceling notification with id %d", notificationId);
            notificationManager.cancel(notificationTag, notificationId);
            if (!CoreUtils.isEmpty(pushId)) {
                PushMessageTrackerHub.getInstance()
                    .onNotificationExpired(pushId, "by ttl", payload, provider);
            }
            AppMetricaPushCore.getInstance(context).getPushMessageHistory().setPushActive(pushId, false);
        } else {
            DebugLogger.INSTANCE.error(TAG, "Notification manager is not available");
        }
    }
}

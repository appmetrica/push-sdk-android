package io.appmetrica.analytics.push.internal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.notification.NotificationActionListener;

public class AppMetricaPushBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "AppMetricaPush";

    public static final String ACTION_BROADCAST_ACTION = "io.appmetrica.analytics.push.action.NOTIFICATION_ACTION";

    @Override
    public void onReceive(@NonNull final Context context, @NonNull final Intent intent) {
        try {
            final String intentAction = intent.getAction();
            if (ACTION_BROADCAST_ACTION.equals(intentAction)) {
                onBroadcastActionReceive(context.getApplicationContext(), intent);
            } else {
                PLog.i(TAG, intentAction);
                PLog.i(TAG, "Bundle: " + intent.getExtras());
            }
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to handle notification action", e);
        }
    }

    private void onBroadcastActionReceive(@NonNull final Context appContext, @NonNull final Intent intent) {
        final NotificationActionListener notificationActionListener =
            AppMetricaPushCore.getInstance(appContext).getPushServiceProvider().getNotificationActionListener();
        notificationActionListener.onNotificationAction(appContext, intent);
    }
}

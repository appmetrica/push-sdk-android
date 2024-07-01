package io.appmetrica.analytics.push.internal.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.RefreshTokenInfo;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import java.util.concurrent.TimeUnit;

public class AppMetricaPushNotificationStatusChangeHandler extends BroadcastReceiver {

    private static final String TAG = "[AppMetricaPushNotificationStatusChangeHandler]";

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        try {
            String action = intent.getAction();
            DebugLogger.INSTANCE.info(
                TAG,
                "Notification block state change with action %s and bundle %s",
                action,
                intent.getExtras()
            );
            if (NotificationManager.ACTION_APP_BLOCK_STATE_CHANGED.equals(action)
                || NotificationManager.ACTION_NOTIFICATION_CHANNEL_BLOCK_STATE_CHANGED.equals(action)
                || NotificationManager.ACTION_NOTIFICATION_CHANNEL_GROUP_BLOCK_STATE_CHANGED.equals(action)) {
                long curTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                PushServiceFacade.refreshToken(context, new RefreshTokenInfo(true, curTime));
            } else {
                DebugLogger.INSTANCE.warning(TAG, "Unknown action %s", action);
            }
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to handle change notification status", e);
        }
    }
}

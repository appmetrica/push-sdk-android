package io.appmetrica.analytics.push.internal.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.RefreshTokenInfo;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import java.util.concurrent.TimeUnit;

public class AppMetricaPushNotificationStatusChangeHandler extends BroadcastReceiver {

    private static final String TAG = "[AppMetricaPushNotificationStatusChangeHandler]";

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        try {
            String action = intent.getAction();
            PLog.i("%s Notification block state change with action %s and bundle %s", TAG, action, intent.getExtras());
            if (NotificationManager.ACTION_APP_BLOCK_STATE_CHANGED.equals(action)
                || NotificationManager.ACTION_NOTIFICATION_CHANNEL_BLOCK_STATE_CHANGED.equals(action)
                || NotificationManager.ACTION_NOTIFICATION_CHANNEL_GROUP_BLOCK_STATE_CHANGED.equals(action)) {
                long curTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                PushServiceFacade.refreshToken(context, new RefreshTokenInfo(true, curTime));
            } else {
                PLog.w("%s Unknown action %s", TAG, action);
            }
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to handle change notification status", e);
        }
    }
}

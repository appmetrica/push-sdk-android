package io.appmetrica.analytics.push.internal.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.notification.NotificationActionListener;

public class AppMetricaPushDummyActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        try {
            onActionReceive(getApplicationContext(), getIntent());
            finish();
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to handle notification action", e);
        }
    }

    private void onActionReceive(@NonNull final Context appContext, @NonNull final Intent intent) {
        final NotificationActionListener notificationActionListener =
            AppMetricaPushCore.getInstance(appContext).getPushServiceProvider().getNotificationActionListener();
        notificationActionListener.onNotificationAction(appContext, intent);
    }
}

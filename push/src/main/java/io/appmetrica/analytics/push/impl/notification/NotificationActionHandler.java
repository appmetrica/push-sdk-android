package io.appmetrica.analytics.push.impl.notification;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;

public class NotificationActionHandler implements NotificationActionListener {

    @Override
    public void onNotificationAction(@NonNull final Context context, @NonNull final Intent intent) {
        AppMetricaPushCore.getInstance(context).getPushServiceProvider().getNotificationActionProcessor()
            .processAction(context, intent);
    }
}

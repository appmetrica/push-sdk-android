package io.appmetrica.analytics.push.impl.notification;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

public interface NotificationActionListener {

    void onNotificationAction(@NonNull final Context context, @NonNull final Intent intent);

}

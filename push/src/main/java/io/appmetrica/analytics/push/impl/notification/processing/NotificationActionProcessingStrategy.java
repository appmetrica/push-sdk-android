package io.appmetrica.analytics.push.impl.notification.processing;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

public interface NotificationActionProcessingStrategy {

    void doAction(@NonNull final Context context, @NonNull final Intent bundle);

}

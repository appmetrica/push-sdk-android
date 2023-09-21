package io.appmetrica.analytics.push.notification;

import android.app.Notification;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.model.PushMessage;

/**
 * Extension point for customization push notification building process.
 */
public interface NotificationCustomizer {

    /**
     * Used to customize {@link Notification} using data from {@link PushMessage}.
     *
     * @param builder pre-filled {@link NotificationCompat.Builder} object that could be changed
     * @param pushMessage received {@link PushMessage}
     */
    void invoke(
        @NonNull NotificationCompat.Builder builder,
        @NonNull PushMessage pushMessage
    );
}

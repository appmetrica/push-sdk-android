package io.appmetrica.analytics.push.notification.providers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setWhen(long)} method.
 */
public class WhenProvider implements NotificationValueProvider<Long> {

    /**
     * Extracts value for {@link NotificationCompat.Builder#setWhen(long)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public Long get(@NonNull PushMessage pushMessage) {
        Long when = null;
        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            when = notification.getWhen();
        }
        if (when == null) {
            when = System.currentTimeMillis();
        }
        return when;
    }
}

package io.appmetrica.analytics.push.notification.providers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setVibrate(long[])} method.
 */
public class VibrateProvider implements NotificationValueProvider<long[]> {

    /**
     * Creates a new instance of {@link VibrateProvider}.
     */
    public VibrateProvider() {
    }

    /**
     * Extracts value for {@link NotificationCompat.Builder#setVibrate(long[])} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public long[] get(@NonNull PushMessage pushMessage) {
        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            return notification.getVibrate();
        }
        return null;
    }
}

package io.appmetrica.analytics.push.notification.providers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setPriority(int)} method.
 */
public class PriorityProvider implements NotificationValueProvider<Integer> {

    /**
     * Creates a new instance of {@link PriorityProvider}.
     */
    public PriorityProvider() {
    }

    /**
     * Extracts value for {@link NotificationCompat.Builder#setPriority(int)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public Integer get(@NonNull PushMessage pushMessage) {
        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            return notification.getPriority();
        }
        return null;
    }
}

package io.appmetrica.analytics.push.notification.providers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setAutoCancel(boolean)} method.
 */
public class AutoCancelProvider implements NotificationValueProvider<Boolean> {

    /**
     * Extracts value for {@link NotificationCompat.Builder#setAutoCancel(boolean)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public Boolean get(@NonNull PushMessage pushMessage) {
        Boolean cancelable = null;
        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            cancelable = notification.getAutoCancel();
        }
        if (cancelable == null) {
            cancelable = true;
        }
        return cancelable;
    }
}

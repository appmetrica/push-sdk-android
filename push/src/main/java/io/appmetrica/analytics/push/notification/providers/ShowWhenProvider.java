package io.appmetrica.analytics.push.notification.providers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setShowWhen(boolean)} method.
 */
public class ShowWhenProvider implements NotificationValueProvider<Boolean> {

    /**
     * Creates a new instance of {@link ShowWhenProvider}.
     */
    public ShowWhenProvider() {
    }

    /**
     * Extracts value for {@link NotificationCompat.Builder#setShowWhen(boolean)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public Boolean get(@NonNull PushMessage pushMessage) {
        Boolean showWhen = true;
        PushNotification notification = pushMessage.getNotification();
        if (notification != null && notification.getShowWhen() != null) {
            showWhen = notification.getShowWhen();
        }
        return showWhen;
    }
}

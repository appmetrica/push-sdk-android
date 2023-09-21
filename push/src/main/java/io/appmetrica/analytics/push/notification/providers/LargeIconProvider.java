package io.appmetrica.analytics.push.notification.providers;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setLargeIcon(Bitmap)} method.
 */
public class LargeIconProvider implements NotificationValueProvider<Bitmap> {

    /**
     * Extracts value for {@link NotificationCompat.Builder#setLargeIcon(Bitmap)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public Bitmap get(@NonNull PushMessage pushMessage) {
        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            return notification.getLargeIcon();
        }
        return null;
    }
}

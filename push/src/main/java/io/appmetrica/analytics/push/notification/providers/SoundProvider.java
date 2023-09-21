package io.appmetrica.analytics.push.notification.providers;

import android.media.RingtoneManager;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setSound(Uri)} method.
 */
public class SoundProvider implements NotificationValueProvider<Uri> {

    /**
     * Extracts value for {@link NotificationCompat.Builder#setSound(Uri)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public Uri get(@NonNull PushMessage pushMessage) {
        PushNotification notification = pushMessage.getNotification();
        if (notification != null && notification.isSoundEnabled()) {
            Uri soundUri = notification.getSoundUri();
            if (soundUri != null) {
                return soundUri;
            } else {
                return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }
        return null;
    }
}

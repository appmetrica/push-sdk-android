package io.appmetrica.analytics.push.notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.model.PushMessage;

/**
 * Extracts value for {@link NotificationCompat.Builder} methods.
 *
 * @param <T> type
 */
public interface NotificationValueProvider<T> {

    /**
     * Extracts value for {@link NotificationCompat.Builder} methods.
     *
     * @param pushMessage received {@link PushMessage}
     * @return parsed value
     */
    @Nullable
    T get(@NonNull final PushMessage pushMessage);
}

package io.appmetrica.analytics.push.notification.providers;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setCategory(String)} method.
 */
public class CategoryProvider implements NotificationValueProvider<String> {

    /**
     * Extracts value for {@link NotificationCompat.Builder#setCategory(String)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public String get(@NonNull PushMessage pushMessage) {
        String category = null;
        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            category = notification.getCategory();
        }
        if (!TextUtils.isEmpty(category)) {
            return category;
        }
        return null;
    }
}

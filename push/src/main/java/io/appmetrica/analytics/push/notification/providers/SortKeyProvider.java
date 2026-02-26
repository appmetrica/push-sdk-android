package io.appmetrica.analytics.push.notification.providers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setSortKey(String)} method.
 */
public class SortKeyProvider implements NotificationValueProvider<String> {

    /**
     * Creates a new instance of {@link SortKeyProvider}.
     */
    public SortKeyProvider() {
    }

    /**
     * Extracts value for {@link NotificationCompat.Builder#setSortKey(String)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public String get(@NonNull PushMessage pushMessage) {
        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            String sortKey = notification.getSortKey();
            if (!CoreUtils.isEmpty(sortKey)) {
                return sortKey;
            }
        }
        return null;
    }
}

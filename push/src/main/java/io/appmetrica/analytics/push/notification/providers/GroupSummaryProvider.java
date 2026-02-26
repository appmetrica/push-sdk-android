package io.appmetrica.analytics.push.notification.providers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setGroupSummary(boolean)} method.
 */
public class GroupSummaryProvider implements NotificationValueProvider<Boolean> {

    /**
     * Creates a new instance of {@link GroupSummaryProvider}.
     */
    public GroupSummaryProvider() {
    }

    /**
     * Extracts value for {@link NotificationCompat.Builder#setGroupSummary(boolean)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public Boolean get(@NonNull PushMessage pushMessage) {
        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            return notification.getGroupSummary();
        }
        return null;
    }
}

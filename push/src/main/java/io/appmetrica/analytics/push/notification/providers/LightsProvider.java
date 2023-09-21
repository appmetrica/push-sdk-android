package io.appmetrica.analytics.push.notification.providers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.util.Arrays;
import java.util.List;
import io.appmetrica.analytics.push.model.LedLights;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setLights(int, int, int)} method.
 */
public class LightsProvider implements NotificationValueProvider<List<Integer>> {

    /**
     * Extracts value for {@link NotificationCompat.Builder#setLights(int, int, int)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public List<Integer> get(@NonNull PushMessage pushMessage) {
        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            LedLights ledLights = notification.getLedLights();
            if (ledLights != null && ledLights.isValid()) {
                return Arrays.asList(ledLights.getColor(), ledLights.getOnMs(), ledLights.getOffMs());
            }
        }
        return null;
    }
}

package io.appmetrica.analytics.push.settings;

import android.app.Notification;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.model.PushMessage;

/**
 * Interface for custom factory that creates {@link Notification} from {@link PushMessage}.
 * Factory can be set via {@link AppMetricaPush#setPushNotificationFactory(Context, PushNotificationFactory)} method.
 */
public interface PushNotificationFactory {

    /**
     * @param context application context
     * @param pushMessage received {@link PushMessage}
     * @return {@link Notification} object to show or null if no notification should be shown
     */
    @Nullable
    Notification buildNotification(
        @NonNull final Context context,
        @NonNull final PushMessage pushMessage
    );
}

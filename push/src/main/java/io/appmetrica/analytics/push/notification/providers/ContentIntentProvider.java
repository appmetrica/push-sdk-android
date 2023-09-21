package io.appmetrica.analytics.push.notification.providers;

import android.app.PendingIntent;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.intent.NotificationActionType;
import io.appmetrica.analytics.push.internal.IntentHelper;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setContentIntent(PendingIntent)} method.
 */
public class ContentIntentProvider implements NotificationValueProvider<PendingIntent> {

    @NonNull
    private final Context context;

    /**
     * Constructor for {@link ContentIntentProvider}.
     *
     * @param context this {@link Context} is used to create {@link PendingIntent}
     */
    public ContentIntentProvider(@NonNull Context context) {
        this.context = context;
    }

    /**
     * Extracts value for {@link NotificationCompat.Builder#setContentIntent(PendingIntent)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public PendingIntent get(@NonNull PushMessage pushMessage) {
        PushNotification notification = pushMessage.getNotification();
        String action = null;
        if (notification != null) {
            action = notification.getOpenActionUrl();
        }
        NotificationActionInfo actionInfo =
            IntentHelper.createNotificationActionInfo(NotificationActionType.CLICK, pushMessage, action);

        return IntentHelper.getPendingIntentForOpenAction(context, notification, actionInfo);
    }
}

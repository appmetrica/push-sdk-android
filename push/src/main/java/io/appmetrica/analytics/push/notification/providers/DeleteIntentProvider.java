package io.appmetrica.analytics.push.notification.providers;

import android.app.PendingIntent;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.intent.NotificationActionType;
import io.appmetrica.analytics.push.internal.IntentHelper;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setDeleteIntent(PendingIntent)} method.
 */
public class DeleteIntentProvider implements NotificationValueProvider<PendingIntent> {

    @NonNull
    private final Context context;

    /**
     * Constructor for {@link DeleteIntentProvider}.
     *
     * @param context this {@link Context} is used to create {@link PendingIntent}
     */
    public DeleteIntentProvider(@NonNull Context context) {
        this.context = context;
    }

    /**
     * Extracts value for {@link NotificationCompat.Builder#setDeleteIntent(PendingIntent)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public PendingIntent get(@NonNull PushMessage pushMessage) {
        boolean autoTracking = AppMetricaPushCore.getInstance(context)
            .getPushServiceProvider()
            .getAutoTrackingConfiguration()
            .trackingDismissAction;
        NotificationActionInfo actionInfo =
            IntentHelper.createNotificationActionInfo(NotificationActionType.CLEAR, pushMessage, null);
        return IntentHelper.createWrappedAction(context, actionInfo, autoTracking);
    }
}

package io.appmetrica.analytics.push.notification.providers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.notification.NotificationChannelController;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.notification.NotificationValueProvider;

/**
 * Extracts value for {@link NotificationCompat.Builder#setChannelId(String)} method.
 */
public class ChannelIdProvider implements NotificationValueProvider<String> {

    @NonNull
    private final Context context;

    /**
     * Constructor for {@link ChannelIdProvider}.
     *
     * @param context this {@link Context} is used to create channel if necessary
     */
    public ChannelIdProvider(@NonNull Context context) {
        this.context = context;
    }

    /**
     * Extracts value for {@link NotificationCompat.Builder#setChannelId(String)} method.
     *
     * @param pushMessage received {@link PushMessage}
     * @return extracted value
     */
    @Nullable
    @Override
    public String get(@NonNull PushMessage pushMessage) {
        String channelId = null;
        PushNotification notification = pushMessage.getNotification();
        if (notification != null) {
            channelId = notification.getChannelId();
        }
        if (CoreUtils.isEmpty(channelId)) {
            AppMetricaPushCore.getInstance(context).getNotificationChannelController().createDefaultChannel();
            channelId = NotificationChannelController.DEFAULT_CHANNEL_ID;
        }
        return channelId;
    }
}

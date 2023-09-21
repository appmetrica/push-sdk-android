package io.appmetrica.analytics.push.impl.processing.strategy;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.impl.PushNotificationFactoryProvider;
import io.appmetrica.analytics.push.impl.processing.NotificationPublisher;
import io.appmetrica.analytics.push.model.PushMessage;

public class GeneralPushStrategy implements PushStrategy {

    @NonNull
    private final NotificationPublisher notificationPublisher;

    public GeneralPushStrategy() {
        this(new NotificationPublisher());
    }

    @VisibleForTesting
    GeneralPushStrategy(@NonNull final NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }

    @Override
    public void processPush(@NonNull Context context, @NonNull PushMessage pushMessage) {
        notificationPublisher.publishNotification(
            context,
            PushNotificationFactoryProvider.getPushNotificationFactory(),
            pushMessage
        );
    }
}

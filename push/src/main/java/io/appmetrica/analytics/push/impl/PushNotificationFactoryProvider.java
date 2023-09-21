package io.appmetrica.analytics.push.impl;

import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.internal.notification.DefaultPushNotificationFactory;
import io.appmetrica.analytics.push.settings.PushNotificationFactory;

public abstract class PushNotificationFactoryProvider {

    @NonNull
    private static final PushNotificationFactory defaultPushNotificationFactory =
        new DefaultPushNotificationFactory();
    @NonNull
    private static volatile PushNotificationFactory pushNotificationFactory = defaultPushNotificationFactory;

    private PushNotificationFactoryProvider() {}

    @NonNull
    public static PushNotificationFactory getPushNotificationFactory() {
        return pushNotificationFactory;
    }

    @NonNull
    public static PushNotificationFactory getDefaultPushNotificationFactory() {
        return defaultPushNotificationFactory;
    }

    public static void setPushNotificationFactory(@NonNull final PushNotificationFactory notificationFactory) {
        pushNotificationFactory = notificationFactory;
    }

    public static boolean isDefault(@NonNull final PushNotificationFactory factory) {
        return factory == defaultPushNotificationFactory;
    }
}

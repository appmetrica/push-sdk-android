package io.appmetrica.analytics.push.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.notification.NotificationActionListener;
import io.appmetrica.analytics.push.impl.notification.NotificationChannelController;
import io.appmetrica.analytics.push.impl.notification.NotificationStatusProvider;
import io.appmetrica.analytics.push.impl.notification.processing.NotificationActionProcessor;
import io.appmetrica.analytics.push.impl.processing.PushProcessingStrategyProvider;
import io.appmetrica.analytics.push.impl.processing.PushProcessor;
import io.appmetrica.analytics.push.impl.processing.transform.filter.PreLazyFilterFacade;
import io.appmetrica.analytics.push.impl.processing.transform.filter.PushFilterFacade;
import io.appmetrica.analytics.push.impl.tracking.InternalPushMessageTracker;
import io.appmetrica.analytics.push.impl.utils.MainProcessDetector;
import io.appmetrica.analytics.push.settings.AutoTrackingConfiguration;
import io.appmetrica.analytics.push.settings.PassportUidProvider;

public interface PushServiceProvider {

    @NonNull
    PushProcessor getPushProcessor();

    @NonNull
    PushProcessingStrategyProvider getPushProcessingStrategyProvider();

    @NonNull
    NotificationActionListener getNotificationActionListener();

    @NonNull
    NotificationActionProcessor getNotificationActionProcessor();

    @NonNull
    InternalPushMessageTracker getPushMessageTracker();

    @NonNull
    AutoTrackingConfiguration getAutoTrackingConfiguration();

    @NonNull
    PreferenceManager getPreferenceManager();

    @NonNull
    PushMessageHistory getPushMessageHistory();

    @NonNull
    NotificationStatusProvider getNotificationStatusProvider();

    @NonNull
    NotificationChannelController getNotificationChannelController();

    @NonNull
    MainProcessDetector getMainProcessDetector();

    @NonNull
    PushFilterFacade getPushFilterFacade();

    @NonNull
    PreLazyFilterFacade getPreLazyFilterFacade();

    @Nullable
    PassportUidProvider getPassportUidProvider();

    void setAutoTrackingConfiguration(@NonNull final AutoTrackingConfiguration autoTrackingConfiguration);

    void setPassportUidProvider(@NonNull PassportUidProvider passportUidProvider);

    void setPushMessageHistory(@NonNull PushMessageHistory pushMessageHistory);

    void setPreferenceManager(@NonNull PreferenceManager preferenceManager);

    void setNotificationChannelController(@NonNull NotificationChannelController notificationChannelController);

    void setPushFilterFacade(@NonNull PushFilterFacade pushFilterFacade);

    void setPreLazyFilterFacade(@NonNull PreLazyFilterFacade preLazyFilterFacade);
}

package io.appmetrica.analytics.push;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.PreferenceManager;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.impl.PushServiceProvider;
import io.appmetrica.analytics.push.impl.notification.NotificationActionListener;
import io.appmetrica.analytics.push.impl.notification.NotificationChannelController;
import io.appmetrica.analytics.push.impl.notification.NotificationStatusProvider;
import io.appmetrica.analytics.push.impl.notification.processing.NotificationActionProcessor;
import io.appmetrica.analytics.push.impl.processing.PushProcessingStrategyProvider;
import io.appmetrica.analytics.push.impl.processing.PushProcessor;
import io.appmetrica.analytics.push.impl.processing.transform.filter.PreLazyFilterFacade;
import io.appmetrica.analytics.push.impl.processing.transform.filter.PushFilterFacade;
import io.appmetrica.analytics.push.impl.tracking.AppMetricaPushTokenEventSerializer;
import io.appmetrica.analytics.push.impl.utils.MainProcessDetector;
import io.appmetrica.analytics.push.settings.AutoTrackingConfiguration;
import io.appmetrica.analytics.push.settings.PassportUidProvider;
import io.appmetrica.analytics.push.settings.PushMessageTracker;
import io.appmetrica.analytics.push.settings.PushNotificationFactory;

import static org.mockito.Mockito.mock;

public class MockablePushServiceProvider implements PushServiceProvider {

    private PushProcessor mPushProcessor;
    private PushProcessingStrategyProvider mPushProcessingStrategyProvider;
    private PushNotificationFactory mPushNotificationFactory;
    private NotificationActionListener mNotificationActionListener;
    private PushMessageTracker mPushMessageTracker;
    private NotificationActionProcessor mNotificationActionProcessor;
    private AutoTrackingConfiguration mAutoTrackingConfiguration;
    private PreferenceManager mPreferenceManager;
    private PushMessageHistory mPushMessageHistory;
    private NotificationStatusProvider mNotificationStatusProvider;
    private AppMetricaPushTokenEventSerializer mAppMetricaPushTokenEventSerializer;
    private NotificationChannelController mNotificationChannelController;
    private MainProcessDetector mMainProcessDetector;
    private PushFilterFacade mPushFilterFacade;
    private PreLazyFilterFacade mPreLazyFilterFacade;
    private PassportUidProvider mPassportUidProvider;

    public MockablePushServiceProvider() {
        mPushProcessor = mock(PushProcessor.class);
        mPushProcessingStrategyProvider = mock(PushProcessingStrategyProvider.class);
        mPushNotificationFactory = mock(PushNotificationFactory.class);
        mNotificationActionListener = mock(NotificationActionListener.class);
        mPushMessageTracker = mock(PushMessageTracker.class);
        mNotificationActionProcessor = mock(NotificationActionProcessor.class);
        mAutoTrackingConfiguration = mock(AutoTrackingConfiguration.class);
        mPreferenceManager = mock(PreferenceManager.class);
        mPushMessageHistory = mock(PushMessageHistory.class);
        mNotificationStatusProvider = mock(NotificationStatusProvider.class);
        mAppMetricaPushTokenEventSerializer = mock(AppMetricaPushTokenEventSerializer.class);
        mNotificationChannelController = mock(NotificationChannelController.class);
        mMainProcessDetector = mock(MainProcessDetector.class);
        mPushFilterFacade = mock(PushFilterFacade.class);
        mPreLazyFilterFacade = mock(PreLazyFilterFacade.class);
        mPassportUidProvider = mock(PassportUidProvider.class);
    }

    @NonNull
    @Override
    public PushProcessor getPushProcessor() {
        return mPushProcessor;
    }

    @NonNull
    @Override
    public PushProcessingStrategyProvider getPushProcessingStrategyProvider() {
        return mPushProcessingStrategyProvider;
    }

    @NonNull
    @Override
    public NotificationActionListener getNotificationActionListener() {
        return mNotificationActionListener;
    }

    @NonNull
    @Override
    public NotificationActionProcessor getNotificationActionProcessor() {
        return mNotificationActionProcessor;
    }

    @NonNull
    @Override
    public PushMessageTracker getPushMessageTracker() {
        return mPushMessageTracker;
    }

    @NonNull
    @Override
    public AutoTrackingConfiguration getAutoTrackingConfiguration() {
        return mAutoTrackingConfiguration;
    }

    @NonNull
    @Override
    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    @NonNull
    @Override
    public PushMessageHistory getPushMessageHistory() {
        return mPushMessageHistory;
    }

    @NonNull
    @Override
    public NotificationStatusProvider getNotificationStatusProvider() {
        return mNotificationStatusProvider;
    }

    @NonNull
    @Override
    public AppMetricaPushTokenEventSerializer getAppMetricaPushTokenEventSerializer() {
        return mAppMetricaPushTokenEventSerializer;
    }

    @NonNull
    @Override
    public NotificationChannelController getNotificationChannelController() {
        return mNotificationChannelController;
    }

    @NonNull
    @Override
    public MainProcessDetector getMainProcessDetector() {
        return mMainProcessDetector;
    }

    @NonNull
    @Override
    public PushFilterFacade getPushFilterFacade() {
        return mPushFilterFacade;
    }

    @NonNull
    @Override
    public PreLazyFilterFacade getPreLazyFilterFacade() {
        return mPreLazyFilterFacade;
    }

    @Nullable
    @Override
    public PassportUidProvider getPassportUidProvider() {
        return mPassportUidProvider;
    }

    @Override
    public void setAutoTrackingConfiguration(@NonNull AutoTrackingConfiguration autoTrackingConfiguration) {
        mAutoTrackingConfiguration = autoTrackingConfiguration;
    }

    @Override
    public void setPassportUidProvider(@NonNull PassportUidProvider passportUidProvider) {
        mPassportUidProvider = passportUidProvider;
    }

    @Override
    public void setPushMessageHistory(@NonNull PushMessageHistory pushMessageHistory) {
        mPushMessageHistory = pushMessageHistory;
    }

    @Override
    public void setPreferenceManager(@NonNull PreferenceManager preferenceManager) {
        mPreferenceManager = preferenceManager;
    }

    @Override
    public void setNotificationChannelController(@NonNull NotificationChannelController notificationChannelController) {
        mNotificationChannelController = notificationChannelController;
    }

    @Override
    public void setPushFilterFacade(@NonNull PushFilterFacade pushFilterFacade) {
        mPushFilterFacade = pushFilterFacade;
    }

    @Override
    public void setPreLazyFilterFacade(@NonNull PreLazyFilterFacade preLazyFilterFacade) {
        mPreLazyFilterFacade = preLazyFilterFacade;
    }
}

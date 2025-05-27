package io.appmetrica.analytics.push.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.notification.NotificationActionHandler;
import io.appmetrica.analytics.push.impl.notification.NotificationActionListener;
import io.appmetrica.analytics.push.impl.notification.NotificationChannelController;
import io.appmetrica.analytics.push.impl.notification.NotificationStatusProvider;
import io.appmetrica.analytics.push.impl.notification.processing.AdditionalActionProcessingStrategy;
import io.appmetrica.analytics.push.impl.notification.processing.ClearNotificationProcessingStrategy;
import io.appmetrica.analytics.push.impl.notification.processing.DefaultNotificationActionProcessor;
import io.appmetrica.analytics.push.impl.notification.processing.InlineActionProcessingStrategy;
import io.appmetrica.analytics.push.impl.notification.processing.NotificationActionProcessor;
import io.appmetrica.analytics.push.impl.notification.processing.OpenActionProcessingStrategy;
import io.appmetrica.analytics.push.impl.processing.DefaultPushProcessingStrategyProvider;
import io.appmetrica.analytics.push.impl.processing.DefaultPushProcessor;
import io.appmetrica.analytics.push.impl.processing.PushProcessingStrategyProvider;
import io.appmetrica.analytics.push.impl.processing.PushProcessor;
import io.appmetrica.analytics.push.impl.processing.transform.filter.PreLazyFilterFacade;
import io.appmetrica.analytics.push.impl.processing.transform.filter.PushFilterFacade;
import io.appmetrica.analytics.push.impl.tracking.AppMetricaPushMessageTracker;
import io.appmetrica.analytics.push.impl.tracking.AppMetricaPushTokenEventSerializer;
import io.appmetrica.analytics.push.impl.tracking.InternalPushMessageTracker;
import io.appmetrica.analytics.push.impl.utils.MainProcessDetector;
import io.appmetrica.analytics.push.settings.AutoTrackingConfiguration;
import io.appmetrica.analytics.push.settings.PassportUidProvider;

public class AppMetricaPushServiceProvider implements PushServiceProvider {

    private final Object lock = new Object();

    @Nullable
    private volatile PushProcessingStrategyProvider pushProcessingStrategyProvider;
    @Nullable
    private volatile PushProcessor pushProcessor;
    @Nullable
    private volatile NotificationActionListener notificationActionListener;
    @Nullable
    private volatile NotificationActionProcessor notificationActionProcessor;
    @Nullable
    private volatile InternalPushMessageTracker pushMessageTracker;
    @Nullable
    private volatile AutoTrackingConfiguration autoTrackingConfiguration;
    @Nullable
    private volatile PreferenceManager preferenceManager;
    @Nullable
    private volatile PushMessageHistory pushMessageHistory;
    @Nullable
    private volatile NotificationStatusProvider notificationStatusProvider;
    @Nullable
    private volatile AppMetricaPushTokenEventSerializer appMetricaPushTokenEventSerializer;
    @Nullable
    private volatile NotificationChannelController notificationChannelController;
    @Nullable
    private volatile MainProcessDetector mainProcessDetector;
    @Nullable
    private volatile PushFilterFacade pushFilterFacade;
    @Nullable
    private volatile PreLazyFilterFacade preLazyFilterFacade;
    @Nullable
    private PassportUidProvider passportUidProvider;

    @NonNull
    private final Context context;
    @NonNull
    private final AppMetricaPushCore pushCore;

    public AppMetricaPushServiceProvider(@NonNull Context context, @NonNull AppMetricaPushCore pushCore) {
        this.context = context;
        this.pushCore = pushCore;
    }

    @NonNull
    @Override
    public PushProcessingStrategyProvider getPushProcessingStrategyProvider() {
        if (pushProcessingStrategyProvider == null) {
            synchronized (lock) {
                if (pushProcessingStrategyProvider == null) {
                    pushProcessingStrategyProvider = new DefaultPushProcessingStrategyProvider();
                }
            }
        }
        return pushProcessingStrategyProvider;
    }

    public void setPushProcessingStrategyProvider(
        @NonNull final PushProcessingStrategyProvider pushProcessingStrategyProvider
    ) {
        synchronized (lock) {
            this.pushProcessingStrategyProvider = pushProcessingStrategyProvider;
        }
    }

    @NonNull
    @Override
    public PushProcessor getPushProcessor() {
        if (pushProcessor == null) {
            synchronized (lock) {
                if (pushProcessor == null) {
                    pushProcessor = new DefaultPushProcessor();
                }
            }
        }
        return pushProcessor;
    }

    public void setPushProcessor(@NonNull final PushProcessor pushProcessor) {
        synchronized (lock) {
            this.pushProcessor = pushProcessor;
        }
    }

    @NonNull
    @Override
    public NotificationActionListener getNotificationActionListener() {
        if (notificationActionListener == null) {
            synchronized (lock) {
                if (notificationActionListener == null) {
                    notificationActionListener = new NotificationActionHandler();
                }
            }
        }
        return notificationActionListener;
    }

    public void setNotificationActionListener(@NonNull final NotificationActionListener notificationActionListener) {
        synchronized (lock) {
            this.notificationActionListener = notificationActionListener;
        }
    }

    @NonNull
    @Override
    public NotificationActionProcessor getNotificationActionProcessor() {
        if (notificationActionProcessor == null) {
            synchronized (lock) {
                if (notificationActionProcessor == null) {
                    notificationActionProcessor = new DefaultNotificationActionProcessor();
                    notificationActionProcessor
                        .setClearNotificationProcessingStrategy(new ClearNotificationProcessingStrategy());
                    notificationActionProcessor
                        .setOpenActionProcessingStrategy(new OpenActionProcessingStrategy());
                    notificationActionProcessor
                        .setAdditionalActionProcessingStrategy(new AdditionalActionProcessingStrategy());
                    notificationActionProcessor
                        .setInlineActionProcessingStrategy(new InlineActionProcessingStrategy());
                }
            }
        }
        return notificationActionProcessor;
    }

    public void setNotificationActionProcessor(@NonNull final NotificationActionProcessor notificationActionProcessor) {
        synchronized (lock) {
            this.notificationActionProcessor = notificationActionProcessor;
        }
    }

    @NonNull
    @Override
    public InternalPushMessageTracker getPushMessageTracker() {
        if (pushMessageTracker == null) {
            synchronized (lock) {
                if (pushMessageTracker == null) {
                    pushMessageTracker = new AppMetricaPushMessageTracker(getPreferenceManager());
                }
            }
        }
        return pushMessageTracker;
    }

    public void setPushMessageTracker(@NonNull final InternalPushMessageTracker pushMessageTracker) {
        synchronized (lock) {
            this.pushMessageTracker = pushMessageTracker;
        }
    }

    @NonNull
    @Override
    public AutoTrackingConfiguration getAutoTrackingConfiguration() {
        if (autoTrackingConfiguration == null) {
            synchronized (lock) {
                if (autoTrackingConfiguration == null) {
                    autoTrackingConfiguration = AutoTrackingConfiguration.newBuilder().build();
                }
            }
        }
        return autoTrackingConfiguration;
    }

    @Override
    public void setAutoTrackingConfiguration(@NonNull final AutoTrackingConfiguration autoTrackingConfiguration) {
        synchronized (lock) {
            this.autoTrackingConfiguration = autoTrackingConfiguration;
        }
    }

    @NonNull
    @Override
    public PreferenceManager getPreferenceManager() {
        if (preferenceManager == null) {
            synchronized (lock) {
                if (preferenceManager == null) {
                    preferenceManager = new PreferenceManager(context);
                }
            }
        }
        return preferenceManager;
    }

    @Override
    public void setPreferenceManager(@NonNull PreferenceManager preferenceManager) {
        synchronized (lock) {
            this.preferenceManager = preferenceManager;
        }
    }

    @NonNull
    @Override
    public PushMessageHistory getPushMessageHistory() {
        if (pushMessageHistory == null) {
            PreferenceManager preferenceManager = getPreferenceManager();
            synchronized (lock) {
                if (pushMessageHistory == null) {
                    pushMessageHistory = new PushMessageHistory(preferenceManager);
                }
            }
        }
        return pushMessageHistory;
    }

    @Override
    public void setPushMessageHistory(@NonNull PushMessageHistory pushMessageHistory) {
        synchronized (lock) {
            this.pushMessageHistory = pushMessageHistory;
        }
    }

    @NonNull
    @Override
    public NotificationStatusProvider getNotificationStatusProvider() {
        if (notificationStatusProvider == null) {
            synchronized (lock) {
                if (notificationStatusProvider == null) {
                    notificationStatusProvider = new NotificationStatusProvider(context);
                }
            }
        }
        return notificationStatusProvider;
    }

    public void setNotificationStatusProvider(@NonNull NotificationStatusProvider notificationStatusProvider) {
        synchronized (lock) {
            this.notificationStatusProvider = notificationStatusProvider;
        }
    }

    @NonNull
    @Override
    public AppMetricaPushTokenEventSerializer getAppMetricaPushTokenEventSerializer() {
        if (appMetricaPushTokenEventSerializer == null) {
            synchronized (lock) {
                if (appMetricaPushTokenEventSerializer == null) {
                    appMetricaPushTokenEventSerializer = new AppMetricaPushTokenEventSerializer();
                }
            }
        }
        return appMetricaPushTokenEventSerializer;
    }

    public void setAppMetricaPushTokenEventSerializer(
        @NonNull AppMetricaPushTokenEventSerializer appMetricaPushTokenEventSerializer
    ) {
        synchronized (lock) {
            this.appMetricaPushTokenEventSerializer = appMetricaPushTokenEventSerializer;
        }
    }

    @NonNull
    @Override
    public NotificationChannelController getNotificationChannelController() {
        if (notificationChannelController == null) {
            synchronized (lock) {
                if (notificationChannelController == null) {
                    notificationChannelController = new NotificationChannelController(context);
                }
            }
        }
        return notificationChannelController;
    }

    @Override
    public void setNotificationChannelController(@NonNull NotificationChannelController notificationChannelController) {
        synchronized (lock) {
            this.notificationChannelController = notificationChannelController;
        }
    }

    @NonNull
    @Override
    public MainProcessDetector getMainProcessDetector() {
        if (mainProcessDetector == null) {
            synchronized (lock) {
                if (mainProcessDetector == null) {
                    mainProcessDetector = new MainProcessDetector();
                }
            }
        }
        return mainProcessDetector;
    }

    public void setMainProcessDetector(@NonNull MainProcessDetector mainProcessDetector) {
        synchronized (lock) {
            this.mainProcessDetector = mainProcessDetector;
        }
    }

    @NonNull
    @Override
    public PushFilterFacade getPushFilterFacade() {
        if (pushFilterFacade == null) {
            synchronized (lock) {
                if (pushFilterFacade == null) {
                    pushFilterFacade = new PushFilterFacade(context, pushCore);
                }
            }
        }
        return pushFilterFacade;
    }

    @Override
    public void setPushFilterFacade(@NonNull PushFilterFacade pushFilterFacade) {
        synchronized (lock) {
            this.pushFilterFacade = pushFilterFacade;
        }
    }

    @NonNull
    @Override
    public PreLazyFilterFacade getPreLazyFilterFacade() {
        if (preLazyFilterFacade == null) {
            synchronized (lock) {
                if (preLazyFilterFacade == null) {
                    preLazyFilterFacade = new PreLazyFilterFacade(context, pushCore);
                }
            }
        }
        return preLazyFilterFacade;
    }

    @Override
    public void setPreLazyFilterFacade(@Nullable PreLazyFilterFacade preLazyFilterFacade) {
        synchronized (lock) {
            this.preLazyFilterFacade = preLazyFilterFacade;
        }
    }

    @Nullable
    @Override
    public PassportUidProvider getPassportUidProvider() {
        return passportUidProvider;
    }

    @Override
    public void setPassportUidProvider(@Nullable PassportUidProvider passportUidProvider) {
        this.passportUidProvider = passportUidProvider;
    }
}

package io.appmetrica.analytics.push.impl;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.content.Context;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.ModulesFacade;
import io.appmetrica.analytics.push.TokenUpdateListener;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.event.PushEvent;
import io.appmetrica.analytics.push.impl.event.InternalPushMessageTrackerWrapper;
import io.appmetrica.analytics.push.impl.notification.NotificationChannelController;
import io.appmetrica.analytics.push.impl.notification.NotificationStatusProvider;
import io.appmetrica.analytics.push.impl.processing.transform.filter.PreLazyFilterFacade;
import io.appmetrica.analytics.push.impl.processing.transform.filter.PushFilterFacade;
import io.appmetrica.analytics.push.impl.token.TokenManager;
import io.appmetrica.analytics.push.impl.tracking.BaseAppMetricaPushMessageTracker;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.impl.utils.AppMetricaTracker;
import io.appmetrica.analytics.push.impl.utils.MainProcessDetector;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import io.appmetrica.analytics.push.provider.api.PushServiceController;
import io.appmetrica.analytics.push.provider.api.PushServiceControllerProvider;
import io.appmetrica.analytics.push.provider.firebase.FirebasePushServiceControllerProvider;
import io.appmetrica.analytics.push.settings.PassportUidProvider;
import java.util.ArrayList;

public final class AppMetricaPushCore {

    private static final String METRICA_API_KEY = "0e5e9c33-f8c3-4568-86c5-2e4f57523f72";

    @SuppressLint("StaticFieldLeak")
    private static volatile AppMetricaPushCore instance;
    private static final Object INSTANCE_MONITOR = new Object();

    @NonNull
    private final Context context;

    @NonNull
    private final Object initLock = new Object();
    private boolean initialized = false;

    @Nullable
    private PushServiceControllerComposite pushServiceController;
    @Nullable
    private TokenManager tokenManager;

    @NonNull
    private PushServiceProvider pushServiceProvider;
    @NonNull
    private InternalPushMessageTrackerWrapper pushMessageTracker;
    @Nullable
    private TokenUpdateListener tokenUpdateListener;

    @NonNull
    public static AppMetricaPushCore getInstance(@NonNull final Context context) {
        if (instance == null) {
            synchronized (INSTANCE_MONITOR) {
                if (instance == null) {
                    instance = new AppMetricaPushCore(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    @VisibleForTesting
    AppMetricaPushCore(@NonNull final Context applicationContext) {
        context = applicationContext;
        pushServiceProvider = new AppMetricaPushServiceProvider(context, this);
        initTrackers(context);
        pushMessageTracker = new InternalPushMessageTrackerWrapper(
            new BaseAppMetricaPushMessageTracker(pushServiceProvider.getPreferenceManager())
        );
    }

    private void initTrackers(@NonNull final Context context) {
        TrackersHub.getInstance().registerTracker(
            new AppMetricaTracker(context, METRICA_API_KEY, pushServiceProvider.getPreferenceManager())
        );
        PushMessageTrackerHub.getInstance().registerTracker(pushServiceProvider.getPushMessageTracker());
    }

    @MainThread
    public void init() {
        init(new FirebasePushServiceControllerProvider(context));
    }

    @MainThread
    public void init(PushServiceControllerProvider... providers) {
        if (getMainProcessDetector().isMainProcess()) {
            synchronized (initLock) {
                if (!initialized) {
                    PublicLogger.INSTANCE.info("Initializing AppMetrica Push SDK");

                    //We don't try to determine the duration of the user session.
                    //It's only need to detect the fact of launching sdk.
                    TrackersHub.getInstance().resumeSession();
                    TrackersHub.getInstance().pauseSession();

                    if (!ModulesFacade.isActivatedForApp()) {
                        throw new IllegalStateException(Constants.EXCEPTION_MESSAGE_METRICA_IS_NOT_ACTIVATED);
                    }

                    ArrayList<PushServiceController> controllers = new ArrayList<>(providers.length);

                    for (PushServiceControllerProvider provider : providers) {
                        controllers.add(provider.getPushServiceController());
                    }

                    pushServiceController = new PushServiceControllerComposite(context, controllers);
                    tokenManager = new TokenManager(context, pushServiceController.getTransportIds());
                    PushServiceFacade.INSTANCE.initPushService(context);

                    initialized = true;
                } else {
                    PublicLogger.INSTANCE.warning("AppMetrica Push SDK has already been initialized.");
                }
            }
        } else {
            PublicLogger.INSTANCE.info("Ignore AppMetrica Push SDK initialization from non main process");
        }
    }

    @Nullable
    public PushServiceControllerComposite getPushServiceController() {
        return pushServiceController;
    }

    @Nullable
    public TokenManager getTokenManager() {
        return tokenManager;
    }

    @NonNull
    public PushMessageHistory getPushMessageHistory() {
        return getPushServiceProvider().getPushMessageHistory();
    }

    @NonNull
    private Context getContext() {
        return context;
    }

    @NonNull
    public PreferenceManager getPreferenceManager() {
        return getPushServiceProvider().getPreferenceManager();
    }

    @NonNull
    public PushServiceProvider getPushServiceProvider() {
        return pushServiceProvider;
    }

    public boolean isInitialized() {
        synchronized (initLock) {
            return initialized;
        }
    }

    @NonNull
    public NotificationChannelController getNotificationChannelController() {
        return getPushServiceProvider().getNotificationChannelController();
    }

    @Nullable
    public NotificationChannel getDefaultNotificationChannel() {
        return getNotificationChannelController().getDefaultChannel();
    }

    @NonNull
    public NotificationStatusProvider getNotificationStatusProvider() {
        return getPushServiceProvider().getNotificationStatusProvider();
    }

    @NonNull
    private MainProcessDetector getMainProcessDetector() {
        return getPushServiceProvider().getMainProcessDetector();
    }

    @Nullable
    public PassportUidProvider getPassportUidProvider() {
        return getPushServiceProvider().getPassportUidProvider();
    }

    public void setPassportUidProvider(@NonNull PassportUidProvider passportUidProvider) {
        getPushServiceProvider().setPassportUidProvider(passportUidProvider);
    }

    @NonNull
    public PushFilterFacade getPushFilterFacade() {
        return getPushServiceProvider().getPushFilterFacade();
    }

    @NonNull
    public PreLazyFilterFacade getPreLazyFilterFacade() {
        return getPushServiceProvider().getPreLazyFilterFacade();
    }

    public void reportPushEvent(@NonNull final PushEvent pushEvent) {
        pushMessageTracker.reportPushEvent(pushEvent);
    }

    @VisibleForTesting
    public void setPushServiceProvider(@NonNull final PushServiceProvider pushServiceProvider) {
        this.pushServiceProvider = pushServiceProvider;
    }

    @VisibleForTesting
    public void setPushMessageHistory(@NonNull final PushMessageHistory pushMessageHistory) {
        getPushServiceProvider().setPushMessageHistory(pushMessageHistory);
    }

    @VisibleForTesting
    public void setPreferenceManager(@NonNull final PreferenceManager preferenceManager) {
        getPushServiceProvider().setPreferenceManager(preferenceManager);
    }

    @VisibleForTesting
    public void setNotificationChannelController(@NonNull NotificationChannelController notificationChannelController) {
        getPushServiceProvider().setNotificationChannelController(notificationChannelController);
    }

    @VisibleForTesting
    public void setPushFilterFacade(@NonNull PushFilterFacade pushFilterFacade) {
        getPushServiceProvider().setPushFilterFacade(pushFilterFacade);
    }

    @VisibleForTesting
    public void setPreLazyFilterFacade(@NonNull PreLazyFilterFacade preLazyFilterFacade) {
        getPushServiceProvider().setPreLazyFilterFacade(preLazyFilterFacade);
    }

    @VisibleForTesting
    public void setPushMessageTracker(@NonNull InternalPushMessageTrackerWrapper tracker) {
        pushMessageTracker = tracker;
    }

    @Nullable
    public TokenUpdateListener getTokenUpdateListener() {
        return tokenUpdateListener;
    }

    public void setTokenUpdateListener(@NonNull TokenUpdateListener listener) {
        PublicLogger.INSTANCE.info("Setting token update listener");
        this.tokenUpdateListener = listener;
    }
}

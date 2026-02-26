package io.appmetrica.analytics.push.provider.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;

/**
 * Subclass of {@link InstanceIDListenerService} that receive and handle push token from GCM.
 */
public class AppMetricaInstanceIDListenerService extends InstanceIDListenerService {

    /**
     * Creates a new instance of {@link AppMetricaInstanceIDListenerService}.
     */
    public AppMetricaInstanceIDListenerService() {
    }

    private static final String TAG = "[AppMetricaInstanceIDListenerService]";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        processToken();
    }

    /**
     * Process push token from GCM.
     */
    public void processToken() {
        try {
            DebugLogger.INSTANCE.info(TAG, "onTokenRefresh");
            TrackersHub.getInstance().reportEvent("InstanceIDListenerService refresh token");
            PushServiceFacade.sendToken(this, CoreConstants.Transport.GCM, null);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to refresh gcm token", e);
        }
    }
}

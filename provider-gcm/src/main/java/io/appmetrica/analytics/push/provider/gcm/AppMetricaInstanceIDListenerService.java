package io.appmetrica.analytics.push.provider.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;

/**
 * Subclass of {@link InstanceIDListenerService} that receive and handle push token from GCM.
 */
public class AppMetricaInstanceIDListenerService extends InstanceIDListenerService {

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
            PLog.d("onTokenRefresh");
            TrackersHub.getInstance().reportEvent("InstanceIDListenerService refresh token");
            PushServiceFacade.refreshToken(this);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to refresh gcm token", e);
        }
    }
}

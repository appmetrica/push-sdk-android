package io.appmetrica.analytics.push.provider.gcm;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.gms.gcm.GcmListenerService;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;

/**
 * Subclass of {@link GcmListenerService} that receive and handle push messages from GCM.
 */
public class AppMetricaGcmListenerService extends GcmListenerService {

    private static final String EVENT_PUSH_RECEIVED = "GcmListenerService receive push";

    @Override
    public void onMessageReceived(@NonNull String from, @NonNull Bundle data) {
        super.onMessageReceived(from, data);
        processPush(this, data);
    }

    /**
     * @param context {@link Context} object
     * @param data received push data
     */
    public void processPush(@NonNull final Context context, @NonNull final Bundle data) {
        try {
            PublicLogger.INSTANCE.info("Receive\nfullData: %s", data);
            TrackersHub.getInstance().reportEvent(EVENT_PUSH_RECEIVED);
            PushServiceFacade.processPush(context, data, CoreConstants.Transport.GCM);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to process gcm push", e);
        }
    }

    /**
     * Method checks if notification is related to AppMetrica Push SDK.
     *
     * @param notification {@link Bundle} bundle with notification
     * @return true if notification from bundle is related to AppMetrica Push SDK. And false otherwise
     */
    public static boolean isNotificationRelatedToSDK(@NonNull final Bundle notification) {
        return CoreUtils.isNotificationRelatedToSDK(notification);
    }
}

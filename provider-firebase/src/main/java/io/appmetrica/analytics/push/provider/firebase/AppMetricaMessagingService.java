package io.appmetrica.analytics.push.provider.firebase;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;

/**
 * Subclass of {@link FirebaseMessagingService} that receive and handle push token and push messages from Firebase.
 */
public class AppMetricaMessagingService extends FirebaseMessagingService {

    private static final String EVENT_PUSH_RECEIVED = "FirebaseMessagingService receive push";

    @Override
    public void onMessageReceived(@NonNull final RemoteMessage message) {
        super.onMessageReceived(message);
        processPush(this, message);
    }

    @Override
    public void onNewToken(@NonNull final String token) {
        super.onNewToken(token);
        processToken(this, token);
    }

    /**
     * @param context {@link Context} object
     * @param message received {@link RemoteMessage}
     */
    public void processPush(@NonNull final Context context, @NonNull final RemoteMessage message) {
        try {
            Bundle data = CoreUtils.fromMapToBundle(message.getData());

            processPush(context, data);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to process firebase push", e);
        }
    }

    /**
     * @param context {@link Context} object
     * @param data {@link Bundle} that is parsed from {@link RemoteMessage}
     */
    public void processPush(@NonNull final Context context, @NonNull final Bundle data) {
        try {
            PublicLogger.d("Receive\nfullData: %s", data);
            TrackersHub.getInstance().reportEvent(EVENT_PUSH_RECEIVED);
            PushServiceFacade.processPush(context, data, CoreConstants.Transport.FIREBASE);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to process firebase push", e);
        }
    }

    /**
     * @param context {@link Context} object
     * @param token received push token
     */
    public void processToken(@NonNull final Context context, @NonNull final String token) {
        try {
            PLog.d("onTokenRefresh");
            TrackersHub.getInstance().reportEvent("FirebaseInstanceIdService refresh token");
            PushServiceFacade.refreshToken(context);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to refresh firebase token", e);
        }
    }

    /**
     * Method checks if notification is related to AppMetrica Push SDK.
     *
     * @param notification {@link RemoteMessage} bundle with notification
     * @return true if notification from bundle is related to AppMetrica Push SDK. And false otherwise
     */
    public static boolean isNotificationRelatedToSDK(@NonNull final RemoteMessage notification) {
        return CoreUtils.isNotificationRelatedToSDK(CoreUtils.fromMapToBundle(notification.getData()));
    }
}

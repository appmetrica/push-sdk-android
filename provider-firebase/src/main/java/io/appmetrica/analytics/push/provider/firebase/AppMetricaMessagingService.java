package io.appmetrica.analytics.push.provider.firebase;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;

/**
 * Subclass of {@link FirebaseMessagingService} that receive and handle push token and push messages from Firebase.
 */
public class AppMetricaMessagingService extends FirebaseMessagingService {

    private static final String TAG = "[AppMetricaMessagingService]";

    private static final String EVENT_PUSH_RECEIVED = "FirebaseMessagingService receive push";
    private static final String TRANSPORT = CoreConstants.Transport.FIREBASE;
    private static final String SERVICE_NAME_FOR_EVENT = "FirebaseInstanceIdService";
    private static final String EVENT_NAME_ON_NEW_TOKEN = SERVICE_NAME_FOR_EVENT + " onNewToken";
    private static final String EVENT_NAME_PROCESS_TOKEN = SERVICE_NAME_FOR_EVENT + " processToken";
    private static final String TOKEN_ERROR = "Token processing failed";

    @Override
    public void onMessageReceived(@NonNull final RemoteMessage message) {
        super.onMessageReceived(message);
        processPush(this, message);
    }

    @Override
    public void onNewToken(@NonNull final String token) {
        super.onNewToken(token);
        try {
            PublicLogger.INSTANCE.info("onNewToken: %s", token);
            TrackersHub.getInstance().reportEvent(EVENT_NAME_ON_NEW_TOKEN);
            PushServiceFacade.sendTokenOnRefresh(this, TRANSPORT, token);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError(TOKEN_ERROR, e);
        }
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
            PublicLogger.INSTANCE.info("Receive\nfullData: %s", data);
            TrackersHub.getInstance().reportEvent(EVENT_PUSH_RECEIVED);
            PushServiceFacade.processPush(context, data, TRANSPORT);
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
            DebugLogger.INSTANCE.info(TAG, "processToken");
            TrackersHub.getInstance().reportEvent(EVENT_NAME_PROCESS_TOKEN);
            PushServiceFacade.sendTokenManually(context, TRANSPORT, token);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError(TOKEN_ERROR, e);
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

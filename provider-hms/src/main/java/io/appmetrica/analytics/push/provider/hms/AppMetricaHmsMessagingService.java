package io.appmetrica.analytics.push.provider.hms;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import io.appmetrica.analytics.push.provider.hms.impl.TokenHolder;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Subclass of {@link HmsMessageService}
 * that receive and handle push token and push messages from Huawei Media Services.
 */
public class AppMetricaHmsMessagingService extends HmsMessageService {

    /**
     * Creates a new instance of {@link AppMetricaHmsMessagingService}.
     */
    public AppMetricaHmsMessagingService() {
    }

    private static final String TAG = "[AppMetricaHmsMessagingService]";

    private static final String EVENT_PUSH_RECEIVED = "HmsMessagingService receive push";
    private static final String TRANSPORT = CoreConstants.Transport.HMS;
    private static final String SERVICE_NAME_FOR_EVENT = "HmsInstanceIdService";
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
            DebugLogger.INSTANCE.info(TAG, "onTokenRefresh");
            TrackersHub.getInstance().reportEvent(EVENT_NAME_ON_NEW_TOKEN);
            TokenHolder.getInstance().setTokenFromService(token);
            PushServiceFacade.sendTokenOnRefresh(this, TRANSPORT, token);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError(TOKEN_ERROR, e);
        }
    }

    /**
     * Process push message from Huawei media services.
     *
     * @param context {@link Context} object
     * @param message received {@link RemoteMessage}
     */
    public void processPush(@NonNull final Context context, @NonNull final RemoteMessage message) {
        try {
            Bundle data = jsonToBundle(message.getData());

            processPush(context, data);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to process hms push", e);
        }
    }

    /**
     * Process push message from Huawei media services.
     *
     * @param context {@link Context} object
     * @param data {@link Bundle} that was parsed from received {@link RemoteMessage}
     */
    public void processPush(@NonNull final Context context, @NonNull final Bundle data) {
        try {
            PublicLogger.INSTANCE.info("Receive\nfullData: %s", data);
            TrackersHub.getInstance().reportEvent(EVENT_PUSH_RECEIVED);
            PushServiceFacade.processPush(context, data, TRANSPORT);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to process hms push", e);
        }
    }

    /**
     * Process push token from Huawei media services.
     *
     * @param context {@link Context} object
     * @param token received push token
     */
    public void processToken(@NonNull final Context context, @NonNull final String token) {
        try {
            DebugLogger.INSTANCE.info(TAG, "processToken");
            TrackersHub.getInstance().reportEvent(EVENT_NAME_PROCESS_TOKEN);
            TokenHolder.getInstance().setTokenFromService(token);
            PushServiceFacade.sendTokenManually(context, TRANSPORT, token);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError(TOKEN_ERROR, e);
        }
    }

    //HMS provides string. GCM and Firebase provides Map<String, String>.
    //To unify those libraries we must send one-level json, consist from strings only.
    @VisibleForTesting
    static Bundle jsonToBundle(@Nullable String data) {
        Bundle output = new Bundle();
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject object = new JSONObject(data);
                Iterator<String> keys = object.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    try {
                        output.putString(key, object.getString(key));
                    } catch (JSONException e) {
                        DebugLogger.INSTANCE.error(TAG, e, "can't read string for key %s", key);
                    }
                }
            } catch (JSONException e) {
                DebugLogger.INSTANCE.error(TAG, e, "exception during json parsing");
            }
        }
        return output;
    }

    /**
     * Method checks if notification is related to AppMetrica Push SDK.
     *
     * @param notification {@link RemoteMessage} bundle with notification
     * @return true if notification from bundle is related to AppMetrica Push SDK. And false otherwise
     */
    public static boolean isNotificationRelatedToSDK(@NonNull final RemoteMessage notification) {
        return CoreUtils.isNotificationRelatedToSDK(jsonToBundle(notification.getData()));
    }
}

package io.appmetrica.analytics.push.provider.rustore;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import java.util.List;
import ru.rustore.sdk.pushclient.messaging.exception.RuStorePushClientException;
import ru.rustore.sdk.pushclient.messaging.model.RemoteMessage;
import ru.rustore.sdk.pushclient.messaging.service.RuStoreMessagingService;

/**
 * Subclass of {@link RuStoreMessagingService} that receive and handle push token and push messages from RuStore.
 */
public class AppMetricaRuStoreMessagingService extends RuStoreMessagingService {

    private static final String EVENT_PUSH_RECEIVED = "RuStoreMessagingService receive push";
    private static final String TOKEN_RECEIVED = "RuStoreMessagingService refresh token";

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

    @Override
    public void onError(@NonNull final List<? extends RuStorePushClientException> errors) {
        super.onError(errors);
        PublicLogger.d("RuStore errors occurred:");
        for (final RuStorePushClientException error : errors) {
            PublicLogger.d(error.getMessage());
        }
    }

    /**
     * Process push message from RuStore.
     *
     * @param context {@link Context} object
     * @param message received {@link RemoteMessage}
     */
    public void processPush(@NonNull final Context context, @NonNull final RemoteMessage message) {
        try {
            final Bundle data = CoreUtils.fromMapToBundle(message.getData());
            processPush(context, data);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to process RuStore push", e);
        }
    }

    /**
     * Process push message from RuStore.
     *
     * @param context {@link Context} object
     * @param data {@link Bundle} that was parsed from received {@link RemoteMessage}
     */
    public void processPush(@NonNull final Context context, @NonNull final Bundle data) {
        try {
            PublicLogger.d("Receive\nfullData: %s", data);
            TrackersHub.getInstance().reportEvent(EVENT_PUSH_RECEIVED);
            PushServiceFacade.processPush(context, data, CoreConstants.Transport.RUSTORE);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to process RuStore push", e);
        }
    }

    /**
     * Process push token from RuStore.
     *
     * @param context {@link Context} object
     * @param token received push token
     */
    public void processToken(@NonNull final Context context, @NonNull final String token) {
        try {
            PLog.d("onTokenRefresh");
            TrackersHub.getInstance().reportEvent(TOKEN_RECEIVED);
            PushServiceFacade.refreshToken(context);
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to refresh rustore token", e);
        }
    }

    /**
     * Method checks if notification is related to AppMetrica Push SDK.
     *
     * @param notification [RemoteMessage] bundle with notification
     * @return true if notification from bundle is related to AppMetrica Push SDK. And false otherwise
     */
    public boolean isNotificationRelatedToSDK(@NonNull final RemoteMessage notification) {
        return CoreUtils.isNotificationRelatedToSDK(CoreUtils.fromMapToBundle(notification.getData()));
    }
}

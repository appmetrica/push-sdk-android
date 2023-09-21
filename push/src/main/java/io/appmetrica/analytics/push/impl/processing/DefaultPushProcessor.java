package io.appmetrica.analytics.push.impl.processing;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.model.BasePushMessage;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.impl.PushServiceProvider;
import io.appmetrica.analytics.push.impl.processing.strategy.PushStrategy;
import io.appmetrica.analytics.push.impl.processing.transform.TransformFacade;
import io.appmetrica.analytics.push.impl.processing.transform.TransformResult;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.model.PushMessage;
import java.util.HashMap;

public class DefaultPushProcessor implements PushProcessor {

    @Override
    public void processPush(@NonNull final Context context, @Nullable final Bundle bundle) {
        if (bundle != null) {
            final BasePushMessage basePush = new BasePushMessage(bundle);
            if (basePush.isOwnPush()) {
                PushMessage pushMessage = new PushMessage(context, bundle);
                try {
                    processPush(context, pushMessage);
                } catch (Throwable e) {
                    reportIgnored(pushMessage, "Failed to process push", e.getMessage());
                }
            } else {
                PublicLogger.i("Receive not recognized push message");
            }
        } else {
            PublicLogger.w("Received push message with empty data bundle");
            TrackersHub.getInstance().reportError("Receive push message with empty bundle", null);
        }
    }

    private void processPush(@NonNull final Context context, @NonNull final PushMessage pushMessage) {
        PublicLogger.i(String.format("Process push with notificationId = %s", pushMessage.getNotificationId()));
        reportEvent(context, "Process push", pushMessage.getNotificationId());

        reportReceive(context, pushMessage);

        TransformResult result = new TransformFacade(context).transform(pushMessage);
        if (result.isSuccess()) {
            strategyProcessPush(context, result.pushMessage);
        } else {
            reportIgnored(result.pushMessage, result.filterResult.category, result.filterResult.details);
        }

        final PushMessageHistory pushMessageHistory = AppMetricaPushCore.getInstance(context).getPushMessageHistory();
        pushMessageHistory.addPush(result.pushMessage);
    }

    private void strategyProcessPush(@NonNull Context context, @NonNull PushMessage pushMessage) {
        final PushStrategy strategy = AppMetricaPushCore.getInstance(context)
            .getPushServiceProvider()
            .getPushProcessingStrategyProvider()
            .getPushStrategy(pushMessage);

        if (strategy != null) {
            strategy.processPush(context, pushMessage);
        } else {
            final String pushId = pushMessage.getNotificationId();
            String message = "Receive push with wrong format";
            PublicLogger.w("%s with pushId = %s", message, pushId);
            reportEvent(context, message, pushId);
            reportIgnored(pushMessage, Constants.IGNORED_CATEGORY_PUSH_DATA_FORMAT_IS_INVALID, message);
        }
    }

    private void reportReceive(@NonNull Context context, @NonNull PushMessage pushMessage) {
        final PushServiceProvider pushServiceProvider =
            AppMetricaPushCore.getInstance(context).getPushServiceProvider();
        final String pushId = pushMessage.getNotificationId();
        if (CoreUtils.isNotEmpty(pushId) && pushServiceProvider.getAutoTrackingConfiguration().trackingReceiveAction) {
            PushMessageTrackerHub.getInstance().onMessageReceived(
                pushId, pushMessage.getPayload(), pushMessage.getTransport()
            );
        }
    }

    private void reportIgnored(@NonNull PushMessage pushMessage, @Nullable String category, @Nullable String details) {
        PublicLogger.i("Push filtered out. Category: %s. Details: %s", category, details);
        final String pushId = pushMessage.getNotificationId();
        if (CoreUtils.isNotEmpty(pushId)) {
            PushMessageTrackerHub.getInstance().onNotificationIgnored(
                pushId, category, details, pushMessage.getPayload(), pushMessage.getTransport()
            );
        }
    }

    private void reportEvent(@NonNull final Context context, @NonNull final String eventName,
                             @Nullable final String notificationId) {
        final HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("notification_Id", notificationId);
        TrackersHub.getInstance().reportEvent(eventName, attributes);
    }
}

package io.appmetrica.analytics.push.impl.processing.strategy;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.model.PushMessage;

public class SilentPushStrategy implements PushStrategy {

    @Override
    public void processPush(@NonNull Context context, @NonNull PushMessage pushMessage) {
        String action = context.getPackageName() + Constants.SILENT_PUSH_POSTFIX;
        Intent intent = new Intent(action);
        intent.setPackage(context.getPackageName());
        String payload = pushMessage.getPayload();
        intent.putExtra(AppMetricaPush.EXTRA_PAYLOAD, payload);
        context.sendBroadcast(intent);
        final boolean autoTracking = AppMetricaPushCore.getInstance(context).getPushServiceProvider()
            .getAutoTrackingConfiguration().trackingProcessedAction;
        if (!CoreUtils.isEmpty(pushMessage.getNotificationId()) && autoTracking) {
            PushMessageTrackerHub.getInstance()
                .onSilentPushProcessed(
                    pushMessage.getNotificationId(), pushMessage.getPayload(), pushMessage.getTransport()
                );
        }
    }
}

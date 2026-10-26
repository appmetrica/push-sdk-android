package io.appmetrica.analytics.push.impl.notification.processing;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.content.IntentCompat;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.notification.OpenActivityStrategy;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;

public class OpenActionProcessingStrategy extends OpenActivityStrategy {

    @Override
    public void doAction(@NonNull Context context, @NonNull Intent intent) {
        final NotificationActionInfo actionInfo = IntentCompat.getParcelableExtra(
            intent, AppMetricaPush.EXTRA_ACTION_INFO, NotificationActionInfo.class
        );
        if (actionInfo != null) {
            final String pushId = actionInfo.pushId;
            final boolean autoTracking = AppMetricaPushCore.getInstance(context).getPushServiceProvider()
                .getAutoTrackingConfiguration().trackingOpenAction;
            if (!CoreUtils.isEmpty(pushId) && autoTracking) {
                PushMessageTrackerHub.getInstance().onPushOpened(
                    pushId,
                    actionInfo.payload,
                    actionInfo.transport,
                    actionInfo.targetActionUri
                );
            }
            openActionOrDefaultActivity(context, actionInfo);
            AppMetricaPushCore.getInstance(context).getPushMessageHistory().setPushActive(actionInfo.pushId, false);
        }
    }
}

package io.appmetrica.analytics.push.impl.notification.processing;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;

public class ClearNotificationProcessingStrategy implements NotificationActionProcessingStrategy {

    @Override
    public void doAction(@NonNull Context context, @NonNull Intent intent) {
        final NotificationActionInfo actionInfo = intent.getParcelableExtra(AppMetricaPush.EXTRA_ACTION_INFO);
        if (actionInfo != null) {
            final boolean autoTracking = AppMetricaPushCore.getInstance(context).getPushServiceProvider()
                .getAutoTrackingConfiguration().trackingDismissAction;
            if (!CoreUtils.isEmpty(actionInfo.pushId) && autoTracking) {
                PushMessageTrackerHub.getInstance().onNotificationCleared(
                    actionInfo.pushId, actionInfo.payload, actionInfo.transport
                );
            }
            AppMetricaPushCore.getInstance(context).getPushMessageHistory().setPushActive(actionInfo.pushId, false);
        }
    }
}

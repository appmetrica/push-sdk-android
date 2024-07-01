package io.appmetrica.analytics.push.impl.notification;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.notification.processing.NotificationActionProcessingStrategy;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;

public abstract class OpenActivityStrategy implements NotificationActionProcessingStrategy {

    @NonNull
    private final ActivityIntentProvider activityIntentProvider;

    protected OpenActivityStrategy() {
        activityIntentProvider = new ActivityIntentProvider();
    }

    protected void openActionOrDefaultActivity(@NonNull Context context,
                                               @NonNull NotificationActionInfo actionInfo) {
        Intent intent = activityIntentProvider.getIntentForActionOrDefaultLaunch(context, actionInfo.targetActionUri);
        if (intent != null) {
            try {
                intent.putExtra(AppMetricaPush.EXTRA_PAYLOAD, actionInfo.payload);
                if (actionInfo.extraBundle != null) {
                    intent.putExtras(actionInfo.extraBundle);
                }
                if (actionInfo.explicitIntent) {
                    intent.setPackage(context.getPackageName());
                }
                context.startActivity(intent);
            } catch (Exception e) {
                PublicLogger.INSTANCE.error(
                    e,
                    "Smth wrong when starting activity for push message with pushId=%s",
                    actionInfo.pushId
                );
                TrackersHub.getInstance().reportError("Error starting activity", e);
            }
        } else {
            PublicLogger.INSTANCE.warning("Intent action for pushId = %s is null", actionInfo.pushId);
            TrackersHub.getInstance().reportError("Open action intent is null", null);
        }
    }
}

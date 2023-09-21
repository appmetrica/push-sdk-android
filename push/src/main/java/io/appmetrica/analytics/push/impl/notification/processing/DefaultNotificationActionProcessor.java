package io.appmetrica.analytics.push.impl.notification.processing;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.intent.NotificationActionType;
import java.util.HashMap;
import java.util.Map;

public class DefaultNotificationActionProcessor implements NotificationActionProcessor {

    @NonNull
    private final Map<NotificationActionType, NotificationActionProcessingStrategy> strategies;

    public DefaultNotificationActionProcessor() {
        strategies = new HashMap<NotificationActionType, NotificationActionProcessingStrategy>();
    }

    @Override
    public void processAction(@NonNull final Context context,
                              @NonNull final Intent intent) {
        final NotificationActionInfo actionInfo = intent.getParcelableExtra(AppMetricaPush.EXTRA_ACTION_INFO);
        if (actionInfo != null) {
            final NotificationActionProcessingStrategy strategy = strategies.get(actionInfo.actionType);
            if (strategy != null) {
                strategy.doAction(context, intent);
            } else {
                TrackersHub.getInstance().reportEvent("No strategy", new HashMap<String, Object>() {{
                    put("actionType", actionInfo.actionType);
                    put("pushId", actionInfo.pushId);
                }});
            }
        } else {
            TrackersHub.getInstance().reportEvent("No action info for DefaultNotificationActionProcessor");
        }
    }

    @Override
    public void setClearNotificationProcessingStrategy(@NonNull final NotificationActionProcessingStrategy strategy) {
        strategies.put(NotificationActionType.CLEAR, strategy);
    }

    @Override
    public void setOpenActionProcessingStrategy(@NonNull final NotificationActionProcessingStrategy strategy) {
        strategies.put(NotificationActionType.CLICK, strategy);
    }

    @Override
    public void setAdditionalActionProcessingStrategy(@NonNull final NotificationActionProcessingStrategy strategy) {
        strategies.put(NotificationActionType.ADDITIONAL_ACTION, strategy);
    }

    @Override
    public void setInlineActionProcessingStrategy(@NonNull final NotificationActionProcessingStrategy strategy) {
        strategies.put(NotificationActionType.INLINE_ACTION, strategy);
    }
}

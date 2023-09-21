package io.appmetrica.analytics.push.impl.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import java.util.HashMap;

public class CommandReporter {

    public static final long EXTRA_COMMAND_RECEIVED_TIME_DEFAULT_VALUE = -1;

    public static void reportCommandTimeDifference(
        @Nullable final String action,
        final long receivedTime,
        @Nullable final String pushId,
        @NonNull final String serviceType
    ) {
        final HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("action", action);
        attributes.put("service_type", serviceType);
        if (receivedTime != EXTRA_COMMAND_RECEIVED_TIME_DEFAULT_VALUE) {
            attributes.put("duration", System.currentTimeMillis() - receivedTime);
        }
        if (pushId != null) {
            attributes.put("push_id", pushId);
        }
        TrackersHub.getInstance().reportEvent("ActionDuration", attributes);
    }
}

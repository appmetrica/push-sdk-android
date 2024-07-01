package io.appmetrica.analytics.push.impl.processing.transform.filter;

import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.settings.PushFilter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

class SinglePushPerPeriodFilter implements PushFilter {

    private static final String TAG = "[SinglePushPerPeriodFilter]";

    private static final String SINGLE_PUSH_PER_PERIOD = "Already have shown push in this period";

    @NonNull
    private final PushMessageHistory pushMessageHistory;

    public SinglePushPerPeriodFilter(@NonNull PushMessageHistory pushMessageHistory) {
        this.pushMessageHistory = pushMessageHistory;
    }

    @NonNull
    @Override
    public FilterResult filter(@NonNull PushMessage pushMessage) {
        Filters filters = pushMessage.getFilters();
        Integer onePushPerPeriodMinutes = filters == null ? null : filters.getOnePushPerPeriodMinutes();
        if (onePushPerPeriodMinutes == null) {
            return FilterResult.show();
        }

        PushNotification pushNotification = pushMessage.getNotification();
        String channelId = pushNotification == null ? null : pushNotification.getChannelId();

        long previousMillis = pushMessageHistory.getLastShownTimeForChannelId(channelId);
        long now = System.currentTimeMillis();
        if (previousMillis > now) {
            // sanity check: user might have move time into future on his device and then back.
            PublicLogger.INSTANCE.warning("%s Last push was shown in future", TAG);
            return FilterResult.show();
        }

        long minDiff = TimeUnit.MINUTES.toMillis(onePushPerPeriodMinutes);
        long diff = now - previousMillis;

        if (diff < minDiff) {
            return FilterResult.silence(
                SINGLE_PUSH_PER_PERIOD,
                String.format(
                    Locale.US,
                    "Previous push was shown [%d] minutes ago, min period is [%d]",
                    TimeUnit.MILLISECONDS.toMinutes(diff),
                    onePushPerPeriodMinutes
                )
            );
        }

        return FilterResult.show();
    }
}

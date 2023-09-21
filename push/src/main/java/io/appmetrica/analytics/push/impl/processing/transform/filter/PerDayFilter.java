package io.appmetrica.analytics.push.impl.processing.transform.filter;

import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import io.appmetrica.analytics.push.settings.PushFilter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

class PerDayFilter implements PushFilter {

    private static final String MAX_PER_DAY_REACHED = "Reached max per day pushes for current topic";

    @NonNull
    private final PushMessageHistory pushMessageHistory;

    public PerDayFilter(@NonNull PushMessageHistory pushMessageHistory) {
        this.pushMessageHistory = pushMessageHistory;
    }

    @NonNull
    @Override
    public FilterResult filter(@NonNull PushMessage pushMessage) {
        Filters filters = pushMessage.getFilters();
        Integer maxPerDay = filters == null ? null : filters.getMaxPushPerDay();
        if (maxPerDay == null) {
            return FilterResult.show();
        }

        PushNotification pushNotification = pushMessage.getNotification();
        String channelId = pushNotification == null ? null : pushNotification.getChannelId();

        List<Long> shownTimes = pushMessageHistory.getShownTimesForChannelId(channelId);

        int shownToday = 0;
        Calendar previous = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        for (Long shownTime : shownTimes) {
            previous.setTimeInMillis(shownTime);
            if (isSameDay(previous, today)) {
                shownToday++;
                if (shownToday >= maxPerDay) {
                    return FilterResult.silence(
                        MAX_PER_DAY_REACHED,
                        String.format(
                            Locale.US,
                            "Was shown [%d], max allowed [%d]",
                            shownToday,
                            maxPerDay
                        )
                    );
                }
            }
        }

        return FilterResult.show();
    }

    private static boolean isSameDay(@NonNull Calendar cal1, @NonNull Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}

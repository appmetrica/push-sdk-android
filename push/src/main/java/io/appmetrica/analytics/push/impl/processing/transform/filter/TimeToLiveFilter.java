package io.appmetrica.analytics.push.impl.processing.transform.filter;

import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeToLiveFilter implements PushFilter {

    private static final String TIME_TO_LIVE_IS_UP = "Time to live is up";

    @NonNull
    @Override
    public FilterResult filter(@NonNull PushMessage pushMessage) {
        Long timeToShow = pushMessage.getTimeToShowMillis();
        Long timeToHide =
            pushMessage.getNotification() == null ? null : pushMessage.getNotification().getTimeToHideMillis();

        long curTimeMillis = System.currentTimeMillis();
        if (timeToShow != null && timeToShow < curTimeMillis) {
            return FilterResult.silence(TIME_TO_LIVE_IS_UP, String.format(Locale.US,
                "Cur time: %s. Time to show: %s", timeFormat(curTimeMillis), timeFormat(timeToShow)
            ));
        }
        if (timeToHide != null && timeToHide < curTimeMillis) {
            return FilterResult.silence(TIME_TO_LIVE_IS_UP, String.format(Locale.US,
                "Cur time: %s. Time to hide: %s", timeFormat(curTimeMillis), timeFormat(timeToHide)
            ));
        }

        return FilterResult.show();
    }

    @NonNull
    private String timeFormat(long timeMillis) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date(timeMillis));
    }
}

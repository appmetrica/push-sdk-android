package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.os.Build;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import java.util.Locale;

public class AndroidApiLevelFilter implements PushFilter {

    private static final String WRONG_ANDROID_OS_VERSION = "Wrong android os version";

    @NonNull
    @Override
    public FilterResult filter(@NonNull PushMessage pushMessage) {
        Filters filters = pushMessage.getFilters();
        if (filters == null) {
            return FilterResult.show();
        }

        int androidApiLevel = Build.VERSION.SDK_INT;
        Integer minAndroidApiLevel = filters.getMinAndroidApiLevel();
        Integer maxAndroidApiLevel = filters.getMaxAndroidApiLevel();
        if (minAndroidApiLevel != null && androidApiLevel < minAndroidApiLevel ||
            maxAndroidApiLevel != null && androidApiLevel > maxAndroidApiLevel) {
            return FilterResult.silence(
                WRONG_ANDROID_OS_VERSION,
                String.format(
                    Locale.US,
                    "Got android os level [%d], allowed min [%d], allowed max [%d]",
                    androidApiLevel, minAndroidApiLevel, maxAndroidApiLevel
                )
            );
        }

        return FilterResult.show();
    }
}

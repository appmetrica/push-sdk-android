package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import java.util.List;

class ContentIdFilter implements PushFilter {

    private static final String DUPLICATE_CONTENT_ID = "Duplicate contentId";

    @NonNull
    private final PushMessageHistory pushMessageHistory;

    public ContentIdFilter(@NonNull PushMessageHistory pushMessageHistory) {
        this.pushMessageHistory = pushMessageHistory;
    }

    @NonNull
    @Override
    public FilterResult filter(@NonNull PushMessage pushMessage) {
        Filters filters = pushMessage.getFilters();
        String contentId = filters == null ? null : filters.getContentId();
        if (TextUtils.isEmpty(contentId)) {
            return FilterResult.show();
        }

        List<String> oldPushIds = pushMessageHistory.getContentIds();
        if (oldPushIds.contains(contentId)) {
            return FilterResult.silence(
                DUPLICATE_CONTENT_ID,
                String.format("Push with the same content id [%s] has already been received", contentId)
            );
        }
        return FilterResult.show();
    }
}

package io.appmetrica.analytics.push.impl.processing.transform.filter;

import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import io.appmetrica.analytics.push.settings.PushFilteredCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PushFilterController implements PushFilter {

    @NonNull
    private final List<PushFilter> pushFilters;
    @NonNull
    private final List<PushFilteredCallback> pushFilteredCallbacks;

    public PushFilterController() {
        pushFilters = new CopyOnWriteArrayList<PushFilter>();
        pushFilteredCallbacks = new ArrayList<PushFilteredCallback>();
    }

    @NonNull
    @Override
    public FilterResult filter(@NonNull PushMessage pushMessage) {
        for (PushFilter pushFilter : pushFilters) {
            FilterResult result = pushFilter.filter(pushMessage);
            if (result.filterResultCode == FilterResultCode.SILENCE) {
                return processAndReturnResult(pushMessage, result);
            }
        }
        return processAndReturnResult(pushMessage, FilterResult.show());
    }

    public void addPushFilter(@NonNull PushFilter pushFilter) {
        pushFilters.add(pushFilter);
    }

    public void addPushCallback(@NonNull PushFilteredCallback pushFilteredCallback) {
        pushFilteredCallbacks.add(pushFilteredCallback);
    }

    @NonNull
    private FilterResult processAndReturnResult(@NonNull PushMessage pushMessage,
                                                @NonNull FilterResult filterResult) {
        for (final PushFilteredCallback pushFilteredCallback : pushFilteredCallbacks) {
            pushFilteredCallback.onPushFiltered(pushMessage, filterResult);
        }
        return filterResult;
    }
}

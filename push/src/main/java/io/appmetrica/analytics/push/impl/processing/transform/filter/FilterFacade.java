package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import io.appmetrica.analytics.push.settings.PushFilteredCallback;

public class FilterFacade implements PushFilter {

    @NonNull
    private final PushFilterController pushFilterController;

    public FilterFacade() {
        this(new PushFilterController());
    }

    protected PushFilter[] getRequiredFilters(
        @NonNull Context context,
        @NonNull AppMetricaPushCore appMetricaPushCore
    ) {
        return new PushFilter[]{
            new VersionCodeFilter(context),
            new AndroidApiLevelFilter(),
            new PerDayFilter(appMetricaPushCore.getPushMessageHistory()),
            new SinglePushPerPeriodFilter(appMetricaPushCore.getPushMessageHistory()),
            new PassportUidFilter(appMetricaPushCore),
            new LocationFilter(),
            new PushIdFilter(appMetricaPushCore.getPushMessageHistory()),
            new ContentIdFilter(appMetricaPushCore.getPushMessageHistory()),
            new LoginFilter(appMetricaPushCore),
            new TimeToLiveFilter()
        };
    }

    @NonNull
    @Override
    public FilterResult filter(@NonNull PushMessage pushMessage) {
        return pushFilterController.filter(pushMessage);
    }

    public void addPushFilter(@NonNull PushFilter pushFilter) {
        pushFilterController.addPushFilter(pushFilter);
    }

    public void addPushCallback(@NonNull PushFilteredCallback pushFilteredCallback) {
        pushFilterController.addPushCallback(pushFilteredCallback);
    }

    public void addPushFilters(@NonNull PushFilter... filters) {
        for (PushFilter filter : filters) {
            addPushFilter(filter);
        }
    }

    @VisibleForTesting
    FilterFacade(@NonNull PushFilterController pushFilterController) {
        this.pushFilterController = pushFilterController;
    }
}

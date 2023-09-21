package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;

public class PushFilterFacade extends FilterFacade {

    public PushFilterFacade(@NonNull Context context, @NonNull AppMetricaPushCore appMetricaPushCore) {
        this(context, appMetricaPushCore, new PushFilterController());
    }

    @VisibleForTesting
    PushFilterFacade(
        @NonNull Context context,
        @NonNull AppMetricaPushCore appMetricaPushCore,
        @NonNull PushFilterController pushFilterController
    ) {
        super(pushFilterController);
        addPushFilters(getRequiredFilters(context, appMetricaPushCore));
        addPushFilters(
            new NotificationStatusFilter(context)
        );
    }
}

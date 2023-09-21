package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;

public class PreLazyFilterFacade extends FilterFacade {

    public PreLazyFilterFacade(@NonNull Context context, @NonNull AppMetricaPushCore appMetricaPushCore) {
        this(context, appMetricaPushCore, new PushFilterController());
    }

    @VisibleForTesting
    PreLazyFilterFacade(
        @NonNull Context context,
        @NonNull AppMetricaPushCore appMetricaPushCore,
        @NonNull PushFilterController pushFilterController
    ) {
        super(pushFilterController);
        addPushFilters(getRequiredFilters(context, appMetricaPushCore));
    }
}

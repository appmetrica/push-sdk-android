package io.appmetrica.analytics.push.impl.processing.transform;

import android.content.Context;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.processing.transform.filter.PushFilterFacade;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;

public class FilterTransformProcessor extends TransformProcessor {

    @NonNull
    private final Context context;

    public FilterTransformProcessor(@NonNull Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TransformResult transform(@NonNull PushMessage pushMessage) {
        PushFilterFacade facade = AppMetricaPushCore.getInstance(context).getPushFilterFacade();
        PushFilter.FilterResult result = facade.filter(pushMessage);
        if (result.isShow()) {
            return success(pushMessage);
        } else {
            return failure(pushMessage, result.category, result.details);
        }
    }
}

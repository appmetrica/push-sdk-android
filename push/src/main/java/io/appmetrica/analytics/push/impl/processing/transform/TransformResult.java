package io.appmetrica.analytics.push.impl.processing.transform;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;

public class TransformResult {

    @NonNull
    public final PushFilter.FilterResult filterResult;
    @NonNull
    public final PushMessage pushMessage;

    private TransformResult(@NonNull PushFilter.FilterResult result, @NonNull PushMessage pushMessage) {
        this.filterResult = result;
        this.pushMessage = pushMessage;
    }

    @NonNull
    static TransformResult success(@NonNull PushMessage pushMessage) {
        return new TransformResult(PushFilter.FilterResult.show(), pushMessage);
    }

    @NonNull
    static TransformResult failure(
        @NonNull PushMessage pushMessage,
        @Nullable String category,
        @Nullable String details
    ) {
        return new TransformResult(PushFilter.FilterResult.silence(category, details), pushMessage);
    }

    public boolean isSuccess() {
        return filterResult.isShow();
    }
}

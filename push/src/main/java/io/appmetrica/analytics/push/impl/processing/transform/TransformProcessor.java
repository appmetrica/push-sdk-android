package io.appmetrica.analytics.push.impl.processing.transform;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.model.PushMessage;

public abstract class TransformProcessor {

    @NonNull
    public abstract TransformResult transform(@NonNull PushMessage pushMessage);

    @NonNull
    protected TransformResult success(@NonNull PushMessage pushMessage) {
        return TransformResult.success(pushMessage);
    }

    @NonNull
    protected TransformResult failure(
        @NonNull PushMessage pushMessage,
        @Nullable String category,
        @Nullable String details
    ) {
        return TransformResult.failure(pushMessage, category, details);
    }
}

package io.appmetrica.analytics.push.impl.processing.transform;

import androidx.annotation.Nullable;

public class TransformFailureException extends RuntimeException {

    @Nullable
    private final String category;
    @Nullable
    private final String details;

    public TransformFailureException(@Nullable String category, @Nullable String details) {
        super(String.format("Transform failure for category '%s' with details '%s'", category, details));
        this.category = category;
        this.details = details;
    }

    @Nullable
    public String getCategory() {
        return category;
    }

    @Nullable
    public String getDetails() {
        return details;
    }
}

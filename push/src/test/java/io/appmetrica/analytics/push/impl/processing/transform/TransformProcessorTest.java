package io.appmetrica.analytics.push.impl.processing.transform;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class TransformProcessorTest {

    protected TransformProcessor transformProcessor;

    public void setUp(@NonNull TransformProcessor transformProcessor) {
        this.transformProcessor = transformProcessor;
    }

    protected void assertSuccess(@NonNull PushMessage pushMessage) {
        assertSuccess(pushMessage, pushMessage);
    }

    protected void assertSuccess(@NonNull PushMessage originalPushMessage, @NonNull PushMessage newPushMessage) {
        TransformResult result = transformProcessor.transform(originalPushMessage);
        assertThat(result.pushMessage).isEqualTo(newPushMessage);
        assertThat(result.filterResult).isEqualTo(PushFilter.FilterResult.show());
        assertThat(result.isSuccess()).isTrue();
    }

    protected void assertFailure(
        @NonNull PushMessage pushMessage,
        @Nullable String category,
        @Nullable String details
    ) {
        assertFailure(pushMessage, pushMessage, category, details);
    }

    protected void assertFailure(
        @NonNull PushMessage originalPushMessage,
        @NonNull PushMessage newPushMessage,
        @Nullable String category,
        @Nullable String details
    ) {
        TransformResult result = transformProcessor.transform(originalPushMessage);
        assertThat(result.pushMessage).isEqualTo(newPushMessage);
        assertThat(result.filterResult.filterResultCode).isEqualTo(PushFilter.FilterResultCode.SILENCE);
        assertThat(result.filterResult.category).isEqualTo(category);
        assertThat(result.filterResult.details).isEqualTo(details);
        assertThat(result.isSuccess()).isFalse();
    }
}

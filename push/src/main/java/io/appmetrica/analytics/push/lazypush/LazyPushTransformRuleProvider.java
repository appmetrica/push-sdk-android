package io.appmetrica.analytics.push.lazypush;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.model.PushMessage;

/**
 * Provides {@link LazyPushTransformRule} using {@link PushMessage}.
 */
public interface LazyPushTransformRuleProvider {

    /**
     * Provides {@link LazyPushTransformRule} using {@link PushMessage}.
     *
     * @param pushMessage received {@link PushMessage}
     * @return {@link LazyPushTransformRule} object or null if it is not necessary
     */
    @Nullable
    LazyPushTransformRule getRule(
        @NonNull final PushMessage pushMessage
    );
}

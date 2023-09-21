package io.appmetrica.analytics.push.impl.processing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.processing.strategy.PushStrategy;
import io.appmetrica.analytics.push.model.PushMessage;

public interface PushProcessingStrategyProvider {

    @Nullable
    PushStrategy getPushStrategy(@NonNull final PushMessage pushMessage);
}

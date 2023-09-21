package io.appmetrica.analytics.push.impl.processing.strategy;

import android.content.Context;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.model.PushMessage;

public interface PushStrategy {

    void processPush(@NonNull final Context context, @NonNull final PushMessage pushMessage);
}

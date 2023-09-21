package io.appmetrica.analytics.push.impl.processing;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface PushProcessor {

    void processPush(@NonNull final Context context, @Nullable final Bundle bundle);
}

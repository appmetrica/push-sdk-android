package io.appmetrica.analytics.push.impl.command;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.processing.PushProcessor;

class ProcessPushCommand implements Command {

    @Override
    public void execute(@NonNull final Context context,
                        @NonNull final Bundle bundle) {
        final PushProcessor pushProcessor =
            AppMetricaPushCore.getInstance(context).getPushServiceProvider().getPushProcessor();
        pushProcessor.processPush(context, bundle);
    }
}

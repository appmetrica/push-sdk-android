package io.appmetrica.analytics.push.impl.command;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PushServiceControllerComposite;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;

class InitPushServiceCommand implements Command {

    @Override
    public void execute(@NonNull final Context context,
                        @NonNull final Bundle bundle) {
        PushServiceControllerComposite pushServiceController =
            AppMetricaPushCore.getInstance(context).getPushServiceController();
        if (pushServiceController != null) {
            pushServiceController.register();
        } else {
            PublicLogger.INSTANCE.info("PushServiceController is null");
        }
    }
}

package io.appmetrica.analytics.push.impl.command;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PushServiceControllerComposite;
import io.appmetrica.analytics.push.impl.token.TokenManager;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;

class InitPushServiceCommand implements Command {

    @Override
    public void execute(@NonNull final Context context,
                        @NonNull final Bundle bundle) {
        AppMetricaPushCore appMetricaPushCore = AppMetricaPushCore.getInstance(context);
        PushServiceControllerComposite pushServiceController = appMetricaPushCore.getPushServiceController();
        if (pushServiceController != null) {
            pushServiceController.register();
        } else {
            PublicLogger.INSTANCE.info("PushServiceController is null");
        }
        PushServiceFacade.sendSystemInfo(context, null);
        TokenManager tokenManager = appMetricaPushCore.getTokenManager();
        if (tokenManager != null) {
            tokenManager.init();
        } else {
            PublicLogger.INSTANCE.info("TokenManager is null");
        }
    }
}

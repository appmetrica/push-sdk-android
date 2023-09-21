package io.appmetrica.analytics.push.impl.command;

import android.content.Context;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.coreutils.internal.RefreshTokenInfo;
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.utils.function.Consumer;
import java.util.Map;

class UpdatePushTokenCommand extends RefreshPushTokenCommand {

    @Override
    protected void reportToken(@NonNull final Context context,
                               @NonNull Map<String, String> tokens,
                               @NonNull final RefreshTokenInfo info) {
        sendTokenIfNeeded(context, info.force, tokens, new Consumer<Map<String, String>>() {
            @Override
            public void accept(Map<String, String> tokens) {
                PublicLogger.i("Will send tokens %s to server!", tokens);
                AppMetricaPushCore.getInstance(context).onTokenUpdated(tokens, info.notificationStatusChangedTime);
            }
        });
    }
}

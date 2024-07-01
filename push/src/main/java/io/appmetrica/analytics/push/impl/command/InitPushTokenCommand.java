package io.appmetrica.analytics.push.impl.command;

import android.content.Context;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.coreutils.internal.RefreshTokenInfo;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.utils.function.Consumer;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import java.util.Map;

class InitPushTokenCommand extends RefreshPushTokenCommand {

    @Override
    protected void reportToken(@NonNull final Context context,
                               @NonNull final Map<String, String> tokens,
                               @NonNull final RefreshTokenInfo info) {
        sendTokenIfNeeded(context, info.force, tokens, new Consumer<Map<String, String>>() {
            @Override
            public void accept(Map<String, String> tokens) {
                PublicLogger.INSTANCE.info("Will send tokens %s to server!", tokens.toString());
                AppMetricaPushCore.getInstance(context).onFirstTokenReceived(tokens);
            }
        });
    }
}

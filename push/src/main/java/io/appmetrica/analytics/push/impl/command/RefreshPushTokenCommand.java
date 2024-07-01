package io.appmetrica.analytics.push.impl.command;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.RefreshTokenInfo;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PreferenceManager;
import io.appmetrica.analytics.push.impl.storage.Token;
import io.appmetrica.analytics.push.impl.utils.function.Consumer;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import java.util.Map;
import java.util.concurrent.TimeUnit;

abstract class RefreshPushTokenCommand implements Command {

    private static final String TAG = "[RefreshPushTokenCommand]";

    @Override
    public void execute(@NonNull final Context context,
                        @NonNull final Bundle bundle) {
        DebugLogger.INSTANCE.info(TAG, "Trying to get tokens");

        RefreshTokenInfo info =
            RefreshTokenInfo.fromBundle(bundle.getBundle(PushServiceFacade.REFRESH_TOKEN_INFO));
        if (AppMetricaPushCore.getInstance(context).isInitialized()) {
            reportToken(context, AppMetricaPushCore.getInstance(context).getPushServiceController().getTokens(), info);
        }
    }

    protected abstract void reportToken(@NonNull final Context context,
                                        @NonNull Map<String, String> tokens,
                                        @NonNull final RefreshTokenInfo info);

    protected void sendTokenIfNeeded(@NonNull final Context context,
                                     final boolean force,
                                     @NonNull Map<String, String> newTokens,
                                     @NonNull final Consumer<Map<String, String>> tokenSender) {
        final PreferenceManager manager = AppMetricaPushCore.getInstance(context).getPreferenceManager();
        Map<String, Token> actualTokens = Token.parseTokens(manager.getTokens());
        final long currentTime = System.currentTimeMillis();
        if (force || actualTokens == null || shouldUpdateTokens(newTokens, actualTokens, currentTime)) {
            manager.saveTokens(Token.saveToString(newTokens, currentTime));
            tokenSender.accept(newTokens);
            PublicLogger.INSTANCE.info("New tokens were saved to PreferenceManager and sent:");
            for (Map.Entry<String, String> entry : newTokens.entrySet()) {
                PublicLogger.INSTANCE.info("token from %s is %s", entry.getKey(), entry.getValue());
            }
        } else {
            PublicLogger.INSTANCE.info("Received old tokens");
        }
    }

    @VisibleForTesting
    static boolean shouldUpdateTokens(@NonNull final Map<String, String> newTokens,
                                      @NonNull final Map<String, Token> actualTokens,
                                      final long currentTime) {
        if (newTokens.size() != actualTokens.size()) {
            return true;
        }
        for (Map.Entry<String, Token> token : actualTokens.entrySet()) {
            if (!newTokens.containsKey(token.getKey())) {
                return true;
            }
            if (!TextUtils.equals(token.getValue().token, newTokens.get(token.getKey()))) {
                return true;
            }
            if (currentTime - token.getValue().lastUpdateTime > TimeUnit.DAYS.toMillis(1)) {
                return true;
            }
        }
        return false;
    }
}

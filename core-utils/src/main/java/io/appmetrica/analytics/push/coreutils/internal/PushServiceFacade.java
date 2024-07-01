package io.appmetrica.analytics.push.coreutils.internal;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.coreutils.internal.model.TransportPushMessage;
import io.appmetrica.analytics.push.coreutils.internal.service.PushServiceControllerProvider;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;

public class PushServiceFacade {

    private static final String TAG = "[PushServiceFacade]";

    public static final String EXTRA_COMMAND = "io.appmetrica.analytics.push.extra.COMMAND";
    public static final String EXTRA_COMMAND_RECEIVED_TIME =
        "io.appmetrica.analytics.push.extra.EXTRA_COMMAND_RECEIVED_TIME";

    public static final String COMMAND_INIT_PUSH_TOKEN = "io.appmetrica.analytics.push.command.INIT_PUSH_TOKEN";
    public static final String COMMAND_UPDATE_TOKEN = "io.appmetrica.analytics.push.command.REFRESH_TOKEN";
    public static final String COMMAND_PROCESS_PUSH = "io.appmetrica.analytics.push.command.PROCESS_PUSH";
    public static final String COMMAND_INIT_PUSH_SERVICE = "io.appmetrica.analytics.push.command.INIT_PUSH_SERVICE";

    public static final String COMMAND_SEND_PUSH_TOKEN_ON_REFRESH =
        "io.appmetrica.analytics.push.command.SEND_PUSH_TOKEN_ON_REFRESH";

    public static final String COMMAND_SEND_PUSH_TOKEN_MANUALLY =
        "io.appmetrica.analytics.push.command.SEND_PUSH_TOKEN_MANUALLY";

    public static final String REFRESH_TOKEN_INFO = "io.appmetrica.analytics.push.REFRESH_TOKEN_INFO";
    public static final String TOKEN = "io.appmetrica.analytics.push.TOKEN";

    @NonNull
    private static CommandServiceWrapper commandServiceWrapper = new CommandServiceWrapper();

    public static void initPushService(@NonNull final Context context) {
        DebugLogger.INSTANCE.info(TAG, "Init push service");
        final Bundle bundle = new Bundle();
        bundle.putAll(createBundleWithCommand(COMMAND_INIT_PUSH_SERVICE));
        commandServiceWrapper.startCommand(context, bundle);
    }

    public static void initToken(@NonNull final Context context) {
        initToken(context, false);
    }

    public static void initToken(@NonNull final Context context, final boolean force) {
        DebugLogger.INSTANCE.info(TAG, "Init push token");
        final Bundle bundle = new Bundle();
        bundle.putAll(createBundleWithCommand(COMMAND_INIT_PUSH_TOKEN));
        bundle.putBundle(REFRESH_TOKEN_INFO, new RefreshTokenInfo(force).toBundle());
        commandServiceWrapper.startCommand(context, bundle);
    }

    public static void sendTokenOnRefresh(@NonNull Context context, @NonNull String token, @NonNull String transport) {
        sendTokenWithCommand(context, COMMAND_SEND_PUSH_TOKEN_ON_REFRESH, token, transport);
    }

    public static void sendTokenManually(@NonNull Context context, @NonNull String token, @NonNull String transport) {
        sendTokenWithCommand(context, COMMAND_SEND_PUSH_TOKEN_MANUALLY, token, transport);
    }

    private static void sendTokenWithCommand(
        @NonNull Context context,
        @NonNull String command,
        @NonNull String token,
        @NonNull String transport
    ) {
        PublicLogger.INSTANCE.info(TAG, "Send token with command: `%s` from transport: %s", command, transport);
        final Bundle bundle = createBundleWithCommand(command);
        bundle.putString(CoreConstants.EXTRA_TRANSPORT, transport);
        bundle.putString(TOKEN, token);
        commandServiceWrapper.startCommand(context, bundle, /* needService */ false);
    }

    public static void refreshToken(@NonNull final Context context) {
        refreshToken(context, false);
    }

    public static void refreshToken(@NonNull final Context context, final boolean force) {
        refreshToken(context, new RefreshTokenInfo(force));
    }

    public static void refreshToken(@NonNull final Context context, @NonNull final RefreshTokenInfo info) {
        DebugLogger.INSTANCE.info(TAG, "Refresh push token");
        final Bundle bundle = createBundleWithCommand(COMMAND_UPDATE_TOKEN);
        bundle.putBundle(REFRESH_TOKEN_INFO, info.toBundle());
        commandServiceWrapper.startCommand(context, bundle);
    }

    public static void processPush(@NonNull final Context context,
                                   @NonNull final Bundle data,
                                   @NonNull String transport
    ) {
        DebugLogger.INSTANCE.info(TAG, "Process push");
        final Bundle bundle = new Bundle();
        bundle.putAll(createBundleWithCommand(COMMAND_PROCESS_PUSH));
        bundle.putAll(data);
        bundle.putString(CoreConstants.EXTRA_TRANSPORT, transport);

        final TransportPushMessage transportPushMessage = new TransportPushMessage(bundle);
        if (transportPushMessage.getProcessingMinTime() != null) {
            bundle.putLong(
                CoreConstants.MIN_PROCESSING_DELAY,
                transportPushMessage.getProcessingMinTime()
            );
        }
        if (transportPushMessage.isOwnPush()) {
            commandServiceWrapper.startCommand(context, bundle, needService(transportPushMessage));
        }
    }

    public static class CommandServiceWrapper {

        @Nullable
        private PushServiceControllerProvider pushServiceControllerProvider;

        public void startCommand(@NonNull final Context context, @NonNull final Bundle bundle) {
            startCommand(context, bundle, true);
        }

        public void startCommand(
            @NonNull final Context context,
            @NonNull final Bundle bundle,
            final boolean needService
        ) {
            try {
                getPushServiceControllerProvider(context)
                    .getPushServiceCommandLauncher(needService)
                    .launchService(bundle);
            } catch (Throwable th) {
                final String msg = "Start failed";
                DebugLogger.INSTANCE.error(TAG, th, msg);
                TrackersHub.getInstance().reportError(msg, th);
            }
        }

        @NonNull
        private synchronized PushServiceControllerProvider getPushServiceControllerProvider(
            @NonNull final Context context
        ) {
            if (pushServiceControllerProvider == null) {
                pushServiceControllerProvider = new PushServiceControllerProvider(context);
            }
            return pushServiceControllerProvider;
        }

        @VisibleForTesting
        void setPushServiceControllerProvider(@NonNull final PushServiceControllerProvider provider) {
            pushServiceControllerProvider = provider;
        }
    }

    @NonNull
    public static Bundle createBundleWithCommand(@NonNull final String command) {
        final Bundle bundle = new Bundle();
        bundle.putString(EXTRA_COMMAND, command);
        bundle.putLong(EXTRA_COMMAND_RECEIVED_TIME, System.currentTimeMillis());
        return bundle;
    }

    @VisibleForTesting
    public static void setJobIntentServiceWrapper(@NonNull final CommandServiceWrapper commandServiceWrapper) {
        PushServiceFacade.commandServiceWrapper = commandServiceWrapper;
    }

    private static boolean needService(@NonNull final TransportPushMessage transportPushMessage) {
        switch (transportPushMessage.getServiceType()) {
            case PROVIDER_SERVICE:
                return false;
            case APPMETRICA_PUSH_SERVICE:
            case UNKNOWN:
            default:
                return true;
        }
    }
}

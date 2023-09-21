package io.appmetrica.analytics.push.coreutils.internal;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.coreutils.internal.model.TransportPushMessage;
import io.appmetrica.analytics.push.coreutils.internal.service.PushServiceControllerProvider;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;

public class PushServiceFacade {

    public static final String EXTRA_COMMAND = "io.appmetrica.analytics.push.extra.COMMAND";
    public static final String EXTRA_COMMAND_RECEIVED_TIME =
        "io.appmetrica.analytics.push.extra.EXTRA_COMMAND_RECEIVED_TIME";

    public static final String COMMAND_INIT_PUSH_TOKEN = "io.appmetrica.analytics.push.command.INIT_PUSH_TOKEN";
    public static final String COMMAND_UPDATE_TOKEN = "io.appmetrica.analytics.push.command.REFRESH_TOKEN";
    public static final String COMMAND_PROCESS_PUSH = "io.appmetrica.analytics.push.command.PROCESS_PUSH";
    public static final String COMMAND_INIT_PUSH_SERVICE = "io.appmetrica.analytics.push.command.INIT_PUSH_SERVICE";

    public static final String REFRESH_TOKEN_INFO = "io.appmetrica.analytics.push.REFRESH_TOKEN_INFO";

    @NonNull
    private static CommandServiceWrapper commandServiceWrapper = new CommandServiceWrapper();

    public static void initPushService(@NonNull final Context context) {
        PLog.i("Init push service");
        final Bundle bundle = new Bundle();
        bundle.putAll(createBundleWithCommand(COMMAND_INIT_PUSH_SERVICE));
        commandServiceWrapper.startCommand(context, bundle);
    }

    public static void initToken(@NonNull final Context context) {
        initToken(context, false);
    }

    public static void initToken(@NonNull final Context context, final boolean force) {
        PLog.i("Init push token");
        final Bundle bundle = new Bundle();
        bundle.putAll(createBundleWithCommand(COMMAND_INIT_PUSH_TOKEN));
        bundle.putBundle(REFRESH_TOKEN_INFO, new RefreshTokenInfo(force).toBundle());
        commandServiceWrapper.startCommand(context, bundle);
    }

    public static void refreshToken(@NonNull final Context context) {
        refreshToken(context, false);
    }

    public static void refreshToken(@NonNull final Context context, final boolean force) {
        refreshToken(context, new RefreshTokenInfo(force));
    }

    public static void refreshToken(@NonNull final Context context, @NonNull final RefreshTokenInfo info) {
        PLog.i("Refresh push token");
        final Bundle bundle = new Bundle();
        bundle.putAll(createBundleWithCommand(COMMAND_UPDATE_TOKEN));
        bundle.putBundle(REFRESH_TOKEN_INFO, info.toBundle());
        commandServiceWrapper.startCommand(context, bundle);
    }

    public static void processPush(@NonNull final Context context,
                                   @NonNull final Bundle data,
                                   @NonNull String transport
    ) {
        PLog.i("Process push");
        final Bundle bundle = new Bundle();
        bundle.putAll(createBundleWithCommand(COMMAND_PROCESS_PUSH));
        bundle.putAll(data);
        bundle.putString(CoreConstants.EXTRA_TRANSPORT, transport);

        final TransportPushMessage transportPushMessage = new TransportPushMessage(bundle);
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
                PLog.e(th, msg);
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

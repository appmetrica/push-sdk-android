package io.appmetrica.analytics.push.coreutils.internal.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.coreutils.internal.commands.Commands;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;

class PushServiceController implements PushServiceCommandLauncher {

    private static final String TAG = "[PushServiceController]";
    private static final String PUSH_SERVICE = "io.appmetrica.analytics.push.internal.service.PushService";

    private static final String ACTION_SERVICE_START =
        "io.appmetrica.analytics.push.configuration.ACTION_SERVICE_START";

    @NonNull
    private final Context context;

    public PushServiceController(@NonNull final Context context) {
        this.context = context;
    }

    @Override
    public void launchService(@NonNull final Bundle extras) {
        DebugLogger.INSTANCE.info(TAG, "Launch command with extras: %s", extras);
        Intent intent = new Intent()
            .setComponent(new ComponentName(context.getPackageName(), PUSH_SERVICE))
            .setAction(ACTION_SERVICE_START)
            .putExtras(extras);
        try {
            context.startService(intent);
        } catch (Throwable e) {
            DebugLogger.INSTANCE.error(TAG, e, e.getMessage());
            TrackersHub.getInstance().reportError(
                String.format("Launching service for command %s failed", extras.getString(Commands.EXTRA_COMMAND)),
                e
            );
        }
    }
}

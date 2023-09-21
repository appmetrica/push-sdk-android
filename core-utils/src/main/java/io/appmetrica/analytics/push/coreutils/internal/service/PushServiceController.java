package io.appmetrica.analytics.push.coreutils.internal.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;

import static io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade.EXTRA_COMMAND;

class PushServiceController implements PushServiceCommandLauncher {

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
        PLog.d("Launch command with extras: %s", extras);
        Intent intent = new Intent()
            .setComponent(new ComponentName(context.getPackageName(), PUSH_SERVICE))
            .setAction(ACTION_SERVICE_START)
            .putExtras(extras);
        try {
            context.startService(intent);
        } catch (Throwable e) {
            PLog.e(e, e.getMessage());
            TrackersHub.getInstance().reportError(
                String.format("Launching service for command %s failed", extras.getString(EXTRA_COMMAND)),
                e
            );
        }
    }
}

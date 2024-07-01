package io.appmetrica.analytics.push.internal.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.command.Command;
import io.appmetrica.analytics.push.impl.command.CommandHolder;
import io.appmetrica.analytics.push.impl.utils.CommandReporter;
import io.appmetrica.analytics.push.impl.utils.Utils;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade.EXTRA_COMMAND;
import static io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade.EXTRA_COMMAND_RECEIVED_TIME;

public class PushService extends Service {

    private static final String TAG = "[PushService]";

    @NonNull
    private final CommandHolder commandHolder = new CommandHolder();
    @NonNull
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        try {
            final String action = intent.getStringExtra(EXTRA_COMMAND);
            DebugLogger.INSTANCE.info(TAG, "Handle command: %s", action);
            CommandReporter.reportCommandTimeDifference(
                action,
                intent.getLongExtra(EXTRA_COMMAND_RECEIVED_TIME,
                    CommandReporter.EXTRA_COMMAND_RECEIVED_TIME_DEFAULT_VALUE),
                Utils.extractPushId(intent.getExtras()),
                "PushService"
            );
            final Command command = commandHolder.get(action);
            if (command != null) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        command.execute(PushService.this, intent.getExtras());
                    }
                });
            }
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to handle command ", e);
            PublicLogger.INSTANCE.error(e, "An unexpected error occurred while running the AppMetrica Push SDK. " +
                "You can report it via https://appmetrica.io/docs/troubleshooting/other.html");
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

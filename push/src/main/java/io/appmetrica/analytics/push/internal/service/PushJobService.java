package io.appmetrica.analytics.push.internal.service;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Bundle;
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

@TargetApi(Build.VERSION_CODES.O)
public class PushJobService extends JobService {

    private static final String TAG = "[PushJobService]";

    @NonNull
    private final CommandHolder commandHolder = new CommandHolder();
    @NonNull
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public boolean onStartJob(@Nullable final JobParameters params) {
        if (params == null) {
            DebugLogger.INSTANCE.info(TAG, "onStartJob - parameters is null");
            return false;
        }
        try {
            final Bundle extras = params.getTransientExtras();
            final String action = extras.getString(EXTRA_COMMAND);
            DebugLogger.INSTANCE.info(TAG, "Handle command: %s", action);
            CommandReporter.reportCommandTimeDifference(
                action,
                extras.getLong(EXTRA_COMMAND_RECEIVED_TIME,
                    CommandReporter.EXTRA_COMMAND_RECEIVED_TIME_DEFAULT_VALUE),
                Utils.extractPushId(extras),
                "PushJobService"
            );
            final Command command = commandHolder.get(action);
            if (command != null) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        command.execute(PushJobService.this, extras);
                        jobFinished(params, false);
                    }
                });
                return true;
            }
            return false;
        } catch (Throwable e) {
            TrackersHub.getInstance().reportError("Failed to handle command ", e);
            PublicLogger.INSTANCE.error(e, "An unexpected error occurred while running the AppMetrica Push SDK. " +
                "You can report it via https://appmetrica.io/docs/troubleshooting/other.html");
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}

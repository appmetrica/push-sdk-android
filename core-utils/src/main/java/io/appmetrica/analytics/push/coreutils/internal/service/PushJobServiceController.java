package io.appmetrica.analytics.push.coreutils.internal.service;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.coreutils.internal.commands.Commands;
import io.appmetrica.analytics.push.coreutils.internal.utils.ConsumerWithThrowable;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RequiresApi(Build.VERSION_CODES.O)
class PushJobServiceController implements PushServiceCommandLauncher {

    private static final String TAG = "[PushJobServiceController]";

    private static final String PUSH_JOB_SERVICE = "io.appmetrica.analytics.push.internal.service.PushJobService";

    private static final int MIN_SERVICE_START_JOB_ID = 27118000;
    private static final int MAX_SERVICE_START_JOB_ID = 27119000;

    @NonNull
    private final Context context;
    @Nullable
    private final JobScheduler jobScheduler;
    private AtomicInteger nextServiceStartJobId = new AtomicInteger(MIN_SERVICE_START_JOB_ID);

    public PushJobServiceController(@NonNull final Context context) {
        this(
            context,
            (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE)
        );
    }

    @VisibleForTesting
    PushJobServiceController(@NonNull final Context context,
                             @Nullable final JobScheduler jobScheduler) {
        this.context = context;
        this.jobScheduler = jobScheduler;
    }

    @Override
    public void launchService(@NonNull final Bundle extras) {
        DebugLogger.INSTANCE.info(TAG, "Launch service with extras: %s", extras);
        final int jobId = getNextJobId();
        final JobInfo jobInfo = new JobInfo.Builder(
            jobId,
            new ComponentName(context.getPackageName(), PUSH_JOB_SERVICE)
        )
            .setTransientExtras(extras)
            .setOverrideDeadline(10)
            .build();

        CoreUtils.accessSystemServiceSafely(new ConsumerWithThrowable<JobScheduler>() {
            @Override
            public void consume(@NonNull JobScheduler input) throws Throwable {
                final int status = input.schedule(jobInfo);
                if (status != JobScheduler.RESULT_SUCCESS) {
                    DebugLogger.INSTANCE.warning(
                        TAG,
                        "Scheduling job %s failed with status %d",
                        extras.getString(Commands.EXTRA_COMMAND),
                        status
                    );
                    TrackersHub.getInstance().reportEvent("Scheduling job failed", new HashMap<String, Object>() {{
                        put("status", status);
                        put("command", extras.getString(Commands.EXTRA_COMMAND));
                    }});
                }
            }
        }, jobScheduler, "launching PushJobServiceController command", "JobScheduler");
    }

    private int getNextJobId() {
        return nextServiceStartJobId.getAndUpdate(operand -> {
            final int result = operand + 1;
            if (result > MAX_SERVICE_START_JOB_ID) {
                return MIN_SERVICE_START_JOB_ID;
            }
            return result;
        });
    }
}

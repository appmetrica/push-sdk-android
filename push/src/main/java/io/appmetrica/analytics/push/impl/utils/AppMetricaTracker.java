package io.appmetrica.analytics.push.impl.utils;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.AppMetrica;
import io.appmetrica.analytics.IReporter;
import io.appmetrica.analytics.push.BuildConfig;
import io.appmetrica.analytics.push.coreutils.internal.utils.Tracker;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PreferenceManager;
import io.appmetrica.analytics.push.impl.PushServiceControllerComposite;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import java.util.HashMap;
import java.util.Map;

public class AppMetricaTracker implements Tracker {

    private static final String TAG = "[AppMetricaTracker]";

    static final String SDK_VERSION_CODE_FIELD = "version_code";
    static final String TRANSPORT_FIELD = "transport";
    static final String EVENT_ID = "event_id";

    @NonNull
    private final Context context;
    @NonNull
    private final String apiKey;

    @Nullable
    private volatile IReporter reporter;
    @NonNull
    private final Object lock = new Object();
    @NonNull
    private final AppMetricaTrackerEventIdGenerator eventIdGenerator;

    public AppMetricaTracker(@NonNull final Context context,
                             @NonNull final String apiKey,
                             @NonNull PreferenceManager preferenceManager) {
        this.context = context;
        this.apiKey = apiKey;
        this.eventIdGenerator = new AppMetricaTrackerEventIdGenerator(preferenceManager, "sdk");
    }

    @Override
    public void resumeSession() {
        DebugLogger.INSTANCE.info(TAG, "Resume Session.");
        getReporter().resumeSession();
    }

    @Override
    public void pauseSession() {
        DebugLogger.INSTANCE.info(TAG, "Pause Session.");
        getReporter().pauseSession();
    }

    @Override
    public void reportEvent(@NonNull String name) {
        reportEvent(name, null);
    }

    @Override
    public void reportEvent(@NonNull String name, @Nullable Map<String, Object> attributes) {
        final Map<String, Object> nonNullAttributes = attributes == null ? new HashMap<String, Object>() : attributes;
        nonNullAttributes.put(SDK_VERSION_CODE_FIELD, String.valueOf(BuildConfig.VERSION_CODE));
        PushServiceControllerComposite controller = AppMetricaPushCore.getInstance(context).getPushServiceController();
        if (controller != null) {
            nonNullAttributes.put(TRANSPORT_FIELD, controller.getTransportIds().toString());
        }
        nonNullAttributes.put(EVENT_ID, eventIdGenerator.generate());
        getReporter().reportEvent(name, nonNullAttributes);
    }

    @Override
    public void reportError(@NonNull String message, @Nullable Throwable error) {
        StringBuilder sb = new StringBuilder();
        sb.append(SDK_VERSION_CODE_FIELD).append(" = ").append(BuildConfig.VERSION_CODE).append(";");
        PushServiceControllerComposite controller = AppMetricaPushCore.getInstance(context).getPushServiceController();
        if (controller != null) {
            sb.append(TRANSPORT_FIELD).append(" = ").append(controller.getTransportIds());
        }
        sb.append(";");
        sb.append(message);
        getReporter().reportError(sb.toString(), error);
    }

    @Override
    public void reportUnhandledException(@NonNull Throwable exception) {
        getReporter().reportUnhandledException(exception);
    }

    @NonNull
    private IReporter getReporter() {
        if (reporter == null) {
            synchronized (lock) {
                if (reporter == null) {
                    reporter = AppMetrica.getReporter(context, apiKey);
                }
            }
        }
        return reporter;
    }
}

package io.appmetrica.analytics.push.impl.tracking;

import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.BuildConfig;
import java.util.HashMap;
import java.util.Map;

public abstract class AppMetricaPushEvent implements AppMetricaEvent {

    protected enum EventType {
        //todo discuss and choose the same names for all platforms
        EVENT_PUSH_TOKEN(14, "Push token"),
        EVENT_NOTIFICATION(15, "Push notification");

        private final int id;
        @NonNull
        private final String caption;

        // do not use annotations since Proguard incorrectly transforms it for enums and Jetifier cannot read it
        EventType(final int id, final String caption) {
            this.id = id;
            this.caption = caption;
        }

        int getId() {
            return id;
        }

        @NonNull
        String getCaption() {
            return caption;
        }
    }

    static final String EVENT_ENVIRONMENT_VERSION = "appmetrica_push_version";
    static final String EVENT_ENVIRONMENT_VERSION_NAME = "appmetrica_push_version_name";
    static final String EVENT_ENVIRONMENT_TRANSPORT = "appmetrica_push_transport";

    @NonNull
    private final EventType type;
    @NonNull
    private final String transport;

    protected AppMetricaPushEvent(@NonNull final EventType type, @NonNull String transport) {
        this.type = type;
        this.transport = transport;
    }

    @Override
    public int getEventType() {
        return type.getId();
    }

    @NonNull
    @Override
    public String getEventName() {
        return type.getCaption();
    }

    @NonNull
    @Override
    public Map<String, Object> getEventEnvironment() {
        final Map<String, Object> eventEnvironment = new HashMap<>();
        eventEnvironment.put(EVENT_ENVIRONMENT_VERSION, String.valueOf(BuildConfig.VERSION_CODE));
        eventEnvironment.put(EVENT_ENVIRONMENT_VERSION_NAME, BuildConfig.VERSION_NAME);
        eventEnvironment.put(EVENT_ENVIRONMENT_TRANSPORT, transport);
        return eventEnvironment;
    }
}

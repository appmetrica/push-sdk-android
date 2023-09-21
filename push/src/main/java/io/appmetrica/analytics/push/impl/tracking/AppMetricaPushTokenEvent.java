package io.appmetrica.analytics.push.impl.tracking;

import androidx.annotation.NonNull;

public class AppMetricaPushTokenEvent extends AppMetricaPushEvent {

    @NonNull
    private final String value;

    AppMetricaPushTokenEvent(@NonNull final String value, @NonNull String transport) {
        super(EventType.EVENT_PUSH_TOKEN, transport);
        this.value = value;
    }

    @NonNull
    @Override
    public String getEventValue() {
        return value;
    }
}

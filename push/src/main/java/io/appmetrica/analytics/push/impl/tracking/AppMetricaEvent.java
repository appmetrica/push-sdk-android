package io.appmetrica.analytics.push.impl.tracking;

import androidx.annotation.NonNull;
import java.util.Map;

public interface AppMetricaEvent {

    int getEventType();

    @NonNull
    String getEventName();

    @NonNull
    String getEventValue();

    @NonNull
    Map<String, Object> getEventEnvironment();

}

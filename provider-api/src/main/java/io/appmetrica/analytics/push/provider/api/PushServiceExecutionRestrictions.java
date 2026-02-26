package io.appmetrica.analytics.push.provider.api;

/**
 * Describes push service restrictions
 */
public abstract class PushServiceExecutionRestrictions {

    /**
     * Creates a new instance of {@link PushServiceExecutionRestrictions}.
     */
    public PushServiceExecutionRestrictions() {
    }

    /**
     * Returns the maximum duration for processing push service callbacks.
     * @return the maximum duration for processing push service callbacks, such as processing push messages
     * or updating a token
     */
    public Long getMaxTaskExecutionDurationSeconds() {
        return 0L;
    }
}

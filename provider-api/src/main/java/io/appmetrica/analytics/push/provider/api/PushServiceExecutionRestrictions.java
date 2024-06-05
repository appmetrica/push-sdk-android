package io.appmetrica.analytics.push.provider.api;

/**
 * Describes push service restrictions
 */
public abstract class PushServiceExecutionRestrictions {

    /**
     * @return the maximum duration for processing push service callbacks, such as processing push messages
     * or updating a token
     */
    public Long getMaxTaskExecutionDurationSeconds() {
        return 0L;
    }
}

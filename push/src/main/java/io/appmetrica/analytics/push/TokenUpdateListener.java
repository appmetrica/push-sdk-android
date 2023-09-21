package io.appmetrica.analytics.push;

import java.util.Map;

/**
 * Token update listener.
 * Listener can be set via {@link AppMetricaPush#setTokenUpdateListener(TokenUpdateListener)} method.
 */
public interface TokenUpdateListener {

    /**
     * Called when some token updated.
     *
     * @param tokens {@link Map} from push service title to push service token with all tokens
     */
    void onTokenUpdated(Map<String, String> tokens);

}

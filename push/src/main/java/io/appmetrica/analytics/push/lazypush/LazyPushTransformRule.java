package io.appmetrica.analytics.push.lazypush;

import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.impl.utils.StringTransform;
import java.util.List;

/**
 * Interface for custom lazy push request info patterns.
 * Custom transform rule has a higher priority than default rules.
 */
public interface LazyPushTransformRule extends StringTransform.Rule {

    /**
     * @return list of patterns from lazy push request info that this provider is able to process
     */
    @NonNull
    List<String> getPatternList();

    /**
     * This method is called only for values from {@link LazyPushTransformRule#getPatternList()} method.
     * It should return new value for pattern.
     *
     * @param pattern {@link String} with pattern from lazy push request info
     *                              if it is present in return value of
     *                              {@link LazyPushTransformRule#getPatternList()} method
     * @return new value for a given pattern
     */
    @NonNull
    String getNewValue(@NonNull String pattern);
}

package io.appmetrica.analytics.push.impl.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringTransform {

    public interface Rule {
        @NonNull
        List<String> getPatternList();

        @NonNull
        String getNewValue(@NonNull String pattern);
    }

    @Nullable
    private final String patternPrefix;
    @Nullable
    private final String patternSuffix;
    @NonNull
    private final Map<String, Rule> transformRules;

    public StringTransform() {
        this(null, null);
    }

    public StringTransform(@Nullable String patternPrefix, @Nullable String patternSuffix) {
        this.patternPrefix = patternPrefix;
        this.patternSuffix = patternSuffix;
        transformRules = new HashMap<String, Rule>();
    }

    @NonNull
    public StringTransform rule(@NonNull Rule rule) {
        for (String pattern : rule.getPatternList()) {
            transformRules.put(pattern, rule);
        }
        return this;
    }

    @NonNull
    public String transform(@NonNull String str) {
        String newStr = str;
        for (Map.Entry<String, Rule> transformRule : transformRules.entrySet()) {
            String key = wrapPattern(transformRule.getKey());
            if (newStr.matches(String.format(".*%s.*", key))) {
                newStr = newStr.replaceAll(key, transformRule.getValue().getNewValue(transformRule.getKey()));
            }
        }
        return newStr;
    }

    @NonNull
    private String wrapPattern(@NonNull String pattern) {
        return String.format("%s%s%s", wrapNullString(patternPrefix), pattern, wrapNullString(patternSuffix));
    }

    @NonNull
    private String wrapNullString(@Nullable String str) {
        return str == null ? "" : str;
    }
}

package io.appmetrica.analytics.push.coreutils.internal.utils;

import android.util.Log;
import androidx.annotation.Nullable;

public class PublicLogger extends BaseLogger {

    private static final BaseLogger INSTANCE = new PublicLogger();

    private PublicLogger() {}

    public static void d(@Nullable final String msg, @Nullable final Object... args) {
        INSTANCE.log(Log.DEBUG, msg, args);
    }

    public static void w(@Nullable final String msg, @Nullable final Object... args) {
        INSTANCE.log(Log.WARN, msg, args);
    }

    public static void i(@Nullable final String msg, @Nullable final Object... args) {
        INSTANCE.log(Log.INFO, msg, args);
    }

    public static void e(@Nullable final String msg, @Nullable final Object... args) {
        INSTANCE.log(Log.ERROR, msg, args);
    }

    public static void e(@Nullable final Throwable e, @Nullable final String msg, @Nullable final Object... args) {
        INSTANCE.log(Log.ERROR, e, msg, args);
    }

    public static void setEnabled() {
        INSTANCE.isEnabled = true;
    }

    @Override
    String getTag() {
        return "AppMetricaPush";
    }

    @Override
    String getPrefix() {
        return "";
    }
}

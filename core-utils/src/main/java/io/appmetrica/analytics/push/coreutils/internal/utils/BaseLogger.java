package io.appmetrica.analytics.push.coreutils.internal.utils;

import android.os.Process;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.BuildConfig;
import java.util.Locale;

public abstract class BaseLogger {

    static final int STACK_OFFSET = 5;

    protected volatile boolean isEnabled = BuildConfig.PUSH_DEBUG;

    protected boolean shouldLog() {
        return isEnabled;
    }

    @NonNull
    private String formatMessage(@NonNull final String message, @Nullable final Object[] params) {
        String msg = (params == null) ? message : String.format(Locale.US, message, params);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";

        for (int i = STACK_OFFSET; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass().getSuperclass();
            if (clazz == null || clazz.equals(BaseLogger.class) == false) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

                caller = callingClass + "." + trace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.US, "[%d/%d] %s: %s", Process.myPid(), Thread.currentThread().getId(), caller, msg);
    }

    protected void log(final int priority, @Nullable final String message, @Nullable Object... params) {
        if (shouldLog()) {
            Log.println(priority, getTag(), getMessage(message, params));
        }
    }

    protected void log(final int priority,
                       @Nullable final Throwable throwable,
                       @Nullable final String message,
                       @Nullable Object... params) {
        if (shouldLog()) {
            Log.println(priority, getTag(), getMessage(message, params) + "\n" + Log.getStackTraceString(throwable));
        }
    }

    @NonNull
    private String getMessage(@Nullable final String message, @Nullable final Object[] params) {
        return getPrefix() + formatMessage(wrapMsg(message), params);
    }

    @NonNull
    private String wrapMsg(@Nullable String msg) {
        return msg == null ? "" : msg;
    }

    abstract String getTag();

    abstract String getPrefix();
}

package io.appmetrica.analytics.push.impl.utils.executers;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ExecutorHelper {

    @SuppressLint("StaticFieldLeak")
    private static volatile ExecutorHelper instance;
    private static final Object INSTANCE_MONITOR = new Object();

    private final Object lock = new Object();

    @Nullable
    private volatile Executor commonExecutor;

    @NonNull
    public static ExecutorHelper getInstance() {
        if (instance == null) {
            synchronized (INSTANCE_MONITOR) {
                if (instance == null) {
                    instance = new ExecutorHelper();
                }
            }
        }
        return instance;
    }

    @NonNull
    public Executor getCommonExecutor() {
        if (commonExecutor == null) {
            synchronized (lock) {
                if (commonExecutor == null) {
                    commonExecutor = new Executor("AppMetricaPushCommon");
                }
            }
        }
        return commonExecutor;
    }
}

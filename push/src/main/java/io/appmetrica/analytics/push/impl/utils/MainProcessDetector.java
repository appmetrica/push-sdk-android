package io.appmetrica.analytics.push.impl.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.CoreUtils;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Main process detection strategy from
 * <a href="https://cs.chromium.org/chromium/src/base/android/java/src/org/chromium/base/ContextUtils.java?q=ContextUtil&sq=package:chromium&g=0&l=172">Chromium</a>.
 */
public class MainProcessDetector {

    @Nullable
    private String processName;

    /**
     * @return The name of the current process. E.g. "org.chromium.chrome:privileged_process0".
     */
    @Nullable
    public String getProcessName() {
        // Once we drop support JB, this method can be simplified to not cache sProcessName and call
        // ActivityThread.currentProcessName().
        if (processName != null) {
            return processName;
        }
        // Before JB MR2, currentActivityThread() returns null when called on a non-UI thread.
        // Cache the name to allow other threads to access it.

        processName = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 ?
            extractProcessNameSinceJBMR2() : extractProcessNameBeforeJBMR2();

        return processName;
    }

    @SuppressLint("StaticFieldLeak")
    @Nullable
    private String extractProcessNameBeforeJBMR2() {
        String result = null;
        try {
            if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
                result = extractProcessFromActivityThread();
            } else {
                FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
                    @Override
                    public String call() {
                        PLog.d("Start extractProcessFromActivityThread");
                        return extractProcessFromActivityThread();
                    }
                });
                new Handler(Looper.getMainLooper()).post(future);
                result = future.get(5, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            PLog.e(e, e.getMessage());
        }

        return result;
    }

    @Nullable
    private String extractProcessNameSinceJBMR2() {
        return extractProcessFromActivityThread();
    }

    @SuppressLint("PrivateApi")
    @Nullable
    private String extractProcessFromActivityThread() {
        try {
            // An even more convenient ActivityThread.currentProcessName() exists, but was not added
            // until JB MR2.
            Class<?> activityThreadClazz = Class.forName("android.app.ActivityThread");
            Object activityThread =
                activityThreadClazz.getMethod("currentActivityThread").invoke(null);
            // Before JB MR2, currentActivityThread() returns null when called on a non-UI thread.
            // Cache the name to allow other threads to access it.
            return (String) activityThreadClazz.getMethod("getProcessName").invoke(activityThread);
        } catch (Exception e) { // No multi-catch below API level 19 for reflection exceptions.
            // If fallback logic is ever needed, refer to:
            // https://chromium-review.googlesource.com/c/chromium/src/+/905563/1
            throw new RuntimeException(e);
        }
    }

    public boolean isMainProcess() {
        try {
            return !CoreUtils.isEmpty(getProcessName()) && !getProcessName().contains(":");
        } catch (Exception e) {
            return false;
        }
    }

}

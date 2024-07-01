package io.appmetrica.analytics.push.impl.utils.executers;

import android.os.Looper;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SingleExecutor {

    public abstract static class Runnable {

        private Looper looper;

        public abstract void run(@NonNull final CountDownLatch countDownLatch);

        @NonNull
        public Looper getLooper() {
            return looper;
        }

        private void withLooper(@NonNull Looper looper) {
            this.looper = looper;
        }
    }

    @NonNull
    private final Runnable runnable;
    @NonNull
    private final Executor executor;
    @NonNull
    private final CountDownLatch countDownLatch;

    public SingleExecutor(@NonNull Runnable runnable) {
        this(runnable, ExecutorHelper.getInstance().getCommonExecutor());
    }

    public SingleExecutor(@NonNull Runnable runnable, @NonNull Executor executor) {
        this.runnable = runnable;
        runnable.withLooper(executor.getLooper());
        this.executor = executor;
        countDownLatch = new CountDownLatch(1);
    }

    public void run(long timeout, @NonNull TimeUnit timeUnit) {
        run();
        await(timeout, timeUnit);
    }

    private void run() {
        if (countDownLatch.getCount() == 0) {
            return;
        }
        executor.execute(new java.lang.Runnable() {
            @Override
            public void run() {
                runnable.run(countDownLatch);
            }
        });
    }

    private void await(long timeout, @NonNull TimeUnit timeUnit) {
        try {
            countDownLatch.await(timeout, timeUnit);
        } catch (InterruptedException e) {
            PublicLogger.INSTANCE.error(e, e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}

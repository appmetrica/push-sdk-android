package io.appmetrica.analytics.push.impl.utils.executers;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

public class Executor {

    @NonNull
    private final HandlerThread handlerThread;
    @NonNull
    private final Handler handler;

    public Executor(@NonNull String name) {
        this(createLaunchedHandlerThread(name));
    }

    @NonNull
    public Looper getLooper() {
        return handlerThread.getLooper();
    }

    public void execute(@NonNull Runnable runnable) {
        handler.post(runnable);
    }

    @NonNull
    private static HandlerThread createLaunchedHandlerThread(@NonNull String name) {
        HandlerThread handlerThread = new HandlerThread(name);
        handlerThread.start();
        return handlerThread;
    }

    private Executor(@NonNull HandlerThread handlerThread) {
        this(handlerThread, new Handler(handlerThread.getLooper()));
    }

    @VisibleForTesting
    Executor(@NonNull HandlerThread handlerThread, @NonNull Handler handler) {
        this.handlerThread = handlerThread;
        this.handler = handler;
    }
}

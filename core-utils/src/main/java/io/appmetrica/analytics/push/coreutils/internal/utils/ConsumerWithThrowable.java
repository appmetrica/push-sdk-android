package io.appmetrica.analytics.push.coreutils.internal.utils;

import androidx.annotation.NonNull;

public interface ConsumerWithThrowable<T> {

    void consume(@NonNull T input) throws Throwable;
}

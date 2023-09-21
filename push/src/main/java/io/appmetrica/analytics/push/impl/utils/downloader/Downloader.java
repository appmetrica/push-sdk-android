package io.appmetrica.analytics.push.impl.utils.downloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Downloader {

    @Nullable
    byte[] download(@NonNull String url);
}

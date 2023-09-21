package io.appmetrica.analytics.push.impl.utils.downloader;

import android.content.Context;
import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.impl.utils.Utils;

public class DownloaderProvider {

    @NonNull
    private final Downloader downloader;

    public DownloaderProvider(@NonNull Context context) {
        if (Utils.isClassExists("okhttp3.OkHttpClient")) {
            downloader = new OkHttpDownloader(context);
        } else {
            downloader = new UrlConnectionDownloader();
        }
    }

    @NonNull
    public Downloader getDownloader() {
        return downloader;
    }
}

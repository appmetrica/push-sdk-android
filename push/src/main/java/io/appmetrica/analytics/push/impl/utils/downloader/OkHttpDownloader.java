package io.appmetrica.analytics.push.impl.utils.downloader;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger;
import io.appmetrica.analytics.push.impl.utils.Utils;
import java.io.IOException;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpDownloader implements Downloader {

    private static final String TAG = "[OkHttpDownloader]";

    private static final long MAX_CACHE_SIZE = 10L * 1024L * 1024L;

    @NonNull
    private final OkHttpClient okHttpClient;

    OkHttpDownloader(@NonNull Context context) {
        Cache cache = new Cache(context.getCacheDir(), MAX_CACHE_SIZE);
        okHttpClient = new OkHttpClient.Builder().cache(cache).build();
    }

    @Nullable
    @Override
    public byte[] download(@NonNull String url) {
        PLog.i("%s Download bitmap with url: %s", TAG, url);
        Request request = new Request.Builder().url(url).build();

        Response response = null;
        byte[] data = null;
        try {
            response = okHttpClient.newCall(request).execute();
            PLog.i("%s Get response with code: %d and message: %s", TAG, response.code(), response.message());
            if (response.cacheResponse() != null) {
                PLog.i("%s Get bitmap from cache", TAG);
            }
            if (response.body() != null) {
                data = response.body().bytes();
                PLog.i("%s Bitmap buffer length: %d", TAG, data.length);
            }
        } catch (IOException e) {
            PublicLogger.e(e, e.getMessage());
        } finally {
            Utils.closeSilently(response);
        }
        return data;
    }
}

package io.appmetrica.analytics.push.impl.utils.downloader;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.utils.Utils;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
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
        DebugLogger.INSTANCE.info(TAG, "Download bitmap with url: %s", url);
        Request request = new Request.Builder().url(url).build();

        Response response = null;
        byte[] data = null;
        try {
            response = okHttpClient.newCall(request).execute();
            DebugLogger.INSTANCE.info(
                TAG,
                "Get response with code: %d and message: %s",
                response.code(),
                response.message()
            );
            if (response.cacheResponse() != null) {
                DebugLogger.INSTANCE.info(TAG, "Get bitmap from cache");
            }
            if (response.body() != null) {
                data = response.body().bytes();
                DebugLogger.INSTANCE.info(TAG, "Bitmap buffer length: %d", data.length);
            }
        } catch (IOException e) {
            PublicLogger.INSTANCE.error(e, e.getMessage());
        } finally {
            Utils.closeSilently(response);
        }
        return data;
    }
}

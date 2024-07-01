package io.appmetrica.analytics.push.impl.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.utils.downloader.Downloader;
import io.appmetrica.analytics.push.impl.utils.downloader.DownloaderProvider;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;

public class BitmapLoader {

    private static final String TAG = "[BitmapLoader]";

    public static final float UNDEFINED_WIDTH = -1;
    public static final float UNDEFINED_HEIGHT = -1;

    @NonNull
    private final Downloader downloader;

    public BitmapLoader(@NonNull Context context) {
        downloader = new DownloaderProvider(context).getDownloader();
    }

    @Nullable
    public Bitmap get(@NonNull Context context, @NonNull String url, float width, float height) {
        float density = context.getResources().getDisplayMetrics().density;
        return get(url, density, width, height);
    }

    @Nullable
    public Bitmap get(@NonNull String url, float density, float width, float height) {
        DebugLogger.INSTANCE.info(TAG, "Get bitmap for url: %s", url);
        float widthPx = width * density;
        float heightPx = height * density;
        byte[] bitmapBytes = downloader.download(url);
        if (bitmapBytes == null) {
            return null;
        }
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, bitmapOptions);
        DebugLogger.INSTANCE.info(
            TAG,
            "Real bitmap options: width = %d, height = %d",
            bitmapOptions.outWidth,
            bitmapOptions.outHeight
        );
        float inSampleWidth = width > 0 ? bitmapOptions.outWidth / widthPx : 1f;
        float inSampleHeight = height > 0 ? bitmapOptions.outHeight / heightPx : 1f;
        float inSampleSize = Math.max(inSampleWidth, inSampleHeight);
        DebugLogger.INSTANCE.info(
            TAG,
            "Bitmap: inSampleWidth = %f; inSampleHeight = %f; inSampleSize = %f",
            inSampleWidth,
            inSampleHeight,
            inSampleSize
        );
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inSampleSize = Math.round(inSampleSize);
        return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, bitmapOptions);
    }
}

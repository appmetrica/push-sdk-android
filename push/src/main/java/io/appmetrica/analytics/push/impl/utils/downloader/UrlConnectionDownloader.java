package io.appmetrica.analytics.push.impl.utils.downloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.utils.Utils;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UrlConnectionDownloader implements Downloader {

    private static final String TAG = "[UrlConnectionDownloader]";

    @Nullable
    @Override
    public byte[] download(@NonNull String url) {
        DebugLogger.INSTANCE.info(TAG, "Download bitmap with url: %s", url);
        byte[] data = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        URLConnection connection = null;

        try {
            connection = new URL(url).openConnection();
            in = connection.getInputStream();
            out = new ByteArrayOutputStream();
            Utils.copy(in, out);
            data = out.toByteArray();
            DebugLogger.INSTANCE.info(TAG, "Bitmap buffer length: %d", data.length);
        } catch (Exception e) {
            PublicLogger.INSTANCE.error(e, e.getMessage());
        } catch (OutOfMemoryError e) {
            PublicLogger.INSTANCE.error(e, e.getMessage());
        } finally {
            Utils.closeSilently(in);
            Utils.closeSilently(out);
        }
        return data;
    }
}

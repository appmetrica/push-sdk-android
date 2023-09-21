package io.appmetrica.analytics.push.impl.utils.downloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.utils.PLog;
import io.appmetrica.analytics.push.coreutils.internal.utils.PublicLogger;
import io.appmetrica.analytics.push.impl.utils.Utils;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UrlConnectionDownloader implements Downloader {

    private static final String TAG = "[UrlConnectionDownloader]";

    @Nullable
    @Override
    public byte[] download(@NonNull String url) {
        PLog.i("%s Download bitmap with url: %s", TAG, url);
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
            PLog.i("%s Bitmap buffer length: %d", TAG, data.length);
        } catch (Exception e) {
            PublicLogger.e(e, e.getMessage());
        } catch (OutOfMemoryError e) {
            PublicLogger.e(e, e.getMessage());
        } finally {
            Utils.closeSilently(in);
            Utils.closeSilently(out);
        }
        return data;
    }
}

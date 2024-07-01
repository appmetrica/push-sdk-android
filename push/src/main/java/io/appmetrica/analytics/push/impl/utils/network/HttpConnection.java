package io.appmetrica.analytics.push.impl.utils.network;

import androidx.annotation.NonNull;
import io.appmetrica.analytics.push.impl.utils.Utils;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpConnection {

    @NonNull
    private final HttpURLConnection connection;

    public HttpConnection(@NonNull String url) throws IOException {
        connection = (HttpURLConnection) new URL(url).openConnection();
    }

    public HttpConnection(@NonNull String url, @NonNull Map<String, String> headers) throws IOException {
        this(url);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            addHeader(header.getKey(), header.getValue());
        }
    }

    public void addHeader(@NonNull String name, @NonNull String value) {
        connection.addRequestProperty(name, value);
    }

    @NonNull
    public byte[] get() throws IOException {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            PublicLogger.INSTANCE.info(
                "Send request %s with headers %s",
                connection.getURL(),
                connection.getRequestProperties()
            );
            int code = connection.getResponseCode();
            PublicLogger.INSTANCE.info("Request return code %s with message '%s' for %s",
                code, connection.getResponseMessage(), connection.getURL());
            if (code != HttpURLConnection.HTTP_OK) {
                throw new ConnectException(
                    String.format("Request return code %s with message '%s'", code, connection.getResponseMessage())
                );
            }
            in = connection.getInputStream();
            out = new ByteArrayOutputStream();
            Utils.copy(in, out);
            return out.toByteArray();
        } catch (IOException e) {
            PublicLogger.INSTANCE.info("Failed request for %s. %s", connection.getURL(), e.getMessage());
            throw e;
        } finally {
            Utils.closeSilently(in);
            Utils.closeSilently(out);
        }
    }
}

package io.appmetrica.analytics.push.impl.processing.transform.lazypush;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.logger.internal.PublicLogger;
import java.nio.charset.Charset;
import java.util.Locale;
import org.json.JSONObject;

public class LazyPushResponse {

    @Nullable
    private String errorParseMessage = null;
    @Nullable
    private String ignoreDetails = null;
    @Nullable
    private JSONObject pushMessageJson = null;

    public LazyPushResponse(@NonNull byte[] response) {
        parse(response);
    }

    private void parse(@NonNull byte[] response) {
        try {
            JSONObject json = new JSONObject(new String(response, Charset.forName("UTF-8")));
            if (json.length() > 1) {
                errorParseMessage =
                    String.format(Locale.US, "Lazy push response have %d elements", json.length());
            } else if (json.has("ignored")) {
                ignoreDetails = json.getJSONObject("ignored").getString("details");
            } else if (json.has(CoreConstants.PushMessage.ROOT_ELEMENT)) {
                pushMessageJson = json;
            } else {
                errorParseMessage = "Lazy push response does not contain ignored message or push message";
            }
        } catch (Throwable e) {
            PublicLogger.INSTANCE.error("Failed to parse lazy push response: " + e.getMessage(), e);
            errorParseMessage = "Failed to parse lazy push response: " + e.getMessage();
        }
    }

    public boolean isCorrect() {
        return errorParseMessage == null;
    }

    @Nullable
    public String getErrorParseMessage() {
        return errorParseMessage;
    }

    public boolean isIgnored() {
        return ignoreDetails != null;
    }

    @Nullable
    public String getIgnoreDetails() {
        return ignoreDetails;
    }

    @Nullable
    public JSONObject getPushMessageJson() {
        return pushMessageJson;
    }
}

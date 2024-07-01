package io.appmetrica.analytics.push.impl.storage;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import io.appmetrica.analytics.push.logger.internal.DebugLogger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class Token {

    private static final String TAG = "[Token]";
    private static final String TOKEN = "token";
    private static final String LAST_UPDATE_TIME = "lastUpdateTime";

    @Nullable
    public final String token;
    public final long lastUpdateTime;

    private Token(@NonNull JSONObject object) throws JSONException {
        this(
            object.has(TOKEN) ? object.getString(TOKEN) : null,
            object.getLong(LAST_UPDATE_TIME)
        );
    }

    @VisibleForTesting
    public Token(@Nullable String token, long lastUpdateTime) {
        this.token = token;
        this.lastUpdateTime = lastUpdateTime;
    }

    private static JSONObject newObject(@Nullable String token,
                                        long lastUpdateTime
    ) throws JSONException {
        return new JSONObject().put(TOKEN, token).put(LAST_UPDATE_TIME, lastUpdateTime);
    }

    @Nullable
    public static String saveToString(@NonNull Map<String, String> tokens, long currentTime) {
        try {
            JSONObject jsonTokens = new JSONObject();
            for (Map.Entry<String, String> entry : tokens.entrySet()) {
                jsonTokens.put(entry.getKey(), Token.newObject(entry.getValue(), currentTime));
            }
            return jsonTokens.toString();
        } catch (JSONException exception) {
            DebugLogger.INSTANCE.error(TAG, exception, "Exception while constructing new tokens list.");
        }
        return null;
    }

    @Nullable
    public static Map<String, Token> parseTokens(@Nullable String data) {
        if (TextUtils.isEmpty(data)) {
            return null;
        } else {
            try {
                final JSONObject jsonTokens = new JSONObject(data);
                HashMap<String, Token> tokens = new HashMap<String, Token>();
                for (String key : new Iterable<String>() {
                    @NonNull
                    @Override
                    public Iterator<String> iterator() {
                        return jsonTokens.keys();
                    }
                }) {
                    tokens.put(key, new Token(jsonTokens.getJSONObject(key)));
                }
                return tokens;
            } catch (JSONException ignored) {
            }
            return null;
        }
    }

}

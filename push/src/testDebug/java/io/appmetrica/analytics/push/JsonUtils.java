package io.appmetrica.analytics.push;

import org.json.JSONObject;

public class JsonUtils {

    public static JSONObject createJsonWithObject(String key, Object value) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, value);
        return jsonObject;
    }
}

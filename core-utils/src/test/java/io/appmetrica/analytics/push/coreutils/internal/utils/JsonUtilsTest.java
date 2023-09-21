package io.appmetrica.analytics.push.coreutils.internal.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomInt;
import static io.appmetrica.analytics.push.testutils.Rand.randomLong;
import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class JsonUtilsTest {

    private static final String KEY = randomString();

    @Test
    public void testOptBoolean() throws JSONException {
        assertThat(JsonUtils.optBoolean(null, randomString(), true)).isTrue();
        assertThat(JsonUtils.optBoolean(null, randomString(), false)).isFalse();
        assertThat(JsonUtils.optBoolean(createJsonWithValue(KEY, null), KEY, true)).isTrue();
        assertThat(JsonUtils.optBoolean(createJsonWithValue(KEY, null), KEY, false)).isFalse();
        assertThat(JsonUtils.optBoolean(createJsonWithValue(KEY, randomString()), KEY, true)).isTrue();
        assertThat(JsonUtils.optBoolean(createJsonWithValue(KEY, randomString()), KEY, false)).isFalse();
        assertThat(JsonUtils.optBoolean(createJsonWithValue(KEY, true), KEY, false)).isTrue();
        assertThat(JsonUtils.optBoolean(createJsonWithValue(KEY, false), KEY, true)).isFalse();
        assertThat(JsonUtils.optBoolean(createJsonWithValue(KEY, true), randomString(), false)).isFalse();
        assertThat(JsonUtils.optBoolean(createJsonWithValue(KEY, false), randomString(), true)).isTrue();
    }

    @Test
    public void testExtractStringSafely() throws JSONException {
        assertThat(JsonUtils.extractStringSafely(null, randomString())).isNull();
        assertThat(JsonUtils.extractStringSafely(createJsonWithValue(KEY, null), KEY)).isNull();

        String value = randomString();
        assertThat(JsonUtils.extractStringSafely(createJsonWithValue(KEY, value), KEY)).isEqualTo(value);
        assertThat(JsonUtils.extractStringSafely(createJsonWithValue(KEY, value), randomString())).isNull();
    }

    @Test
    public void testExtractBooleanSafely() throws JSONException {
        assertThat(JsonUtils.extractBooleanSafely(null, randomString())).isNull();
        assertThat(JsonUtils.extractBooleanSafely(createJsonWithValue(KEY, null), KEY)).isNull();
        assertThat(JsonUtils.extractBooleanSafely(createJsonWithValue(KEY, randomString()), KEY)).isNull();
        assertThat(JsonUtils.extractBooleanSafely(createJsonWithValue(KEY, true), KEY)).isTrue();
        assertThat(JsonUtils.extractBooleanSafely(createJsonWithValue(KEY, false), KEY)).isFalse();
        assertThat(JsonUtils.extractBooleanSafely(createJsonWithValue(KEY, true), randomString())).isNull();
    }

    @Test
    public void testExtractIntegerSafely() throws JSONException {
        assertThat(JsonUtils.extractIntegerSafely(null, randomString())).isNull();
        assertThat(JsonUtils.extractIntegerSafely(createJsonWithValue(KEY, null), KEY)).isNull();
        assertThat(JsonUtils.extractIntegerSafely(createJsonWithValue(KEY, randomString()), KEY)).isNull();

        int value = randomInt();
        assertThat(JsonUtils.extractIntegerSafely(createJsonWithValue(KEY, value), KEY)).isEqualTo(value);
        assertThat(JsonUtils.extractIntegerSafely(createJsonWithValue(KEY, value), randomString())).isNull();
    }

    @Test
    public void testExtractLongSafely() throws JSONException {
        assertThat(JsonUtils.extractLongSafely(null, randomString())).isNull();
        assertThat(JsonUtils.extractLongSafely(createJsonWithValue(KEY, null), KEY)).isNull();
        assertThat(JsonUtils.extractLongSafely(createJsonWithValue(KEY, randomString()), KEY)).isNull();

        long value = randomLong();
        assertThat(JsonUtils.extractLongSafely(createJsonWithValue(KEY, value), KEY)).isEqualTo(value);
        assertThat(JsonUtils.extractLongSafely(createJsonWithValue(KEY, value), randomString())).isNull();
    }

    @NonNull
    private JSONObject createJsonWithValue(@NonNull String key, @Nullable Object value) throws JSONException {
        return new JSONObject().put(key, value);
    }
}

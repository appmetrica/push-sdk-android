package io.appmetrica.analytics.push.impl.storage;

import java.util.Collections;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class TokenTest {

    @Test
    public void parseTokens() {
        String token = "blablatok";
        String transport = "sometransport";
        int saveTime = 12123213;
        Map<String, Token> tokens = Token.parseTokens(getJSONString(transport, token, saveTime));
        assertThat(tokens.size()).isEqualTo(1);
        assertThat(tokens).containsKey(transport);
        assertThat(tokens.get(transport).token).isEqualTo(token);
        assertThat(tokens.get(transport).lastUpdateTime).isEqualTo(saveTime);
    }

    @Test
    public void testSaveToString() {
        String token = "blablatok";
        String transport = "sometransport";
        int saveTime = 12123213;
        String string = Token.saveToString(Collections.singletonMap(transport, new Token(token, saveTime)));
        assertThat(string).isEqualTo(getJSONString(transport, token, saveTime));
    }

    @Test
    public void nullableToken() {
        String token = null;
        String transport = "sometransport";
        int saveTime = 12123213;
        Map<String, Token> tokens =
            Token.parseTokens(Token.saveToString(Collections.singletonMap(transport, new Token(token, saveTime))));
        assertThat(tokens.size()).isEqualTo(1);
        assertThat(tokens).containsKey(transport);
        assertThat(tokens.get(transport).token).isEqualTo(token);
        assertThat(tokens.get(transport).lastUpdateTime).isEqualTo(saveTime);
    }

    private String getJSONString(String transport, String token, long saveTime) {
        try {
            return new JSONObject().put(transport,
                new JSONObject()
                    .put("token", token)
                    .put("lastUpdateTime", saveTime)
            ).toString();
        } catch (JSONException ignored) {
            return null;
        }
    }
}

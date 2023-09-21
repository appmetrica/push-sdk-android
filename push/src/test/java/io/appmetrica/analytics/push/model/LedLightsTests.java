package io.appmetrica.analytics.push.model;

import io.appmetrica.analytics.push.JsonUtils;
import io.appmetrica.analytics.push.impl.Constants;
import java.util.Random;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class LedLightsTests {

    @Test
    public void testColorShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(new LedLights(new JSONObject()).getColor()).isNull();
    }

    @Test
    public void testColorShouldBeNullIfStringInJson() throws Exception {
        testColorShouldBeNull("invalid");
    }

    @Test
    public void testColorShouldBeNullIfBooleanInJson() throws Exception {
        testColorShouldBeNull(true);
    }

    @Test
    public void testColorShouldBeNullIfNullInJson() throws Exception {
        testColorShouldBeNull(null);
    }

    @Test
    public void testColorShouldBeExpectedIfDefinedInJson() throws Exception {
        int expected = new Random().nextInt();
        assertThat(new LedLights(createJsonWithColor(expected)).getColor()).isEqualTo(expected);
    }

    private void testColorShouldBeNull(Object input) throws Exception {
        assertThat(new LedLights(createJsonWithColor(input)).getColor()).isNull();
    }

    private JSONObject createJsonWithColor(Object value) throws Exception {
        return JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.LedLights.LED_LIGHTS_COLOR, value);
    }

    @Test
    public void testOnMsShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(new LedLights(new JSONObject()).getOnMs()).isNull();
    }

    @Test
    public void testOnMsShouldBeNullIfInvalidInJson() throws Exception {
        testOnMsShouldBeNull("invalid");
    }

    @Test
    public void testOnMsShouldBeNullIfNullInJson() throws Exception {
        testOnMsShouldBeNull(null);
    }

    @Test
    public void testOnMsShouldBeNullIfBooleanInJson() throws Exception {
        testOnMsShouldBeNull(true);
    }

    private void testOnMsShouldBeNull(Object input) throws Exception {
        assertThat(new LedLights(createJsonWithOnMs(input)).getOffMs()).isNull();
    }

    private JSONObject createJsonWithOnMs(Object value) throws Exception {
        return JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.LedLights.LED_LIGHTS_ON_MS, value);
    }

    @Test
    public void testOnMsShouldBeExpectedIfDefinedInJson() throws Exception {
        int expected = new Random().nextInt();
        assertThat(new LedLights(createJsonWithOnMs(expected)).getOnMs()).isEqualTo(expected);
    }

    @Test
    public void testOffMsShouldBeNullIfNotDefinedInJson() throws Exception {
        assertThat(new LedLights(new JSONObject()).getOffMs()).isNull();
    }

    @Test
    public void testOffMsShouldBeNullIfInvalidInJson() throws Exception {
        testOffMsShouldBeNull("invalid");
    }

    @Test
    public void testOffMsShouldBeNullIfNullInJson() throws Exception {
        testOffMsShouldBeNull(null);
    }

    @Test
    public void testOffMsShouldBeNullIfBooleanInJson() throws Exception {
        testOffMsShouldBeNull(true);
    }

    @Test
    public void testOffMsShouldBeExpectedIfDefinedInJson() throws Exception {
        Integer expected = new Random().nextInt();
        assertThat(new LedLights(createJsonObjectWithOffMs(expected)).getOffMs()).isEqualTo(expected);
    }

    private void testOffMsShouldBeNull(Object input) throws Exception {
        assertThat(new LedLights(createJsonObjectWithOffMs(input)).getOffMs()).isNull();
    }

    private JSONObject createJsonObjectWithOffMs(Object value) throws Exception {
        return JsonUtils.createJsonWithObject(Constants.PushMessage.Notification.LedLights.LED_LIGHTS_OFF_MS, value);
    }

    @Test
    public void testLedLightsShouldBeValidIfAllPropertiesAreNotNull() {
        final LedLights ledLights =
            new LedLights(new Random().nextInt(), new Random().nextInt(), new Random().nextInt());
        assertThat(ledLights.isValid()).isTrue();
    }

    @Test
    public void testLedLightsShouldBeInvalidIfColorIsNull() {
        final LedLights ledLights =
            new LedLights(null, new Random().nextInt(), new Random().nextInt());
        assertThat(ledLights.isValid()).isFalse();
    }

    @Test
    public void testLedLightsShouldBeInvalidIfOnMsIsNull() {
        final LedLights ledLights =
            new LedLights(new Random().nextInt(), null, new Random().nextInt());
        assertThat(ledLights.isValid()).isFalse();
    }

    @Test
    public void testLedLightsShouldBeInvalidIfOffMsIsNull() {
        final LedLights ledLights =
            new LedLights(new Random().nextInt(), new Random().nextInt(), null);
        assertThat(ledLights.isValid()).isFalse();
    }
}

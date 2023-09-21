package io.appmetrica.analytics.push.provider.hms.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class IdentifierTests {

    @Test
    public void testIfAppIdAndSenderIdNotNull() {
        String appId = randomString();
        Identifier identifier = new Identifier(appId);

        assertThat(identifier.isValid()).isEqualTo(true);
        assertThat(identifier.isEmpty()).isEqualTo(false);
        assertThat(identifier.getAppId()).isEqualTo(appId);
    }

    @Test
    public void testIfAppIdNull() {
        Identifier identifier = new Identifier(null);

        assertThat(identifier.isValid()).isEqualTo(false);
        assertThat(identifier.isEmpty()).isEqualTo(true);
    }

}

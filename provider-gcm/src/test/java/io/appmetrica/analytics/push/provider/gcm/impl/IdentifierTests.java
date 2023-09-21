package io.appmetrica.analytics.push.provider.gcm.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class IdentifierTests {

    @Test
    public void testNoSenderId() {
        Identifier identifier = new Identifier(null);

        assertThat(identifier.isValid()).isFalse();
    }

    @Test
    public void testSenderId() {
        Identifier identifier = new Identifier(randomString());

        assertThat(identifier.isValid()).isTrue();
    }
}

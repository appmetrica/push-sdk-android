package io.appmetrica.analytics.push.provider.firebase.impl;

import com.google.firebase.FirebaseOptions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class IdentifierTests {

    @Test
    public void testIfAppIdAndSenderIdNotNull() {
        Identifier identifier = new Identifier(null, randomString(), randomString(), null);

        assertThat(identifier.isValid()).isEqualTo(true);
        assertThat(identifier.isEmpty()).isEqualTo(false);
    }

    @Test
    public void testIfAppIdNull() {
        Identifier identifier = new Identifier(null, null, randomString(), null);

        assertThat(identifier.isValid()).isEqualTo(false);
        assertThat(identifier.isEmpty()).isEqualTo(false);
    }

    @Test
    public void testIfSenderIdNull() {
        Identifier identifier = new Identifier(null, randomString(), null, null);

        assertThat(identifier.isValid()).isEqualTo(false);
        assertThat(identifier.isEmpty()).isEqualTo(false);
    }

    @Test
    public void testIfAppIdAndSenderIdNull() {
        Identifier identifier = new Identifier(null, null, null, null);

        assertThat(identifier.isValid()).isEqualTo(false);
        assertThat(identifier.isEmpty()).isEqualTo(true);
    }

    @Test
    public void testToFirebaseOptions() {
        Identifier identifier = new Identifier(randomString(), randomString(), randomString(), randomString());
        FirebaseOptions options = identifier.toFirebaseOptions();

        assertThat(options.getGcmSenderId()).isEqualTo(identifier.getSenderId());
        assertThat(options.getApplicationId()).isEqualTo(identifier.getAppId());
        assertThat(options.getApiKey()).isEqualTo(identifier.getApiKey());
        assertThat(options.getProjectId()).isEqualTo(identifier.getProjectId());
    }

}

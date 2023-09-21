package io.appmetrica.analytics.push.provider.rustore.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class IdentifierTest {

    @Test
    public void identifierIfProjectIdIsNotNull() {
        final String projectId = randomString();
        final Identifier identifier = new Identifier(projectId);

        assertThat(identifier.isValid()).isEqualTo(true);
        assertThat(identifier.isEmpty()).isEqualTo(false);
        assertThat(identifier.projectId).isEqualTo(projectId);
    }

    @Test
    public void identifierIfProjectIdIsNull() {
        final Identifier identifier = new Identifier(null);

        assertThat(identifier.isValid()).isEqualTo(false);
        assertThat(identifier.isEmpty()).isEqualTo(true);
        assertThat(identifier.projectId).isNull();
    }
}

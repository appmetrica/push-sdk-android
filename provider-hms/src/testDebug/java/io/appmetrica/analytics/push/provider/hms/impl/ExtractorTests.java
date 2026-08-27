package io.appmetrica.analytics.push.provider.hms.impl;

import androidx.annotation.NonNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public abstract class ExtractorTests {

    private IdentifierExtractor mIdentifierExtractor;

    public void setUp(@NonNull final IdentifierExtractor identifierExtractor) {
        mIdentifierExtractor = identifierExtractor;
    }

    @Test
    public void testGetAppId() {
        String appId = createAppId();

        assertThat(mIdentifierExtractor.getAppId()).isEqualTo(appId);
    }

    @Test
    public void testGetAppIdIfAppIdNotFound() {
        assertThat(mIdentifierExtractor.getAppId()).isNull();
    }

    @NonNull
    abstract String createAppId();

}

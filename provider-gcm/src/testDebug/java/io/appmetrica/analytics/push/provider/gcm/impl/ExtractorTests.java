package io.appmetrica.analytics.push.provider.gcm.impl;

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
    public void testGetSenderId() {
        String senderId = createSenderId();

        assertThat(mIdentifierExtractor.getSenderId()).isEqualTo(senderId);
    }

    @Test
    public void testGetSenderIdIfSenderIdNotFound() {
        assertThat(mIdentifierExtractor.getSenderId()).isNull();
    }

    @NonNull
    abstract String createSenderId();

}

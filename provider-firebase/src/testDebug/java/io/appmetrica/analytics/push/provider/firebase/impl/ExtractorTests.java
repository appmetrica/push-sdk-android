package io.appmetrica.analytics.push.provider.firebase.impl;

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
    public void testGetApiKey() {
        String apiKey = createApiKey();

        assertThat(mIdentifierExtractor.getApiKey()).isEqualTo(apiKey);
    }

    @Test
    public void testGetApiKeyIfApiKeyNotFound() {
        assertThat(mIdentifierExtractor.getApiKey()).isNull();
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

    @Test
    public void testGetSenderId() {
        String senderId = createSenderId();

        assertThat(mIdentifierExtractor.getSenderId()).isEqualTo(senderId);
    }

    @Test
    public void testGetSenderIdIfSenderIdNotFound() {
        assertThat(mIdentifierExtractor.getSenderId()).isNull();
    }

    @Test
    public void testGetProjectId() {
        String projectId = createProjectId();

        assertThat(mIdentifierExtractor.getProjectId()).isEqualTo(projectId);
    }

    @Test
    public void testGetProjectIdIfProjectIdNotFound() {
        assertThat(mIdentifierExtractor.getProjectId()).isNull();
    }

    @NonNull
    abstract String createApiKey();

    @NonNull
    abstract String createAppId();

    @NonNull
    abstract String createSenderId();

    @NonNull
    abstract String createProjectId();
}

package io.appmetrica.analytics.push.provider.rustore.impl;

import androidx.annotation.NonNull;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class ExtractorTests {

    private IdentifierExtractor identifierExtractor;

    public void setUp(@NonNull final IdentifierExtractor identifierExtractor) {
        this.identifierExtractor = identifierExtractor;
    }

    @Test
    public void getProjectId() {
        String projectId = createProjectId();

        assertThat(identifierExtractor.getProjectId()).isEqualTo(projectId);
    }

    @Test
    public void getProjectIdIfProjectIdNotFound() {
        assertThat(identifierExtractor.getProjectId()).isNull();
    }

    @NonNull
    abstract String createProjectId();
}

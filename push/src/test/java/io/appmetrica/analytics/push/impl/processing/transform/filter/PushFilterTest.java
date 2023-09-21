package io.appmetrica.analytics.push.impl.processing.transform.filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import org.assertj.core.api.SoftAssertions;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class PushFilterTest {

    protected PushFilter pushFilter;

    public void setUp(@NonNull PushFilter pushFilter) {
        this.pushFilter = pushFilter;
    }

    protected void assertShow(@NonNull PushMessage pushMessage) {
        assertThat(pushFilter.filter(pushMessage).filterResultCode).isEqualTo(PushFilter.FilterResultCode.SHOW);
    }

    protected void assertSilence(@NonNull PushMessage pushMessage) {
        assertThat(pushFilter.filter(pushMessage).filterResultCode).isEqualTo(PushFilter.FilterResultCode.SILENCE);
    }

    protected void assertSilence(@NonNull PushMessage pushMessage, @Nullable String category) {
        PushFilter.FilterResult result = pushFilter.filter(pushMessage);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.filterResultCode).isEqualTo(PushFilter.FilterResultCode.SILENCE);
        softly.assertThat(result.category).isEqualTo(category);
        softly.assertAll();
    }

    protected void assertSilence(
        @NonNull PushMessage pushMessage,
        @Nullable String category,
        @Nullable String details
    ) {
        PushFilter.FilterResult result = pushFilter.filter(pushMessage);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result.filterResultCode).isEqualTo(PushFilter.FilterResultCode.SILENCE);
        softly.assertThat(result.category).isEqualTo(category);
        softly.assertThat(result.details).isEqualTo(details);
        softly.assertAll();
    }
}

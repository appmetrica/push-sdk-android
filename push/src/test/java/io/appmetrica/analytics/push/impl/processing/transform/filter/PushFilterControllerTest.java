package io.appmetrica.analytics.push.impl.processing.transform.filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import io.appmetrica.analytics.push.settings.PushFilteredCallback;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class PushFilterControllerTest {

    private PushMessage pushMessage;
    private PushFilteredCallback pushFilteredCallback;
    private PushFilterController pushFilterController;

    @Before
    public void setUp() {
        pushMessage = mock(PushMessage.class);
        pushFilteredCallback = mock(PushFilteredCallback.class);
        pushFilterController = new PushFilterController();
        pushFilterController.addPushCallback(pushFilteredCallback);
    }

    @Test
    public void testAllFiltersShown() {
        pushFilterController.addPushFilter(createPushFilterWithShownResult());
        pushFilterController.addPushFilter(createPushFilterWithShownResult());
        assertThat(pushFilterController.filter(pushMessage).filterResultCode)
            .isEqualTo(PushFilter.FilterResultCode.SHOW);
        verify(pushFilteredCallback)
            .onPushFiltered(eq(pushMessage), argThat(new FilterResultCodeIs(PushFilter.FilterResultCode.SHOW)));
    }

    @Test
    public void testFirstFilterSilence() {
        pushFilterController.addPushFilter(createPushFilterWithSilenceResult(randomString(), randomString()));
        pushFilterController.addPushFilter(createPushFilterWithShownResult());
        assertThat(pushFilterController.filter(pushMessage).filterResultCode)
            .isEqualTo(PushFilter.FilterResultCode.SILENCE);
        verify(pushFilteredCallback)
            .onPushFiltered(eq(pushMessage), argThat(new FilterResultCodeIs(PushFilter.FilterResultCode.SILENCE)));
    }

    @Test
    public void testLastFilterSilence() {
        pushFilterController.addPushFilter(createPushFilterWithShownResult());
        pushFilterController.addPushFilter(createPushFilterWithSilenceResult(randomString(), randomString()));
        assertThat(pushFilterController.filter(pushMessage).filterResultCode)
            .isEqualTo(PushFilter.FilterResultCode.SILENCE);
        verify(pushFilteredCallback)
            .onPushFiltered(eq(pushMessage), argThat(new FilterResultCodeIs(PushFilter.FilterResultCode.SILENCE)));
    }

    @Test
    public void testAllFiltersSilence() {
        String expectedCategory = randomString();
        pushFilterController.addPushFilter(createPushFilterWithSilenceResult(expectedCategory, randomString()));
        pushFilterController.addPushFilter(createPushFilterWithSilenceResult(randomString(), randomString()));
        PushFilter.FilterResult result = pushFilterController.filter(pushMessage);
        assertThat(result.filterResultCode).isEqualTo(PushFilter.FilterResultCode.SILENCE);
        assertThat(result.category).isEqualTo(expectedCategory);
        verify(pushFilteredCallback)
            .onPushFiltered(eq(pushMessage), argThat(new FilterResultCodeIs(PushFilter.FilterResultCode.SILENCE)));
    }

    @NonNull
    private PushFilter createPushFilterWithShownResult() {
        PushFilter pushFilter = mock(PushFilter.class);
        when(pushFilter.filter(any(PushMessage.class))).thenReturn(PushFilter.FilterResult.show());
        return pushFilter;
    }

    @NonNull
    private PushFilter createPushFilterWithSilenceResult(@Nullable String category, @Nullable String details) {
        PushFilter pushFilter = mock(PushFilter.class);
        when(pushFilter.filter(any(PushMessage.class))).thenReturn(PushFilter.FilterResult.silence(category, details));
        return pushFilter;
    }

    private static final class FilterResultCodeIs implements ArgumentMatcher<PushFilter.FilterResult> {

        private final PushFilter.FilterResultCode expected;

        public FilterResultCodeIs(final PushFilter.FilterResultCode expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(PushFilter.FilterResult argument) {
            return argument.filterResultCode == expected;
        }
    }
}

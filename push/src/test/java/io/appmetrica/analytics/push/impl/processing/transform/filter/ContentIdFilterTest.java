package io.appmetrica.analytics.push.impl.processing.transform.filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ContentIdFilterTest extends PushFilterTest {

    private PushMessageHistory pushMessageHistory;

    @Before
    public void setUp() {
        pushMessageHistory = mock(PushMessageHistory.class);
        super.setUp(new ContentIdFilter(pushMessageHistory));
    }

    @Test
    public void testNoContentId() {
        assertShow(createPushMessage(null));
    }

    @Test
    public void testEmptyContentId() {
        assertShow(createPushMessage(""));
    }

    @Test
    public void testNewContentId() {
        when(pushMessageHistory.getContentIds()).thenReturn(Collections.<String>emptyList());
        assertShow(createPushMessage(randomString()));
    }

    @Test
    public void testDuplicateContentId() {
        String contentId = randomString();
        when(pushMessageHistory.getContentIds()).thenReturn(Collections.singletonList(contentId));
        assertSilence(createPushMessage(contentId));
    }

    @NonNull
    private PushMessage createPushMessage(@Nullable String contentId) {
        Filters filters = mock(Filters.class);
        when(filters.getContentId()).thenReturn(contentId);

        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getFilters()).thenReturn(filters);
        return pushMessage;
    }
}

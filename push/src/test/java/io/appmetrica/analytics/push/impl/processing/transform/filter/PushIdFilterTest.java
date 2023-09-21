package io.appmetrica.analytics.push.impl.processing.transform.filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
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
public class PushIdFilterTest extends PushFilterTest {

    private PushMessageHistory pushMessageHistory;

    @Before
    public void setUp() {
        pushMessageHistory = mock(PushMessageHistory.class);
        super.setUp(new PushIdFilter(pushMessageHistory));
    }

    @Test
    public void testNoPushId() {
        assertSilence(createPushMessage(null));
    }

    @Test
    public void testEmptyPushId() {
        assertSilence(createPushMessage(""));
    }

    @Test
    public void testNewPushId() {
        when(pushMessageHistory.getPushIds()).thenReturn(Collections.<String>emptyList());
        assertShow(createPushMessage(randomString()));
    }

    @Test
    public void testDuplicatePushId() {
        String pushId = randomString();
        when(pushMessageHistory.getPushIds()).thenReturn(Collections.singletonList(pushId));
        assertSilence(createPushMessage(pushId));
    }

    @NonNull
    private PushMessage createPushMessage(@Nullable String pushId) {
        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getNotificationId()).thenReturn(pushId);
        return pushMessage;
    }
}

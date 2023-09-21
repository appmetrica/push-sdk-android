package io.appmetrica.analytics.push.impl.processing.transform.filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class TimeToLiveFilterTest extends PushFilterTest {

    private static final String TIME_TO_LIVE_IS_UP = "Time to live is up";

    @Before
    public void setUp() {
        super.setUp(new TimeToLiveFilter());
    }

    @Test
    public void testNoTimeToShowAndNoNotificationBlock() {
        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getTimeToShowMillis()).thenReturn(null);
        when(pushMessage.getNotification()).thenReturn(null);
        assertShow(pushMessage);
    }

    @Test
    public void testNoTimeToShowAndNoTimeToHide() {
        assertShow(createPushMessage(null, null));
    }

    @Test
    public void testTimeToShowLessCurTime() {
        long timeToShow = System.currentTimeMillis() - 1000;
        assertSilence(createPushMessage(timeToShow, null), TIME_TO_LIVE_IS_UP);
    }

    @Test
    public void testTimeToShowMoreCurTime() {
        long timeToShow = System.currentTimeMillis() + 1000;
        assertShow(createPushMessage(timeToShow, null));
    }

    @Test
    public void testTimeToHideLessCurTime() {
        long timeToHide = System.currentTimeMillis() - 1000;
        assertSilence(createPushMessage(null, timeToHide), TIME_TO_LIVE_IS_UP);
    }

    @Test
    public void testTimeToHideMoreCurTime() {
        long timeToHide = System.currentTimeMillis() + 1000;
        assertShow(createPushMessage(null, timeToHide));
    }

    @NonNull
    private PushMessage createPushMessage(@Nullable Long timeToShow, @Nullable Long timeToHide) {
        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getTimeToShowMillis()).thenReturn(timeToShow);

        PushNotification pushNotification = mock(PushNotification.class);
        when(pushNotification.getTimeToHideMillis()).thenReturn(timeToHide);
        when(pushMessage.getNotification()).thenReturn(pushNotification);
        return pushMessage;
    }
}

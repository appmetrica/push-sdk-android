package io.appmetrica.analytics.push.impl.processing.transform.filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class SinglePushPerPeriodFilterTest extends PushFilterTest {

    private static final int SINGLE_PUSH_PER_PERIOD_MINUTES = 10;
    private static final String CHANNEL_ID = randomString();

    private PushMessageHistory pushMessageHistory;

    @Before
    public void setUp() {
        pushMessageHistory = mock(PushMessageHistory.class);
        super.setUp(new SinglePushPerPeriodFilter(pushMessageHistory));
    }

    @Test
    public void testNoFilters() {
        PushMessage pushMessage = createPushMessage();
        when(pushMessage.getFilters()).thenReturn(null);
        assertShow(pushMessage);
    }

    @Test
    public void testNoOnePushPerPeriod() {
        PushMessage pushMessage = createPushMessage();
        when(pushMessage.getFilters().getOnePushPerPeriodMinutes()).thenReturn(null);
        assertShow(pushMessage);
    }

    @Test
    public void testNoPushNotification() {
        PushMessage pushMessage = createPushMessage();
        when(pushMessage.getNotification()).thenReturn(null);
        assertShow(pushMessage);
    }

    @Test
    public void testDateTranslation() {
        long now = System.currentTimeMillis();
        when(pushMessageHistory.getLastShownTimeForChannelId(CHANNEL_ID))
            .thenReturn(now + TimeUnit.MINUTES.toMillis(SINGLE_PUSH_PER_PERIOD_MINUTES) / 2);
        assertShow(createPushMessage());
    }

    @Test
    public void testOnePushPerPeriod() {
        long now = System.currentTimeMillis();
        when(pushMessageHistory.getLastShownTimeForChannelId(CHANNEL_ID))
            .thenReturn(now - TimeUnit.MINUTES.toMillis(SINGLE_PUSH_PER_PERIOD_MINUTES) * 2);
        assertShow(createPushMessage());

        when(pushMessageHistory.getLastShownTimeForChannelId(CHANNEL_ID))
            .thenReturn(now - TimeUnit.MINUTES.toMillis(SINGLE_PUSH_PER_PERIOD_MINUTES) / 2);
        assertSilence(createPushMessage());
    }

    @NonNull
    private PushMessage createPushMessage(@Nullable Integer onePushPerPeriod, @Nullable String channelId) {
        Filters filters = mock(Filters.class);
        when(filters.getOnePushPerPeriodMinutes()).thenReturn(onePushPerPeriod);

        PushNotification pushNotification = mock(PushNotification.class);
        when(pushNotification.getChannelId()).thenReturn(channelId);

        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getFilters()).thenReturn(filters);
        when(pushMessage.getNotification()).thenReturn(pushNotification);
        return pushMessage;
    }

    @NonNull
    private PushMessage createPushMessage() {
        return createPushMessage(SINGLE_PUSH_PER_PERIOD_MINUTES, CHANNEL_ID);
    }
}

package io.appmetrica.analytics.push.impl.processing.transform.filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class PerDayFilterTest extends PushFilterTest {

    private static final int MAX_PER_DAY = 10;
    private static final String CHANNEL_ID = randomString();

    private PushMessageHistory pushMessageHistory;

    @Before
    public void setUp() {
        pushMessageHistory = mock(PushMessageHistory.class);
        super.setUp(new PerDayFilter(pushMessageHistory));
    }

    @Test
    public void testNoFilters() {
        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getFilters()).thenReturn(null);
        assertShow(pushMessage);
    }

    @Test
    public void testNoMaxPerDay() {
        assertShow(createPushMessage(null, null));
    }

    @Test
    public void testNoPushNotification() {
        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getFilters()).thenReturn(mock(Filters.class));
        when(pushMessage.getNotification()).thenReturn(null);
        assertShow(pushMessage);
    }

    @Test
    public void testNewDay() {
        List<Long> shownTimes = new LinkedList<Long>();
        when(pushMessageHistory.getShownTimesForChannelId(CHANNEL_ID)).thenReturn(shownTimes);

        for (int i = 0; i < MAX_PER_DAY; i++) {
            shownTimes.add(getPreviousDayMillis());
        }
        assertShow(createPushMessage(MAX_PER_DAY, CHANNEL_ID));
    }

    @Test
    public void testMaxPerDay() {
        List<Long> shownTimes = new LinkedList<Long>();
        when(pushMessageHistory.getShownTimesForChannelId(anyString())).thenReturn(Collections.<Long>emptyList());
        when(pushMessageHistory.getShownTimesForChannelId(CHANNEL_ID)).thenReturn(shownTimes);

        for (int i = 0; i < MAX_PER_DAY; i++) {
            assertShow(createPushMessage(MAX_PER_DAY, CHANNEL_ID));
            shownTimes.add(getTodayMillis());
        }
        assertSilence(createPushMessage(MAX_PER_DAY, CHANNEL_ID));
        assertShow(createPushMessage(MAX_PER_DAY, randomString()));
    }

    @Test
    public void testDateTranslation() {
        List<Long> shownTimes = new LinkedList<Long>();
        when(pushMessageHistory.getShownTimesForChannelId(CHANNEL_ID)).thenReturn(shownTimes);

        for (int i = 0; i < MAX_PER_DAY; i++) {
            shownTimes.add(getTodayMillis());
        }
        shownTimes.add(getNextDayMillis());
        assertSilence(createPushMessage(MAX_PER_DAY, CHANNEL_ID));
    }

    @NonNull
    private PushMessage createPushMessage(@Nullable Integer maxPerDay, @Nullable String channelId) {
        Filters filters = mock(Filters.class);
        when(filters.getMaxPushPerDay()).thenReturn(maxPerDay);

        PushNotification pushNotification = mock(PushNotification.class);
        when(pushNotification.getChannelId()).thenReturn(channelId);

        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getFilters()).thenReturn(filters);
        when(pushMessage.getNotification()).thenReturn(pushNotification);
        return pushMessage;
    }

    private long getTodayMillis() {
        return System.currentTimeMillis();
    }

    private long getPreviousDayMillis() {
        return getTodayMillis() - TimeUnit.DAYS.toMillis(1);
    }

    private long getNextDayMillis() {
        return getTodayMillis() + TimeUnit.DAYS.toMillis(1);
    }
}

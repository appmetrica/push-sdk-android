package io.appmetrica.analytics.push.impl;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class PushMessageHistoryTests {

    private Context mContext;
    private PreferenceManager mPreferenceManager;
    private PushMessageHistory mPushMessageHistory;

    @Before
    public void setUp() {
        mContext = RuntimeEnvironment.application.getApplicationContext();
        mPreferenceManager = new PreferenceManager(mContext);
        mPreferenceManager.clearAll();
        mPushMessageHistory = new PushMessageHistory(mPreferenceManager);
    }

    @Test
    public void testFirstCreatePreferenceManager() {
        assertThat(mPushMessageHistory.getPushIds()).isEmpty();
        assertThat(mPushMessageHistory.getContentIds()).isEmpty();
        assertThat(mPushMessageHistory.getShownTimesForChannelId(randomString())).isEmpty();
        assertThat(mPushMessageHistory.getLastShownTimeForChannelId(randomString())).isZero();
    }

    @Test
    public void testAddPush() {
        PushMessageHistory pushMessageHistory = new PushMessageHistory(mPreferenceManager);
        PushMessage pushMessage = mockPushMessageWithContentId(randomString());
        pushMessageHistory.addPush(pushMessage);

        assertThat(mPushMessageHistory.getPushIds()).contains(pushMessage.getNotificationId());
        assertThat(mPushMessageHistory.getContentIds()).contains(pushMessage.getFilters().getContentId());
    }

    @Test
    public void testSavePushTimestamp() {
        PushMessageHistory pushMessageHistory = new PushMessageHistory(mPreferenceManager);
        PushMessage pushMessage = mockPushMessage();
        pushMessageHistory.savePushTimestamp(pushMessage);

        assertThat(mPushMessageHistory.getShownTimesForChannelId(pushMessage.getNotification().getChannelId()))
            .contains(pushMessage.getTimestamp());
        assertThat(mPushMessageHistory.getLastShownTimeForChannelId(pushMessage.getNotification().getChannelId()))
            .isEqualTo(pushMessage.getTimestamp());
    }

    @Test
    public void testPushIdsShouldNotBeGreaterThanHistoryRecordLimit() {
        for (int i = 0; i < PushMessageHistory.HISTORY_RECORDS_LIMIT * 10; i++) {
            mPushMessageHistory.addPush(mockPushMessage());
        }
        assertThat(mPushMessageHistory.getPushIds()).hasSize(PushMessageHistory.HISTORY_RECORDS_LIMIT);
    }

    @Test
    public void testContentIdsShouldNotBeGreaterThanHistoryRecordLimit() {
        for (int i = 0; i < PushMessageHistory.HISTORY_RECORDS_LIMIT * 10; i++) {
            mPushMessageHistory.addPush(mockPushMessageWithContentId(randomString()));
        }
        assertThat(mPushMessageHistory.getContentIds()).hasSize(PushMessageHistory.HISTORY_RECORDS_LIMIT);
    }

    @Test
    public void testShownTimesShouldNotBeGreaterThanHistoryRecordLimit() {
        String channelId = randomString();
        for (int i = 0; i < PushMessageHistory.HISTORY_RECORDS_LIMIT * 10; i++) {
            mPushMessageHistory.savePushTimestamp(mockPushMessage(channelId));
        }
        assertThat(mPushMessageHistory.getShownTimesForChannelId(channelId))
            .hasSize(PushMessageHistory.HISTORY_RECORDS_LIMIT);
    }

    @Test
    public void testPushIdsShouldRemoveTheOldestElementsOnTruncate() {
        for (int i = 0; i < PushMessageHistory.HISTORY_RECORDS_LIMIT * 10; i++) {
            mPushMessageHistory.addPush(mockPushMessage("" + i, i, randomString()));
        }
        for (String pushId : mPushMessageHistory.getPushIds()) {
            assertThat(Integer.parseInt(pushId))
                .isGreaterThanOrEqualTo(PushMessageHistory.HISTORY_RECORDS_LIMIT * 9);
        }
    }

    @Test
    public void testContentIdsShouldRemoveTheOldestElementsOnTruncate() {
        for (int i = 0; i < PushMessageHistory.HISTORY_RECORDS_LIMIT * 10; i++) {
            mPushMessageHistory.addPush(mockPushMessageWithContentId("" + i, i));
        }
        for (String contentId : mPushMessageHistory.getContentIds()) {
            assertThat(Integer.parseInt(contentId))
                .isGreaterThanOrEqualTo(PushMessageHistory.HISTORY_RECORDS_LIMIT * 9);
        }
    }

    @Test
    public void testShownTimesShouldRemoveTheOldestElementsOnTruncate() {
        String channelId = randomString();
        for (int i = 0; i < PushMessageHistory.HISTORY_RECORDS_LIMIT * 10; i++) {
            mPushMessageHistory.savePushTimestamp(mockPushMessage(randomString(), i, channelId));
        }
        for (long shownTime : mPushMessageHistory.getShownTimesForChannelId(channelId)) {
            assertThat(shownTime).isGreaterThanOrEqualTo(PushMessageHistory.HISTORY_RECORDS_LIMIT * 9);
        }
    }

    @Test
    public void testLastShownTimeShouldBeEqualsLastAddedTimestamp() {
        String channelId = randomString();
        for (int i = 0; i < PushMessageHistory.HISTORY_RECORDS_LIMIT; i++) {
            mPushMessageHistory.savePushTimestamp(mockPushMessage(randomString(), i, channelId));
        }
        mPushMessageHistory.savePushTimestamp(mockPushMessage(randomString(), Integer.MAX_VALUE, channelId));
        assertThat(mPushMessageHistory.getLastShownTimeForChannelId(channelId)).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void testAddExistingPushId() {
        PushMessage pushMessage = mockPushMessage();

        mPushMessageHistory.addPush(pushMessage);
        mPushMessageHistory.addPush(pushMessage);
        assertThat(mPushMessageHistory.getPushIds()).containsExactly(pushMessage.getNotificationId());
    }

    @Test
    public void testAddExistingContentId() {
        PushMessage pushMessage = mockPushMessageWithContentId(randomString());

        mPushMessageHistory.addPush(pushMessage);
        mPushMessageHistory.addPush(pushMessage);
        assertThat(mPushMessageHistory.getContentIds()).containsExactly(pushMessage.getFilters().getContentId());
    }

    @NonNull
    private PushMessage mockPushMessage() {
        return mockPushMessage(randomString());
    }

    @NonNull
    private PushMessage mockPushMessage(@Nullable String channelId) {
        return mockPushMessage(randomString(), System.currentTimeMillis(), channelId);
    }

    @NonNull
    private PushMessage mockPushMessage(@Nullable String pushId, long timestamp, @Nullable String channelId) {
        PushNotification pushNotification = mock(PushNotification.class);
        when(pushNotification.getChannelId()).thenReturn(channelId);

        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getNotificationId()).thenReturn(pushId);
        when(pushMessage.getTimestamp()).thenReturn(timestamp);
        when(pushMessage.getNotification()).thenReturn(pushNotification);

        return pushMessage;
    }

    private PushMessage mockPushMessageWithContentId(@Nullable String contentId) {
        return mockPushMessageWithContentId(contentId, System.currentTimeMillis());
    }

    @NonNull
    private PushMessage mockPushMessageWithContentId(@Nullable String contentId, long timestamp) {
        Filters filters = mock(Filters.class);
        when(filters.getContentId()).thenReturn(contentId);

        PushMessage pushMessage = mockPushMessage(randomString(), timestamp, randomString());
        when(pushMessage.getFilters()).thenReturn(filters);

        return pushMessage;
    }
}

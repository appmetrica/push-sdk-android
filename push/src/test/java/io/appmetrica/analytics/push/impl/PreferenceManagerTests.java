package io.appmetrica.analytics.push.impl;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.test.core.app.ApplicationProvider;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class PreferenceManagerTests {

    private Context mContext;
    private PreferenceManager mPreferenceManager;

    @Before
    public void setUp() {
        mContext = RuntimeEnvironment.application.getApplicationContext();
        mPreferenceManager = new PreferenceManager(mContext);
        mPreferenceManager.clearAll();
    }

    @Test
    public void testGetPendingIntentIdReturnDefaultValueIfNotExists() {
        int defValue = new Random().nextInt();
        assertThat(mPreferenceManager.getPendingIntentId(defValue)).isEqualTo(defValue);
    }

    @Test
    public void testGetPendingIntentIdReturnSavedValue() {
        int expectedValue = new Random().nextInt();
        int defValue = Integer.MIN_VALUE;
        mPreferenceManager.savePendingIntentId(expectedValue);
        assertThat(mPreferenceManager.getPendingIntentId(defValue)).isEqualTo(expectedValue);
    }

    @Test
    public void testGetPushIdsReturnDefValueIfNotExists() {
        String defValue = new RandomStringGenerator(1000).nextString();
        assertThat(mPreferenceManager.getPushIds(defValue)).isEqualTo(defValue);
    }

    @Test
    public void testGetPushIdsReturnSavedValue() {
        String defValue = new RandomStringGenerator(1000).nextString();
        String expectedValue = new RandomStringGenerator(10000).nextString();
        mPreferenceManager.savePushIds(expectedValue);
        assertThat(mPreferenceManager.getPushIds(defValue)).isEqualTo(expectedValue);
    }

    @Test
    public void testGetContentIdsReturnDefValueIfNotExists() {
        String defValue = new RandomStringGenerator(1000).nextString();
        assertThat(mPreferenceManager.getContentIds(defValue)).isEqualTo(defValue);
    }

    @Test
    public void testGetContentIdsReturnSavedValue() {
        String defValue = new RandomStringGenerator(1000).nextString();
        String expectedValue = new RandomStringGenerator(10000).nextString();
        mPreferenceManager.saveContentIds(expectedValue);
        assertThat(mPreferenceManager.getContentIds(defValue)).isEqualTo(expectedValue);
    }

    @Test
    public void testGetShownTimesForChannelIdReturnDefValueIfNotExists() {
        String channelId = randomString();
        String defValue = randomString();
        assertThat(mPreferenceManager.getShownTimesForChannelId(channelId, defValue)).isEqualTo(defValue);
    }

    @Test
    public void testGetShownTimesForChannelIdReturnSavedValue() {
        String channelId = randomString();
        String defValue = randomString();
        String expectedValue = randomString();
        mPreferenceManager.saveShownTimesForChannelId(channelId, expectedValue);
        assertThat(mPreferenceManager.getShownTimesForChannelId(channelId, defValue)).isEqualTo(expectedValue);
        assertThat(mPreferenceManager.getShownTimesForChannelId(randomString(), defValue)).isEqualTo(defValue);
    }

    @Test
    public void testGetAppNotificationStatusReturnDefValueIfNotExists() {
        assertThat(mPreferenceManager.getAppNotificationStatus()).isNull();
    }

    @Test
    public void testGetAppNotificationStatusReturnSavedValue() {
        mPreferenceManager.saveAppNotificationStatus(true);
        assertThat(mPreferenceManager.getAppNotificationStatus()).isTrue();
        mPreferenceManager.saveAppNotificationStatus(false);
        assertThat(mPreferenceManager.getAppNotificationStatus()).isFalse();
    }

    @Test
    public void testGetNotificationChannelStatusReturnDefValueIfNotExists() {
        assertThat(mPreferenceManager.getNotificationChannelStatus(new RandomStringGenerator().nextString())).isNull();
    }

    @Test
    public void testGetNotificationChannelStatusReturnSavedValue() {
        String channelId = new RandomStringGenerator().nextString();
        mPreferenceManager.saveNotificationChannelStatus(channelId, true);
        assertThat(mPreferenceManager.getNotificationChannelStatus(channelId)).isTrue();
        mPreferenceManager.saveNotificationChannelStatus(channelId, false);
        assertThat(mPreferenceManager.getNotificationChannelStatus(channelId)).isFalse();
    }

    @Test
    public void testGetNotificationChannelGroupStatusReturnDefValueIfNotExists() {
        assertThat(mPreferenceManager.getNotificationChannelGroupStatus(new RandomStringGenerator().nextString()))
            .isNull();
    }

    @Test
    public void testGetNotificationChannelGroupStatusReturnSavedValue() {
        String groupId = new RandomStringGenerator().nextString();
        mPreferenceManager.saveNotificationChannelGroupStatus(groupId, true);
        assertThat(mPreferenceManager.getNotificationChannelGroupStatus(groupId)).isTrue();
        mPreferenceManager.saveNotificationChannelGroupStatus(groupId, false);
        assertThat(mPreferenceManager.getNotificationChannelGroupStatus(groupId)).isFalse();
    }

    @Test
    public void testGetTokensReturnDefValueIfNotExists() {
        assertThat(mPreferenceManager.getTokens()).isNull();
    }

    @Test
    public void testGetTokenReturnSavedValue() {
        String token = new RandomStringGenerator().nextString();
        mPreferenceManager.saveTokens(token);
        assertThat(mPreferenceManager.getTokens()).isEqualTo(token);
    }

    @Test
    public void getAppMetricaEventTrackerIdIfMissing() {
        assertThat(mPreferenceManager.getAppMetricaTrackerEventId("scope", -1L)).isEqualTo(-1L);
    }

    @Test
    public void saveAndGetAppMetricaTrackerEventId() {
        String scope = "scope";
        long value = 17;
        mPreferenceManager.saveAppMetricaTrackerEventId(scope, value);
        assertThat(mPreferenceManager.getAppMetricaTrackerEventId(scope, -1L)).isEqualTo(value);
    }

    @Test
    public void saveAndGetAppMetricaTrackerEventIdForDifferentScopes() {
        String scope1 = "scope#1";
        String scope2 = "scope#2";
        long value = 22L;
        mPreferenceManager.saveAppMetricaTrackerEventId(scope1, value);
        assertThat(mPreferenceManager.getAppMetricaTrackerEventId(scope1, -1)).isEqualTo(value);
        assertThat(mPreferenceManager.getAppMetricaTrackerEventId(scope2, -1)).isEqualTo(-1);
    }

    @RunWith(RobolectricTestRunner.class)
    public static class TestMigration {

        @Test
        public void updateStorageVersion() {
            Context context = ApplicationProvider.getApplicationContext();
            SharedPreferences preferences = context.getSharedPreferences("io.appmetrica.analytics.push.test.STORAGE", 0);
            assertThat(preferences.getInt("storage_version", 0)).isEqualTo(0);
            new PreferenceManager(context);
            assertThat(preferences.getInt("storage_version", 0)).isEqualTo(1);
        }

    }
}

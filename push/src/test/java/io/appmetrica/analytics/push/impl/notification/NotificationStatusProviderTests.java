package io.appmetrica.analytics.push.impl.notification;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.test.core.app.ApplicationProvider;
import io.appmetrica.analytics.push.impl.PreferenceManager;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class NotificationStatusProviderTests {

    private NotificationManager mNotificationManager;
    private NotificationManagerCompat mNotificationManagerCompat;
    private PreferenceManager mPreferenceManager;
    private NotificationStatusProvider mNotificationStatusProvider;

    @Before
    public void setUp() {
        mNotificationManager = mock(NotificationManager.class);
        mNotificationManagerCompat = mock(NotificationManagerCompat.class);
        mPreferenceManager = new PreferenceManager(ApplicationProvider.getApplicationContext());
        mPreferenceManager.clearAll();
        mNotificationStatusProvider = new NotificationStatusProvider(mNotificationManager, mNotificationManagerCompat,
            mPreferenceManager);
    }

    @Test
    public void testNotificationStatusContainsValidAppNotificationStatus() {
        when(mNotificationManagerCompat.areNotificationsEnabled()).thenReturn(true);
        assertThat(getNotificationStatus().getEnabled()).isTrue();

        when(mNotificationManagerCompat.areNotificationsEnabled()).thenReturn(false);
        assertThat(getNotificationStatus().getEnabled()).isFalse();
    }

    @Test
    public void testNotificationStatusChanged() {
        when(mNotificationManagerCompat.areNotificationsEnabled()).thenReturn(true);
        NotificationStatus notificationStatus = getNotificationStatus();
        assertThat(notificationStatus.getEnabled()).isTrue();
        assertThat(notificationStatus.getChanged()).isFalse();

        when(mNotificationManagerCompat.areNotificationsEnabled()).thenReturn(false);
        notificationStatus = getNotificationStatus();
        assertThat(notificationStatus.getEnabled()).isFalse();
        assertThat(notificationStatus.getChanged()).isTrue();

        when(mNotificationManagerCompat.areNotificationsEnabled()).thenReturn(false);
        notificationStatus = getNotificationStatus();
        assertThat(notificationStatus.getEnabled()).isFalse();
        assertThat(notificationStatus.getChanged()).isFalse();
    }

    @Test
    @Config(maxSdk = Build.VERSION_CODES.N_MR1)
    public void testNotificationStatusNotContainsGroupsOnApiLessOrEquals25() {
        assertThat(getNotificationStatus().getGroups()).hasSize(0);
    }

    @Test
    @Config(maxSdk = Build.VERSION_CODES.N_MR1)
    public void testNotificationStatusNotContainsChannelsWithoutGroupOnApiLessOrEquals25() {
        assertThat(getNotificationStatus().getChannelsWithoutGroup()).hasSize(0);
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    public void testNotificationStatusOnApiMoreOrEquals26() {
        Map<String, Boolean> groups = new HashMap<String, Boolean>();
        groups.put("group1", true);
        groups.put("group2", false);
        Map<String, Pair<String, Integer>> channels = new HashMap<String, Pair<String, Integer>>();
        channels.put(randomString(), new Pair<String, Integer>("group1", NotificationManager.IMPORTANCE_NONE));
        channels.put(randomString(), new Pair<String, Integer>("group1", NotificationManager.IMPORTANCE_DEFAULT));
        channels.put(randomString(), new Pair<String, Integer>("group2", NotificationManager.IMPORTANCE_NONE));
        channels.put(randomString(), new Pair<String, Integer>("group2", NotificationManager.IMPORTANCE_DEFAULT));
        channels.put(randomString(), new Pair<String, Integer>(null, NotificationManager.IMPORTANCE_NONE));
        channels.put(randomString(), new Pair<String, Integer>(null, NotificationManager.IMPORTANCE_DEFAULT));
        mockGroups(groups);
        mockChannels(channels);
        NotificationStatus notificationStatus = getNotificationStatus();
        assertThat(notificationStatus.getGroups()).hasSize(groups.size());
        for (NotificationStatusGroup group : notificationStatus.getGroups()) {
            assertThat(groups).containsKey(group.getId());
            assertThat(group.getChannels()).hasSize(2);
            for (NotificationStatusChannel channel : group.getChannels()) {
                assertThat(channels).containsKey(channel.getId());
                assertThat(channels.get(channel.getId()).second != NotificationManager.IMPORTANCE_NONE)
                    .isEqualTo(channel.getEnabled());
                assertThat(channels.get(channel.getId()).first).isEqualTo(group.getId());
                channels.remove(channel.getId());
            }
            groups.remove(group.getId());
        }
        assertThat(notificationStatus.getChannelsWithoutGroup()).hasSize(2);
        for (NotificationStatusChannel channel : notificationStatus.getChannelsWithoutGroup()) {
            assertThat(channels).containsKey(channel.getId());
            assertThat(channels.get(channel.getId()).second != NotificationManager.IMPORTANCE_NONE)
                .isEqualTo(channel.getEnabled());
            assertThat(channels.get(channel.getId()).first).isNull();
            channels.remove(channel.getId());
        }
    }

    @Test
    @Config(sdk = {Build.VERSION_CODES.O, Build.VERSION_CODES.O_MR1})
    public void testGroupBlockedOnApi26And27() {
        Map<String, Boolean> groups = new HashMap<String, Boolean>();
        groups.put(randomString(), true);
        groups.put(randomString(), false);
        mockGroups(groups);
        NotificationStatus notificationStatus = getNotificationStatus();
        assertThat(notificationStatus.getGroups()).hasSize(groups.size());
        for (NotificationStatusGroup group : notificationStatus.getGroups()) {
            assertThat(groups).containsKey(group.getId());
            assertThat(group.getEnabled()).isTrue();
            groups.remove(group.getId());
        }
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    public void testChannelImportanceOnApiMoreOrEquals26() {
        Map<String, Pair<String, Integer>> channels = new HashMap<String, Pair<String, Integer>>();
        channels.put(randomString(), new Pair<String, Integer>(null, NotificationManager.IMPORTANCE_NONE));
        channels.put(randomString(), new Pair<String, Integer>(null, NotificationManager.IMPORTANCE_MIN));
        channels.put(randomString(), new Pair<String, Integer>(null, NotificationManager.IMPORTANCE_LOW));
        channels.put(randomString(), new Pair<String, Integer>(null, NotificationManager.IMPORTANCE_DEFAULT));
        channels.put(randomString(), new Pair<String, Integer>(null, NotificationManager.IMPORTANCE_HIGH));
        channels.put(randomString(), new Pair<String, Integer>(null, NotificationManager.IMPORTANCE_MAX));
        mockChannels(channels);
        NotificationStatus notificationStatus = getNotificationStatus();
        assertThat(notificationStatus.getChannelsWithoutGroup()).hasSize(channels.size());
        for (NotificationStatusChannel channel : notificationStatus.getChannelsWithoutGroup()) {
            assertThat(channels).containsKey(channel.getId());
            assertThat(channels.get(channel.getId()).second != NotificationManager.IMPORTANCE_NONE)
                .isEqualTo(channel.getEnabled());
            channels.remove(channel.getId());
        }
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.P)
    public void testGroupBlockedOnApiMoreOrEquals28() {
        Map<String, Boolean> groups = new HashMap<String, Boolean>();
        groups.put(randomString(), true);
        groups.put(randomString(), false);
        mockGroups(groups);
        NotificationStatus notificationStatus = getNotificationStatus();
        assertThat(notificationStatus.getGroups()).hasSize(groups.size());
        for (NotificationStatusGroup group : notificationStatus.getGroups()) {
            assertThat(groups).containsKey(group.getId());
            assertThat(groups.get(group.getId())).isEqualTo(group.getEnabled() == false);
            groups.remove(group.getId());
        }
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.O)
    public void testNotificationChannelStatusChanged() {
        String channelId = randomString();
        Map<String, Pair<String, Integer>> channels = new HashMap<String, Pair<String, Integer>>();
        channels.put(channelId, new Pair<String, Integer>(null, NotificationManager.IMPORTANCE_DEFAULT));
        mockChannels(channels);
        NotificationStatus notificationStatus = getNotificationStatus();
        assertThat(notificationStatus.getChannelsWithoutGroup()).hasSize(1);
        assertThat(notificationStatus.getChannelsWithoutGroup().iterator().next().getChanged()).isFalse();

        channels.put(channelId, new Pair<String, Integer>(null, NotificationManager.IMPORTANCE_LOW));
        mockChannels(channels);
        notificationStatus = getNotificationStatus();
        assertThat(notificationStatus.getChannelsWithoutGroup()).hasSize(1);
        assertThat(notificationStatus.getChannelsWithoutGroup().iterator().next().getChanged()).isFalse();

        channels.put(channelId, new Pair<String, Integer>(null, NotificationManager.IMPORTANCE_NONE));
        mockChannels(channels);
        notificationStatus = getNotificationStatus();
        assertThat(notificationStatus.getChannelsWithoutGroup()).hasSize(1);
        assertThat(notificationStatus.getChannelsWithoutGroup().iterator().next().getChanged()).isTrue();
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.P)
    public void testNotificationChannelGroupStatusChannel() {
        String groupId = randomString();
        Map<String, Boolean> groups = new HashMap<String, Boolean>();
        groups.put(groupId, true);
        mockGroups(groups);
        NotificationStatus notificationStatus = getNotificationStatus();
        assertThat(notificationStatus.getGroups()).hasSize(1);
        assertThat(notificationStatus.getGroups().iterator().next().getChanged()).isFalse();

        groups.put(groupId, false);
        mockGroups(groups);
        notificationStatus = getNotificationStatus();
        assertThat(notificationStatus.getGroups()).hasSize(1);
        assertThat(notificationStatus.getGroups().iterator().next().getChanged()).isTrue();

        groups.put(groupId, false);
        mockGroups(groups);
        notificationStatus = getNotificationStatus();
        assertThat(notificationStatus.getGroups()).hasSize(1);
        assertThat(notificationStatus.getGroups().iterator().next().getChanged()).isFalse();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void mockGroups(@NonNull Map<String, Boolean> groups) {
        final List<NotificationChannelGroup> groupList = new ArrayList<NotificationChannelGroup>(groups.size());
        for (Map.Entry<String, Boolean> groupEntry : groups.entrySet()) {
            NotificationChannelGroup group = spy(new NotificationChannelGroup(groupEntry.getKey(), randomString()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                when(group.isBlocked()).thenReturn(groupEntry.getValue());
            }
            groupList.add(group);
        }
        when(mNotificationManager.getNotificationChannelGroups()).thenReturn(groupList);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void mockChannels(@NonNull Map<String, Pair<String, Integer>> channels) {
        final List<NotificationChannel> channelList = new ArrayList<NotificationChannel>(channels.size());
        for (Map.Entry<String, Pair<String, Integer>> channelEntry : channels.entrySet()) {
            NotificationChannel channel =
                new NotificationChannel(channelEntry.getKey(), randomString(), channelEntry.getValue().second);
            channel.setGroup(channelEntry.getValue().first);
            channelList.add(channel);
        }
        when(mNotificationManager.getNotificationChannels()).thenReturn(channelList);
    }

    @NonNull
    private NotificationStatus getNotificationStatus() {
        return mNotificationStatusProvider.getNotificationStatus();
    }

    @NonNull
    private String randomString() {
        return new RandomStringGenerator().nextString();
    }
}

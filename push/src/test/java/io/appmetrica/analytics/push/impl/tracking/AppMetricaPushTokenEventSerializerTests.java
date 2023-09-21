package io.appmetrica.analytics.push.impl.tracking;

import io.appmetrica.analytics.push.impl.notification.NotificationStatus;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.impl.tracking.AppMetricaPushTokenEventSerializer.JsonKeys;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaPushTokenEventSerializerTests {

    private AppMetricaPushTokenEventSerializer serializer;

    @Before
    public void setUp() {
        serializer = new AppMetricaPushTokenEventSerializer();
    }

    @Test
    public void testResultShouldContainsPushTokenIfHeNotEmpty() throws JSONException {
        final String pushToken = randomString();
        JSONObject result = new JSONObject(serializer.toJson(pushToken, new NotificationStatus(true, false)));
        assertThat(result.getString(JsonKeys.TOKEN_KEY)).isEqualTo(pushToken);
    }

    @Test
    public void testResultShouldContainsPushTokenIfHeIsEmpty() throws JSONException {
        final String pushToken = "";
        JSONObject result = new JSONObject(serializer.toJson(pushToken, new NotificationStatus(true, false)));
        assertThat(result.getString(JsonKeys.TOKEN_KEY)).isEqualTo(pushToken);
    }

    @Test
    public void testResultShouldNotContainsPushTokenIfHeIsNull() throws JSONException {
        final String pushToken = null;
        JSONObject result = new JSONObject(serializer.toJson(pushToken, new NotificationStatus(true, false)));
        assertThat(result.has(JsonKeys.TOKEN_KEY)).isFalse();
    }

    @Test
    public void testResultShouldContainsNotificationStatus() throws JSONException {
        JSONObject result = new JSONObject(serializer.toJson(null, new NotificationStatus(true, false)));
        assertThat(result.has(JsonKeys.NOTIFICATION_STATUS_KEY)).isTrue();
    }

    @Test
    public void testResultShouldContainsValidNotificationsStatus() throws JSONException {
        JSONObject result = new JSONObject(serializer.toJson(null, new NotificationStatus(true, false)));
        assertThat(
            result.getJSONObject(JsonKeys.NOTIFICATION_STATUS_KEY).getBoolean(JsonKeys.NotificationStatus.ENABLED_KEY)
        ).isTrue();

        result = new JSONObject(serializer.toJson(null, new NotificationStatus(false, true)));
        assertThat(
            result.getJSONObject(JsonKeys.NOTIFICATION_STATUS_KEY).getBoolean(JsonKeys.NotificationStatus.ENABLED_KEY)
        ).isFalse();
    }

    @Test
    public void testResultShouldContainsValidNotificationsStatusChanged() throws JSONException {
        JSONObject result = new JSONObject(serializer.toJson(null, new NotificationStatus(true, false)));
        assertThat(
            result.getJSONObject(JsonKeys.NOTIFICATION_STATUS_KEY).has(JsonKeys.NotificationStatus.CHANGED_KEY)
        ).isFalse();

        result = new JSONObject(serializer.toJson(null, new NotificationStatus(false, true)));
        assertThat(
            result.getJSONObject(JsonKeys.NOTIFICATION_STATUS_KEY).has(JsonKeys.NotificationStatus.CHANGED_KEY)
        ).isTrue();
    }

    @Test
    public void testResultShouldNotContainsGroupsIfGroupListIsEmpty() throws JSONException {
        final NotificationStatus notificationStatus =
            new NotificationStatus(emptyGroupSet(), emptyChannelSet(), true, false);
        JSONObject result = new JSONObject(serializer.toJson(null, notificationStatus));
        JSONObject notificationStatusJson = result.getJSONObject(JsonKeys.NOTIFICATION_STATUS_KEY);

        assertThat(notificationStatusJson.has(JsonKeys.NotificationStatus.GROUPS_KEY)).isFalse();
    }

    @Test
    public void testResultShouldContainsGroupsIfGroupsListIsNotEmpty() throws JSONException {
        final Set<NotificationStatus.Group> groups = emptyGroupSet();
        groups.add(new NotificationStatus.Group(randomString(), emptyChannelSet(), true, false));
        groups.add(new NotificationStatus.Group(randomString(), emptyChannelSet(), true, false));
        final NotificationStatus notificationStatus =
            new NotificationStatus(groups, emptyChannelSet(), true, false);
        JSONObject result = new JSONObject(serializer.toJson(null, notificationStatus));
        JSONObject notificationStatusJson = result.getJSONObject(JsonKeys.NOTIFICATION_STATUS_KEY);

        assertThat(notificationStatusJson.getJSONObject(JsonKeys.NotificationStatus.GROUPS_KEY).length())
            .isEqualTo(groups.size());
    }

    @Test
    public void testResultShouldContainsGroupWithValidStatus() throws JSONException {
        final Set<NotificationStatus.Group> groups = emptyGroupSet();
        groups.add(new NotificationStatus.Group(randomString(), emptyChannelSet(), true, false));
        groups.add(new NotificationStatus.Group(randomString(), emptyChannelSet(), false, true));
        final NotificationStatus notificationStatus =
            new NotificationStatus(groups, emptyChannelSet(), true, false);
        JSONObject result = new JSONObject(serializer.toJson(null, notificationStatus));
        JSONObject groupsJson = result
            .getJSONObject(JsonKeys.NOTIFICATION_STATUS_KEY)
            .getJSONObject(JsonKeys.NotificationStatus.GROUPS_KEY);

        assertThat(groupsJson.length()).isEqualTo(groups.size());
        for (NotificationStatus.Group group : groups) {
            assertThat(
                groupsJson.getJSONObject(group.id).getBoolean(JsonKeys.NotificationStatus.Group.ENABLED_KEY)
            ).isEqualTo(group.enabled);
            assertThat(
                groupsJson.getJSONObject(group.id).has(JsonKeys.NotificationStatus.Group.CHANGED_KEY)
            ).isEqualTo(group.changed);
        }
    }

    @Test
    public void testResultShouldContainsGroupWithValidChannels() throws JSONException {
        final String groupId = randomString();
        final Set<NotificationStatus.Group> groups = emptyGroupSet();
        final Set<NotificationStatus.Channel> channels = emptyChannelSet();
        channels.add(new NotificationStatus.Channel(randomString(), true, false));
        channels.add(new NotificationStatus.Channel(randomString(), false, true));
        groups.add(new NotificationStatus.Group(groupId, channels, true, false));
        final NotificationStatus notificationStatus =
            new NotificationStatus(groups, emptyChannelSet(), true, false);
        JSONObject result = new JSONObject(serializer.toJson(null, notificationStatus));
        JSONObject channelsJson = result
            .getJSONObject(JsonKeys.NOTIFICATION_STATUS_KEY)
            .getJSONObject(JsonKeys.NotificationStatus.GROUPS_KEY)
            .getJSONObject(groupId)
            .getJSONObject(JsonKeys.NotificationStatus.Group.CHANNELS_KEY);

        assertThat(channelsJson.length()).isEqualTo(channels.size());
        for (NotificationStatus.Channel channel : channels) {
            assertThat(
                channelsJson.getJSONObject(channel.id).getBoolean(JsonKeys.NotificationStatus.Channel.ENABLED_KEY)
            ).isEqualTo(channel.enabled);

            assertThat(
                channelsJson.getJSONObject(channel.id).has(JsonKeys.NotificationStatus.Channel.CHANGED_KEY)
            ).isEqualTo(channel.changed);
        }
    }

    @Test
    public void testResultShouldContainsChannelsWithoutGroupWithValidChannels() throws JSONException {
        final Set<NotificationStatus.Channel> channels = emptyChannelSet();
        channels.add(new NotificationStatus.Channel(randomString(), true, false));
        channels.add(new NotificationStatus.Channel(randomString(), false, true));
        final NotificationStatus notificationStatus =
            new NotificationStatus(emptyGroupSet(), channels, true, false);
        JSONObject result = new JSONObject(serializer.toJson(null, notificationStatus));
        JSONObject channelsJson = result
            .getJSONObject(JsonKeys.NOTIFICATION_STATUS_KEY)
            .getJSONObject(JsonKeys.NotificationStatus.CHANNELS_WITHOUT_GROUP_KEY);

        assertThat(channelsJson.length()).isEqualTo(channels.size());
        for (NotificationStatus.Channel channel : channels) {
            assertThat(
                channelsJson.getJSONObject(channel.id).getBoolean(JsonKeys.NotificationStatus.Channel.ENABLED_KEY)
            ).isEqualTo(channel.enabled);
            assertThat(
                channelsJson.getJSONObject(channel.id).has(JsonKeys.NotificationStatus.Channel.CHANGED_KEY)
            ).isEqualTo(channel.changed);
        }
    }

    private Set<NotificationStatus.Channel> emptyChannelSet() {
        return new HashSet<NotificationStatus.Channel>();
    }

    private Set<NotificationStatus.Group> emptyGroupSet() {
        return new HashSet<NotificationStatus.Group>();
    }

    private String randomString() {
        return new RandomStringGenerator(new Random().nextInt(100) + 1).nextString();
    }
}

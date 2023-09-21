package io.appmetrica.analytics.push.intent;

import android.os.Bundle;

import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class NotificationActionInfoTests {

    private static final String TRANSPORT = "push_transport";

    private NotificationActionInfo.Builder builder;

    @Before
    public void setUp() {
        builder = NotificationActionInfo.newBuilder(TRANSPORT);
    }

    @Test
    public void testTransport() {
        assertThat(builder.build().transport).isEqualTo(TRANSPORT);
    }

    @Test
    public void testPushId() {
        final String pushId = randomString();
        assertThat(builder.withPushId(pushId).build().pushId).isEqualTo(pushId);
    }

    @Test
    public void testDefaultPushId() {
        assertThat(builder.build().pushId).isNull();
    }

    @Test
    public void testTargetAction() {
        final String targetAction = randomString();
        assertThat(builder.withTargetActionUri(targetAction).build().targetActionUri).isEqualTo(targetAction);
    }

    @Test
    public void testDefaultTargetAction() {
        assertThat(builder.build().targetActionUri).isNull();
    }

    @Test
    public void testPayload() {
        final String payload = randomString();
        assertThat(builder.withPayload(payload).build().payload).isEqualTo(payload);
    }

    @Test
    public void testDefaultPayload() {
        assertThat(builder.build().payload).isNull();
    }

    @Test
    public void testActionType() {
        final NotificationActionType actionType = NotificationActionType.CLICK;
        assertThat(builder.withActionType(actionType).build().actionType).isEqualTo(actionType);
    }

    @Test
    public void testDefaultActionType() {
        assertThat(builder.build().actionType).isNull();
    }

    @Test
    public void testActionId() {
        final String actionId = randomString();
        assertThat(builder.withActionId(actionId).build().actionId).isEqualTo(actionId);
    }

    @Test
    public void testDefaultActionId() {
        assertThat(builder.build().actionId).isNull();
    }

    @Test
    public void testNotificationTag() {
        final String notificationTag = randomString();
        assertThat(builder.withNotificationTag(notificationTag).build().notificationTag).isEqualTo(notificationTag);
    }

    @Test
    public void testDefaultNotificationTag() {
        assertThat(builder.build().notificationTag).isNull();
    }

    @Test
    public void testNotificationId() {
        final int notificationId = new Random().nextInt();
        assertThat(builder.withNotificationId(notificationId).build().notificationId).isEqualTo(notificationId);
    }

    @Test
    public void testDefaultNotificationId() {
        assertThat(builder.build().notificationId).isZero();
    }

    @Test
    public void testHideQuickControlPanel() {
        assertThat(builder.withHideQuickControlPanel(true).build().hideQuickControlPanel).isTrue();
        assertThat(builder.withHideQuickControlPanel(false).build().hideQuickControlPanel).isFalse();
    }

    @Test
    public void testDefaultHideQuickControlPanel() {
        assertThat(builder.build().hideQuickControlPanel).isFalse();
    }

    @Test
    public void testDismissOnAdditionalAction() {
        assertThat(builder.withDismissOnAdditionalAction(true).build().dismissOnAdditionalAction).isTrue();
        assertThat(builder.withDismissOnAdditionalAction(false).build().dismissOnAdditionalAction).isFalse();
    }

    @Test
    public void testDefaultDismissOnAdditionalAction() {
        assertThat(builder.build().dismissOnAdditionalAction).isFalse();
    }

    @Test
    public void testDoNothing() {
        assertThat(builder.withDoNothing(true).build().doNothing).isTrue();
        assertThat(builder.withDoNothing(false).build().doNothing).isFalse();
    }

    @Test
    public void testDefaultDoNothing() {
        assertThat(builder.build().doNothing).isFalse();
    }

    @Test
    public void testUseFlagActivityNewTask() {
        assertThat(builder.withUseFlagActivityNewTask(true).build().useFlagActivityNewTask).isTrue();
        assertThat(builder.withUseFlagActivityNewTask(false).build().useFlagActivityNewTask).isFalse();
    }

    @Test
    public void testDefaultUseFlagActivityNewTask() {
        assertThat(builder.build().useFlagActivityNewTask).isFalse();
    }

    @Test
    public void testCustomBundle() {
        String key1 = "key1";
        String value1 = "value1";
        Bundle bundle = new Bundle();
        bundle.putString(key1, value1);
        Bundle res = builder.withExtraBundle(bundle).build().extraBundle;
        assertThat(res.getString(key1)).isEqualTo(value1);

        String key2 = "key2";
        String value2 = "value2";
        bundle.putString(key2, value2);
        assertThat(res.containsKey(key2)).isFalse();
    }

    @Test
    public void testDefaultCustomIntent() {
        assertThat(builder.build().extraBundle).isNull();
    }

    @Test
    public void testChannelId() {
        final String channelId = randomString();
        assertThat(builder.withChannelId(channelId).build().channelId).isEqualTo(channelId);
    }

    @Test
    public void testDefaultChannelId() {
        assertThat(builder.build().channelId).isNull();
    }

    @Test
    public void testHideAfterSeconds() {
        final long value = new Random().nextLong();
        assertThat(builder.withHideAfterSeconds(value).build().hideAfterSeconds).isEqualTo(value);
    }

    private String randomString() {
        return new RandomStringGenerator(new Random().nextInt(100) + 1).nextString();
    }
}

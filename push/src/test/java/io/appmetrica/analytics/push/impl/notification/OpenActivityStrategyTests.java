package io.appmetrica.analytics.push.impl.notification;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.MockablePushServiceProvider;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class OpenActivityStrategyTests {

    private static final String TRANSPORT = "push_transport";

    @Rule
    public final MockedStaticRule<PushMessageTrackerHub> sPushMessageTrackerHub =
        new MockedStaticRule<>(PushMessageTrackerHub.class);
    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);

    protected Context mContext;
    protected AppMetricaPushCore mPushCore;
    protected PushMessageTrackerHub mPushMessageTracker;
    protected TrackersHub trackersHub;
    protected MockablePushServiceProvider mPushServiceProvider;
    protected NotificationActionInfo.Builder mNotificationActionInfoBuilder;

    @Before
    public void setUp() {
        mContext = spy(RuntimeEnvironment.application.getApplicationContext());
        doNothing().when(mContext).startActivity(any(Intent.class));
        mPushMessageTracker = mock(PushMessageTrackerHub.class);
        when(PushMessageTrackerHub.getInstance()).thenReturn(mPushMessageTracker);
        trackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(trackersHub);
        mPushCore = AppMetricaPushCore.getInstance(mContext);
        mPushServiceProvider = new MockablePushServiceProvider();
        mPushCore.setPushServiceProvider(mPushServiceProvider);
        mNotificationActionInfoBuilder = NotificationActionInfo.newBuilder(TRANSPORT);
    }

    protected String randomString() {
        return new RandomStringGenerator().nextString();
    }

    @Test
    public void testDoActionShouldStartActivity() {
        getStrategy().doAction(
            mContext,
            wrapToIntent(mNotificationActionInfoBuilder.withTargetActionUri("https://play.google.com/").build())
        );
        verify(mContext, times(1)).startActivity(any(Intent.class));
    }

    @Test
    public void testDoActionShouldStartActionIfActionDefined() {
        String action = "https://play.google.com/";
        getStrategy().doAction(
            mContext,
            wrapToIntent(mNotificationActionInfoBuilder.withTargetActionUri(action).build())
        );
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mContext, times(1)).startActivity(arg.capture());
        assertThat(arg.getValue().getData()).isEqualTo(Uri.parse(action));
    }

    @Test
    public void testDoActionShouldStartActionViewIfActionDefined() {
        getStrategy().doAction(
            mContext,
            wrapToIntent(mNotificationActionInfoBuilder.withTargetActionUri("https://play.google.com/").build())
        );
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mContext, times(1)).startActivity(arg.capture());
        assertThat(arg.getValue().getAction()).isEqualTo(Intent.ACTION_VIEW);
    }

    @Test
    public void testDoActionShouldAddPayloadToIntentIfActionDefined() {
        String payload = randomString();
        NotificationActionInfo actionInfo = mNotificationActionInfoBuilder
            .withPayload(payload)
            .withTargetActionUri("https://play.google.com/")
            .build();
        getStrategy().doAction(mContext, wrapToIntent(actionInfo));
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mContext, times(1)).startActivity(arg.capture());
        assertThat(arg.getValue().getStringExtra(AppMetricaPush.EXTRA_PAYLOAD)).isEqualTo(payload);
    }

    @Test
    public void testDoActionShouldAddCustomIntentExtrasToIntentIfActionDefined() {
        String key = "key";
        String value = "value";
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        NotificationActionInfo actionInfo = mNotificationActionInfoBuilder
            .withExtraBundle(bundle)
            .withTargetActionUri("https://play.google.com/")
            .build();
        getStrategy().doAction(mContext, wrapToIntent(actionInfo));
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mContext, times(1)).startActivity(arg.capture());
        assertThat(arg.getValue().getStringExtra(key)).isEqualTo(value);
    }

    @Test
    public void testDoActionShouldAddPackageNameToIntentIfActionDefined() {
        NotificationActionInfo actionInfo = mNotificationActionInfoBuilder
            .withExplicitIntent(true)
            .withTargetActionUri("https://play.google.com/")
            .build();
        getStrategy().doAction(mContext, wrapToIntent(actionInfo));
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mContext, times(1)).startActivity(arg.capture());
        assertThat(arg.getValue().getPackage()).isEqualTo(mContext.getPackageName());
    }

    protected abstract OpenActivityStrategy getStrategy();

    private Intent wrapToIntent(final NotificationActionInfo info) {
        return new Intent().putExtra(AppMetricaPush.EXTRA_ACTION_INFO, info);
    }
}

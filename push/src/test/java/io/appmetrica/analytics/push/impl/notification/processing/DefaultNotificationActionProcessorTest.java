package io.appmetrica.analytics.push.impl.notification.processing;

import android.content.Context;
import android.content.Intent;
import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.intent.NotificationActionType;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import java.util.AbstractMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static io.appmetrica.analytics.push.testutils.Rand.randomInt;
import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class DefaultNotificationActionProcessorTest {

    private static final String TRANSPORT = "push_transport";

    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);

    private DefaultNotificationActionProcessor processor;
    private Context context;
    private TrackersHub trackersHub;
    private NotificationActionInfo.Builder notificationActionInfoBuilder;
    private NotificationActionProcessingStrategy strategy;

    @Before
    public void setUp() {
        processor = new DefaultNotificationActionProcessor();
        context = spy(RuntimeEnvironment.application.getApplicationContext());
        trackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(trackersHub);
        strategy = mock(NotificationActionProcessingStrategy.class);
        notificationActionInfoBuilder = NotificationActionInfo.newBuilder(TRANSPORT);
    }

    @Test
    public void testProcessActionReportToTrackersHubIfNoActionInfo() {
        processor.processAction(context, new Intent());
        verify(trackersHub, times(1)).reportEvent("No action info for DefaultNotificationActionProcessor");
    }

    @Test
    public void testProcessActionReportToTrackersHubIfNoStrategy() {
        final NotificationActionInfo info = notificationActionInfoBuilder
            .withPushId(randomString())
            .withActionType(NotificationActionType.CLICK)
            .withActionId(randomString())
            .withNotificationId(randomInt())
            .withNotificationTag(randomString())
            .build();
        processor.processAction(context, wrapToIntent(info));
        ArgumentCaptor<Map> arg = ArgumentCaptor.forClass(Map.class);
        verify(trackersHub, times(1)).reportEvent(eq("No strategy"), arg.capture());
        assertThat(arg.getValue()).contains(
            new AbstractMap.SimpleEntry<String, Object>("actionType", info.actionType),
            new AbstractMap.SimpleEntry<String, Object>("pushId", info.pushId)
        );
    }

    @Test
    public void testProcessActionCallsStrategy() {
        final Intent intent = wrapToIntent(notificationActionInfoBuilder
            .withPushId(randomString())
            .withActionType(NotificationActionType.CLICK)
            .build()
        );
        processor.setOpenActionProcessingStrategy(strategy);
        processor.processAction(context, intent);
        verify(strategy, times(1)).doAction(context, intent);
    }

    private Intent wrapToIntent(final NotificationActionInfo info) {
        return new Intent().putExtra(AppMetricaPush.EXTRA_ACTION_INFO, info);
    }
}

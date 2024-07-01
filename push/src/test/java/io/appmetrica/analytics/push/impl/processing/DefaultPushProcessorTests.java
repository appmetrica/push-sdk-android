package io.appmetrica.analytics.push.impl.processing;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import io.appmetrica.analytics.push.MockablePushServiceProvider;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.impl.PushServiceProvider;
import io.appmetrica.analytics.push.impl.processing.strategy.PushStrategy;
import io.appmetrica.analytics.push.impl.processing.transform.filter.PushFilterFacade;
import io.appmetrica.analytics.push.impl.tracking.PushMessageTrackerHub;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.AutoTrackingConfiguration;
import io.appmetrica.analytics.push.settings.PushFilter;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class DefaultPushProcessorTests {

    @Rule
    public final MockedStaticRule<PushMessageTrackerHub> sPushMessageTrackerHub =
        new MockedStaticRule<>(PushMessageTrackerHub.class);

    private Context mContext;
    private DefaultPushProcessor mPushProcessor;
    private PushStrategy mPushStrategy;
    private PushProcessingStrategyProvider mPushProcessingStrategyProvider;
    private PushMessageTrackerHub mTracker;
    private Bundle mBundle;
    private PushFilterFacade mPushFilterFacade;
    private PushMessageHistory mPushMessageHistory;

    @Before
    public void setUp() {
        mContext = RuntimeEnvironment.application.getApplicationContext();
        mPushProcessor = new DefaultPushProcessor();
        mTracker = mock(PushMessageTrackerHub.class);
        when(PushMessageTrackerHub.getInstance()).thenReturn(mTracker);
        final AppMetricaPushCore pushCore = AppMetricaPushCore.getInstance(mContext);
        final PushServiceProvider pushServiceProvider = new MockablePushServiceProvider();
        pushCore.setPushServiceProvider(pushServiceProvider);
        pushServiceProvider.setAutoTrackingConfiguration(AutoTrackingConfiguration.newBuilder().build());

        mPushFilterFacade = mock(PushFilterFacade.class);
        pushCore.setPushFilterFacade(mPushFilterFacade);
        when(mPushFilterFacade.filter(any(PushMessage.class))).thenReturn(PushFilter.FilterResult.show());

        mPushStrategy = mock(PushStrategy.class);
        mPushProcessingStrategyProvider = pushServiceProvider.getPushProcessingStrategyProvider();
        when(mPushProcessingStrategyProvider.getPushStrategy(any(PushMessage.class))).thenReturn(mPushStrategy);
        mBundle = new Bundle();
        mPushMessageHistory = mock(PushMessageHistory.class);
        pushCore.setPushMessageHistory(mPushMessageHistory);
    }

    @Test
    public void testProcessPushShouldNotDoAnythingIfBundleIsEmpty() {
        mPushProcessor.processPush(mContext, null);
        verify(mPushStrategy, never()).processPush(any(Context.class), any(PushMessage.class));
        verify(mPushMessageHistory, never()).addPush(any(PushMessage.class));
    }

    @Test
    public void testProcessPushShouldNotDoAnythingIfRootPushElementDoesNotExist() {
        mPushProcessor.processPush(mContext, new Bundle());
        verify(mPushStrategy, never()).processPush(any(Context.class), any(PushMessage.class));
        verify(mPushMessageHistory, never()).addPush(any(PushMessage.class));
    }

    @Test
    public void testProcessPushShouldOnlyReportIgnoredIfFilterResultIsNotShow() throws Exception {
        String category = randomString();
        String details = randomString();
        when(mPushFilterFacade.filter(any(PushMessage.class)))
            .thenReturn(PushFilter.FilterResult.silence(category, details));
        addPushValuesToBundle(Constants.PushMessage.NOTIFICATION_ID, randomString());
        mPushProcessor.processPush(mContext, mBundle);
        verify(mPushStrategy, never()).processPush(any(Context.class), any(PushMessage.class));
        verify(mTracker, times(1))
            .onNotificationIgnored(anyString(), eq(category), eq(details), nullable(String.class), anyString());
        verify(mPushMessageHistory, times(1)).addPush(any(PushMessage.class));
    }

    @Test
    public void testProcessPushShouldReportOnReceiveMessageToTracker() throws Exception {
        addPushValuesToBundle(Constants.PushMessage.NOTIFICATION_ID, new RandomStringGenerator().nextString());
        mPushProcessor.processPush(mContext, mBundle);
        verify(mTracker, times(1))
            .onMessageReceived(anyString(), nullable(String.class), anyString());
        verify(mPushStrategy, times(1)).processPush(eq(mContext), any(PushMessage.class));
        verify(mPushMessageHistory, times(1)).addPush(any(PushMessage.class));
    }

    @Test
    public void testProcessPushShouldNotReportOnReceiveMessageToTrackerIfPushIdIsNull() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PushMessage.NOTIFICATION_ID, null);
        mPushProcessor.processPush(mContext, bundle);
        verify(mTracker, never()).onMessageReceived(anyString(), anyString(), anyString());
        verify(mPushStrategy, never()).processPush(any(Context.class), any(PushMessage.class));
        verify(mPushMessageHistory, never()).addPush(any(PushMessage.class));
    }

    @Test
    public void testProcessPushShouldNotReportOnReceiveMessageToTrackerIfPushIdIsEmpty() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PushMessage.NOTIFICATION_ID, "");
        mPushProcessor.processPush(mContext, bundle);
        verify(mTracker, never()).onMessageReceived(anyString(), anyString(), anyString());
        verify(mPushStrategy, never()).processPush(any(Context.class), any(PushMessage.class));
        verify(mPushMessageHistory, never()).addPush(any(PushMessage.class));
    }

    @Test
    public void testProcessPushShouldSendPushIdToReportOnReceiveMessageToTracker() throws Exception {
        String notificationId = new RandomStringGenerator().nextString();
        addPushValuesToBundle(Constants.PushMessage.NOTIFICATION_ID, notificationId);
        mPushProcessor.processPush(mContext, mBundle);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mTracker, times(1))
            .onMessageReceived(arg.capture(), nullable(String.class), anyString());
        assertThat(arg.getValue()).isEqualTo(notificationId);
        verify(mPushStrategy, times(1)).processPush(eq(mContext), any(PushMessage.class));
        verify(mPushMessageHistory, times(1)).addPush(any(PushMessage.class));
    }

    @Test
    public void testProcessPushShouldCallProcessPush() throws Exception {
        addPushValuesToBundle(Constants.PushMessage.NOTIFICATION_ID, new RandomStringGenerator().nextString());
        mPushProcessor.processPush(mContext, mBundle);
        verify(mPushStrategy, times(1)).processPush(any(Context.class), any(PushMessage.class));
        verify(mPushStrategy, times(1)).processPush(eq(mContext), any(PushMessage.class));
        verify(mPushMessageHistory, times(1)).addPush(any(PushMessage.class));
    }

    @Test
    public void testProcessPushShouldReportOnNotificationIgnoredIfStrategyIsNull() throws Exception {
        when(mPushProcessingStrategyProvider.getPushStrategy(any(PushMessage.class))).thenReturn(null);
        addPushValuesToBundle(Constants.PushMessage.NOTIFICATION_ID, new RandomStringGenerator().nextString());
        mPushProcessor.processPush(mContext, mBundle);
        verify(mTracker, times(1)).onNotificationIgnored(
            anyString(),
            eq(Constants.IGNORED_CATEGORY_PUSH_DATA_FORMAT_IS_INVALID),
            anyString(),
            nullable(String.class),
            anyString()
        );
        verify(mPushMessageHistory, times(1)).addPush(any(PushMessage.class));
        verify(mPushStrategy, never()).processPush(any(Context.class), any(PushMessage.class));
    }

    private void addPushValuesToBundle(String key, Object value) throws Exception {
        String rootString = mBundle.getString(CoreConstants.PushMessage.ROOT_ELEMENT);
        JSONObject rootJson = TextUtils.isEmpty(rootString) ? new JSONObject() : new JSONObject(rootString);
        rootJson.put(key, value);
        mBundle.putString(CoreConstants.PushMessage.ROOT_ELEMENT, rootJson.toString());
    }
}

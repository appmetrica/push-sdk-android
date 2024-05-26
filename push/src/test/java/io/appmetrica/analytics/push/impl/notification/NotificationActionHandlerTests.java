package io.appmetrica.analytics.push.impl.notification;

import android.content.Context;
import android.content.Intent;

import io.appmetrica.analytics.push.AppMetricaPush;
import io.appmetrica.analytics.push.MockablePushServiceProvider;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PushServiceProvider;
import io.appmetrica.analytics.push.impl.notification.processing.NotificationActionProcessor;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.intent.NotificationActionType;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class NotificationActionHandlerTests {
    private Context mContext;
    private Intent mIntent;
    private NotificationActionProcessor mNotificationActionProcessor;
    private NotificationActionHandler mActionHandler;
    private AppMetricaPushCore mAppMetricaPushCore;
    private PushServiceProvider mAppMetricaPushServiceProvider;

    @Before
    public void setUp() {
        mContext = RuntimeEnvironment.application.getApplicationContext();
        mIntent = new Intent();
        mActionHandler = new NotificationActionHandler();
        mAppMetricaPushCore = AppMetricaPushCore.getInstance(mContext);
        mAppMetricaPushServiceProvider = new MockablePushServiceProvider();
        mAppMetricaPushCore.setPushServiceProvider(mAppMetricaPushServiceProvider);
        mNotificationActionProcessor = mAppMetricaPushServiceProvider.getNotificationActionProcessor();
    }

    @Test
    public void testOnNotificationActionShouldSendActionToNotificationActionProcessor() {
        mActionHandler.onNotificationAction(mContext, mIntent);
        verify(mNotificationActionProcessor, times(1)).processAction(any(Context.class), any(Intent.class));
    }

    @Test
    public void testOnNotificationActionShouldSendPushMessageIdToNotificationActionProcessor() {
        NotificationActionInfo actionInfo = NotificationActionInfo.newBuilder("")
            .withPushId(randomString())
            .withPayload(randomString())
            .withActionType(NotificationActionType.CLICK)
            .withActionId(randomString())
            .withTargetActionUri(randomString())
            .withHideQuickControlPanel(true)
            .withDismissOnAdditionalAction(true)
            .withNotificationId(new Random().nextInt())
            .build();
        mIntent.putExtra(AppMetricaPush.EXTRA_ACTION_INFO, actionInfo);
        mActionHandler.onNotificationAction(mContext, mIntent);
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mNotificationActionProcessor, times(1)).processAction(any(Context.class), arg.capture());
        // Cast as https://joel-costigliola.github.io/assertj/assertj-core.html#ambiguous-compilation-error
        assertThat((NotificationActionInfo) arg.getValue().getParcelableExtra(AppMetricaPush.EXTRA_ACTION_INFO))
            .isEqualToComparingFieldByField(actionInfo);
    }

    private String randomString() {
        return new RandomStringGenerator().nextString();
    }
}

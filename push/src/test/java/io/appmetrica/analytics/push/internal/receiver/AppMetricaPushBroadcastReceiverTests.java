package io.appmetrica.analytics.push.internal.receiver;

import android.content.Context;
import android.content.Intent;
import io.appmetrica.analytics.push.MockablePushServiceProvider;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PushServiceProvider;
import io.appmetrica.analytics.push.impl.notification.NotificationActionListener;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaPushBroadcastReceiverTests {
    private Context mContext;
    private NotificationActionListener mNotificationActionListener;
    private AppMetricaPushBroadcastReceiver mReceiver;
    private AppMetricaPushCore mPushCore;
    private PushServiceProvider mPushServiceProvider;

    @Before
    public void setUp() {
        mContext = RuntimeEnvironment.application.getApplicationContext();
        mPushServiceProvider = new MockablePushServiceProvider();
        mPushCore = AppMetricaPushCore.getInstance(mContext);
        mPushCore.setPushServiceProvider(mPushServiceProvider);
        mNotificationActionListener = mPushServiceProvider.getNotificationActionListener();
        mReceiver = new AppMetricaPushBroadcastReceiver();
    }

    @Test
    public void testOnReceiveShouldSendIntentToNotificationActionListenerIfActionIsNotificationAction() {
        Intent intent = new Intent();
        intent.setAction(AppMetricaPushBroadcastReceiver.ACTION_BROADCAST_ACTION);
        mReceiver.onReceive(mContext, intent);
        ArgumentCaptor<Intent> arg = ArgumentCaptor.forClass(Intent.class);
        verify(mNotificationActionListener, times(1)).onNotificationAction(any(Context.class), arg.capture());
        assertThat(arg.getValue()).isEqualTo(intent);
    }

    @Test
    public void testOnReceiveShouldNotSendIntentToNotificationActionListenerIfActionIfNotNotificationAction() {
        Intent intent = new Intent();
        intent.setAction(new RandomStringGenerator().nextString());
        mReceiver.onReceive(mContext, intent);
        verify(mNotificationActionListener, never()).onNotificationAction(any(Context.class), any(Intent.class));
    }
}

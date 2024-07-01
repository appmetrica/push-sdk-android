package io.appmetrica.analytics.push.internal.receiver;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import io.appmetrica.analytics.push.MockablePushServiceProvider;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.Constants;
import io.appmetrica.analytics.push.impl.PushMessageHistory;
import io.appmetrica.analytics.push.impl.PushServiceProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomInt;
import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class TtlBroadcastReceiverTests {

    private static final String NOTIFICATION_TAG = randomString();
    private static final int NOTIFICATION_ID = randomInt();

    private Context mContext;
    private NotificationManager mNotificationManager;
    private TtlBroadcastReceiver mReceiver;
    private PushMessageHistory mPushMessageHistory;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        when(mContext.getApplicationContext()).thenReturn(mContext);
        mNotificationManager = mock(NotificationManager.class);
        when(mContext.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(mNotificationManager);
        PushServiceProvider pushServiceProvider = new MockablePushServiceProvider();
        AppMetricaPushCore.getInstance(mContext).setPushServiceProvider(pushServiceProvider);
        mPushMessageHistory = pushServiceProvider.getPushMessageHistory();

        mReceiver = new TtlBroadcastReceiver();
    }

    @Test
    public void testOnReceiveDoesNothingIfWrongAction() {
        Intent intent = new Intent();
        intent.setAction(randomString());

        mReceiver.onReceive(mContext, intent);
        verifyNoInteractions(mNotificationManager);
        verifyNoInteractions(mPushMessageHistory);
    }

    @Test
    public void testOnReceiveShouldNotCancelNotificationIfNoExtras() {
        Intent intent = createIntentForTtlBroadcastReceiver();

        mReceiver.onReceive(mContext, intent);
        verify(mNotificationManager, never()).cancel(anyString(), anyInt());
    }

    @Test
    public void testOnReceiveShouldCancelNotificationWithId0IfNoId() {
        Intent intent = createIntentForTtlBroadcastReceiver();
        intent.putExtra(Constants.NOTIFICATION_TAG, NOTIFICATION_TAG);

        mReceiver.onReceive(mContext, intent);
        verify(mNotificationManager, times(1)).cancel(eq(NOTIFICATION_TAG), eq(0));
    }

    @Test
    public void testOnReceiveShouldCancelNotificationTagNullIfNoTag() {
        Intent intent = createIntentForTtlBroadcastReceiver();
        intent.putExtra(Constants.NOTIFICATION_ID, NOTIFICATION_ID);

        mReceiver.onReceive(mContext, intent);
        verify(mNotificationManager, times(1)).cancel(ArgumentMatchers.<String>isNull(), eq(NOTIFICATION_ID));
    }

    @Test
    public void testOnReceiveShouldCancel() {
        Intent intent = createIntentForTtlBroadcastReceiver();
        intent.putExtra(Constants.NOTIFICATION_ID, NOTIFICATION_ID);
        intent.putExtra(Constants.NOTIFICATION_TAG, NOTIFICATION_TAG);

        mReceiver.onReceive(mContext, intent);
        ArgumentCaptor<String> tagArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> idArg = ArgumentCaptor.forClass(Integer.class);
        verify(mNotificationManager, times(1)).cancel(tagArg.capture(), idArg.capture());
        assertThat(tagArg.getValue()).isEqualTo(NOTIFICATION_TAG);
        assertThat(idArg.getValue()).isEqualTo(NOTIFICATION_ID);
    }

    @Test
    public void testSetPushActiveToFalseIfNotificationWasCanceled() {
        String pushId = randomString();
        Intent intent = createIntentForTtlBroadcastReceiver();
        intent.putExtra(Constants.PUSH_ID, pushId);

        mReceiver.onReceive(mContext, intent);
        verify(mPushMessageHistory).setPushActive(eq(pushId), eq(false));
    }

    private Intent createIntentForTtlBroadcastReceiver() {
        Intent intent = new Intent();
        intent.setAction(TtlBroadcastReceiver.EXPIRED_BY_TTL_ACTION);
        return intent;
    }
}

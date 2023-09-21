package io.appmetrica.analytics.push.impl.processing;

import io.appmetrica.analytics.push.impl.processing.strategy.GeneralPushStrategy;
import io.appmetrica.analytics.push.impl.processing.strategy.PushStrategy;
import io.appmetrica.analytics.push.impl.processing.strategy.RemovingSilentPushStrategy;
import io.appmetrica.analytics.push.impl.processing.strategy.SilentPushStrategy;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.model.PushNotification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class DefaultPushProcessingStrategyProviderTests {

    private DefaultPushProcessingStrategyProvider mStrategyProvider;
    private PushMessage mPushMessage;
    private PushNotification mPushNotification;

    @Before
    public void setUp() {
        mStrategyProvider = new DefaultPushProcessingStrategyProvider();
        mPushMessage = mock(PushMessage.class);
        mPushNotification = mock(PushNotification.class);
    }

    @Test
    public void testGetPushStrategyShouldReturnSilentPushStrategyIfPushIsSilent() {
        when(mPushMessage.isSilent()).thenReturn(true);
        when(mPushMessage.getPushIdToRemove()).thenReturn(null);
        final PushStrategy strategy = mStrategyProvider.getPushStrategy(mPushMessage);
        assertThat(strategy).isInstanceOf(SilentPushStrategy.class);
    }

    @Test
    public void testGetPushStrategyShouldReturnRemovingSilentPushStrategyIfPushIsSilent() {
        when(mPushMessage.isSilent()).thenReturn(true);
        when(mPushMessage.getPushIdToRemove()).thenReturn("push_id_to_remove");
        final PushStrategy strategy = mStrategyProvider.getPushStrategy(mPushMessage);
        assertThat(strategy).isInstanceOf(RemovingSilentPushStrategy.class);
    }

    @Test
    public void testGetPushStrategyShouldReturnGeneralPushStrategyIfPushIsNotSilentAndNotificationIsNotNull() {
        when(mPushMessage.isSilent()).thenReturn(false);
        when(mPushMessage.getNotification()).thenReturn(mPushNotification);
        final PushStrategy strategy = mStrategyProvider.getPushStrategy(mPushMessage);
        assertThat(strategy).isInstanceOf(GeneralPushStrategy.class);
    }

    @Test
    public void testGetPushStrategyShouldReturnNullIfPushIsNotSilentAndNotificationIsNull() {
        when(mPushMessage.isSilent()).thenReturn(false);
        when(mPushMessage.getNotification()).thenReturn(null);
        final PushStrategy strategy = mStrategyProvider.getPushStrategy(mPushMessage);
        assertThat(strategy).isNull();
    }
}

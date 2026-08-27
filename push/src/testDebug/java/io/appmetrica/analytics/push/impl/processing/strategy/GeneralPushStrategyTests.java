package io.appmetrica.analytics.push.impl.processing.strategy;

import android.content.Context;
import io.appmetrica.analytics.push.impl.PushNotificationFactoryProvider;
import io.appmetrica.analytics.push.impl.processing.NotificationPublisher;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushNotificationFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GeneralPushStrategyTests {

    @Mock
    private Context context;
    @Mock
    private PushMessage pushMessage;
    @Mock
    private PushNotificationFactory pushNotificationFactory;
    @Mock
    private NotificationPublisher notificationPublisher;

    private GeneralPushStrategy strategy;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        PushNotificationFactoryProvider.setPushNotificationFactory(pushNotificationFactory);

        strategy = new GeneralPushStrategy(notificationPublisher);
    }

    @Test
    public void testProcessGeneralPushShouldCallPublishNotification() {
        strategy.processPush(context, pushMessage);
        verify(notificationPublisher, times(1)).publishNotification(
            context,
            pushNotificationFactory,
            pushMessage
        );
    }
}

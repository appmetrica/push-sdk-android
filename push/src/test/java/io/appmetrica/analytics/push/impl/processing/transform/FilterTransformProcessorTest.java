package io.appmetrica.analytics.push.impl.processing.transform;

import android.content.Context;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.processing.transform.filter.PushFilterFacade;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class FilterTransformProcessorTest extends TransformProcessorTest {

    @Rule
    public final MockedStaticRule<AppMetricaPushCore> sAppMetricaPushCore =
        new MockedStaticRule<>(AppMetricaPushCore.class);

    private PushFilterFacade pushFilterFacade;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application;
        AppMetricaPushCore appMetricaPushCore = mock(AppMetricaPushCore.class);
        when(AppMetricaPushCore.getInstance(context)).thenReturn(appMetricaPushCore);
        pushFilterFacade = mock(PushFilterFacade.class);
        when(appMetricaPushCore.getPushFilterFacade()).thenReturn(pushFilterFacade);
        super.setUp(new FilterTransformProcessor(context));
    }

    @Test
    public void testFilterShow() {
        PushMessage pushMessage = mock(PushMessage.class);
        when(pushFilterFacade.filter(pushMessage)).thenReturn(PushFilter.FilterResult.show());
        assertSuccess(pushMessage);
    }

    @Test
    public void testFilterSilence() {
        PushMessage pushMessage = mock(PushMessage.class);
        String category = randomString();
        String details = randomString();
        when(pushFilterFacade.filter(pushMessage)).thenReturn(PushFilter.FilterResult.silence(category, details));
        assertFailure(pushMessage, category, details);
    }
}

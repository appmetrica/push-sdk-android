package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.content.Context;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.settings.PushFilter;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class PushFilterFacadeTest {

    private static final List<Class<? extends PushFilter>> filtersClass = Arrays.asList(
        NotificationStatusFilter.class,
        VersionCodeFilter.class,
        AndroidApiLevelFilter.class,
        PerDayFilter.class,
        SinglePushPerPeriodFilter.class,
        PassportUidFilter.class,
        LocationFilter.class,
        PushIdFilter.class,
        ContentIdFilter.class,
        LoginFilter.class,
        TimeToLiveFilter.class
    );

    private Context context;
    private AppMetricaPushCore appMetricaPushCore;
    private PushFilterController pushFilterController;
    private PushFilterFacade pushFilterFacade;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
        pushFilterController = mock(PushFilterController.class);
        appMetricaPushCore = AppMetricaPushCore.getInstance(context);
        pushFilterFacade = new PushFilterFacade(context, appMetricaPushCore, pushFilterController);
    }

    @Test
    public void testCreateDefaultFilters() {
        verify(pushFilterController, times(filtersClass.size())).addPushFilter(any(PushFilter.class));
        for (Class<? extends PushFilter> filterClass : filtersClass) {
            verify(pushFilterController, times(1)).addPushFilter(isA(filterClass));
        }
    }
}

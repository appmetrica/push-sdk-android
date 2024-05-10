package io.appmetrica.analytics.push.impl.processing.transform.filter;

import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PushFilter;
import io.appmetrica.analytics.push.settings.PushFilteredCallback;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class FilterFacadeTest {

    private PushFilterController pushFilterController;
    private FilterFacade filterFacade;

    @Before
    public void setUp() {
        pushFilterController = mock(PushFilterController.class);
        filterFacade = new FilterFacade(pushFilterController);
    }

    @Test
    public void testCreateDefaultFilters() {
        verify(pushFilterController, times(0)).addPushFilter(any(PushFilter.class));
    }

    @Test
    public void testAddPushFilter() {
        PushFilter pushFilter = mock(PushFilter.class);
        filterFacade.addPushFilter(pushFilter);
        verify(pushFilterController, times(1)).addPushFilter(eq(pushFilter));
    }

    @Test
    public void testAddPushFilteredCallback() {
        PushFilteredCallback pushFilteredCallback = mock(PushFilteredCallback.class);
        filterFacade.addPushCallback(pushFilteredCallback);
        verify(pushFilterController, times(1)).addPushCallback(eq(pushFilteredCallback));
    }

    @Test
    public void testFilter() {
        PushMessage pushMessage = mock(PushMessage.class);
        filterFacade.filter(pushMessage);
        verify(pushFilterController, times(1)).filter(eq(pushMessage));
    }
}

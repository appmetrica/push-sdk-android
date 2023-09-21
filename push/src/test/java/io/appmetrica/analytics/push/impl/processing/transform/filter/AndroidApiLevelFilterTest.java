package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AndroidApiLevelFilterTest extends PushFilterTest {

    @Before
    public void setUp() {
        super.setUp(new AndroidApiLevelFilter());
    }

    @Test
    public void testNoFilters() {
        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getFilters()).thenReturn(null);
        assertShow(pushMessage);
    }

    @Test
    public void testMinAndroidApiLevel() {
        int apiLevel = Build.VERSION.SDK_INT;

        assertShow(createPushMessage(apiLevel, null));
        assertSilence(createPushMessage(apiLevel + 1, null));
    }

    @Test
    public void testMaxAndroidApiLevel() {
        int apiLevel = Build.VERSION.SDK_INT;

        assertShow(createPushMessage(null, apiLevel));
        assertSilence(createPushMessage(null, apiLevel - 1));
    }

    @NonNull
    private PushMessage createPushMessage(@Nullable Integer minApiLevel, @Nullable Integer maxApiLevel) {
        Filters filters = mock(Filters.class);
        when(filters.getMinAndroidApiLevel()).thenReturn(minApiLevel);
        when(filters.getMaxAndroidApiLevel()).thenReturn(maxApiLevel);

        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getFilters()).thenReturn(filters);
        return pushMessage;
    }
}

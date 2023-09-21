package io.appmetrica.analytics.push.impl.processing.transform.filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PassportUidProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class PassportUidFilterTest extends PushFilterTest {

    private AppMetricaPushCore appMetricaPushCore;

    @Before
    public void setUp() {
        appMetricaPushCore = AppMetricaPushCore.getInstance(RuntimeEnvironment.application);
        super.setUp(new PassportUidFilter(appMetricaPushCore));
    }

    @Test
    public void testNoFilters() {
        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getFilters()).thenReturn(null);
        assertShow(pushMessage);
    }

    @Test
    public void testNoPassportUid() {
        assertShow(createPushMessage(null));
    }

    @Test
    public void testEmptyPassportUid() {
        assertShow(createPushMessage(null));
    }

    @Test
    public void testNoPassportUidProvider() {
        assertSilence(createPushMessage(randomString()));
    }

    @Test
    public void testNoUidFromPassportUidProvider() {
        setPassportUidProviderWithUid(null);
        assertSilence(createPushMessage(randomString()));
    }

    @Test
    public void testDifferentPassportUid() {
        setPassportUidProviderWithUid(randomString());
        assertSilence(createPushMessage(randomString()));
    }

    @Test
    public void testPassportUid() {
        String passportUid = randomString();
        setPassportUidProviderWithUid(passportUid);
        assertShow(createPushMessage(passportUid));
    }

    private void setPassportUidProviderWithUid(@Nullable final String passportUid) {
        appMetricaPushCore.setPassportUidProvider(new PassportUidProvider() {
            @Nullable
            @Override
            public String getUid() {
                return passportUid;
            }
        });
    }

    @NonNull
    private PushMessage createPushMessage(@Nullable String passportUid) {
        Filters filters = mock(Filters.class);
        when(filters.getPassportUid()).thenReturn(passportUid);

        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getFilters()).thenReturn(filters);
        return pushMessage;
    }
}

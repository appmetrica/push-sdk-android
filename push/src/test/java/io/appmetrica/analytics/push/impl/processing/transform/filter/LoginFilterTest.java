package io.appmetrica.analytics.push.impl.processing.transform.filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import io.appmetrica.analytics.push.settings.PassportUidProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class LoginFilterTest extends PushFilterTest {

    public AppMetricaPushCore appMetricaPushCore;
    private static final int SHOW_TO_LOGGED_IN = 1;
    private static final int SHOW_TO_NOT_LOGGED_IN = 2;

    @Before
    public void setUp() {
        appMetricaPushCore = AppMetricaPushCore.getInstance(RuntimeEnvironment.application);
        super.setUp(new LoginFilter(appMetricaPushCore));
    }

    @After
    public void cleanUp() {
        appMetricaPushCore.setPassportUidProvider(null);
    }

    @Test
    public void testNoFilters() {
        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getFilters()).thenReturn(null);
        assertShow(pushMessage);
    }

    @Test
    public void testNoLoginFilterType() {
        assertShow(createPushMessage(null));
    }

    @Test
    public void testNoPassportUidProviderIfShowToAll() {
        assertShow(createPushMessage(SHOW_TO_LOGGED_IN | SHOW_TO_NOT_LOGGED_IN));
    }

    @Test
    public void testNoPassportUidProviderIfShowToNoOne() {
        assertSilence(createPushMessage(0));
    }

    @Test
    public void testNoPassportUidProviderIfShowToLoggedIn() {
        assertSilence(createPushMessage(SHOW_TO_LOGGED_IN));
    }

    @Test
    public void testNoPassportUidProviderIfShowToNotLoggedIn() {
        assertSilence(createPushMessage(SHOW_TO_NOT_LOGGED_IN));
    }

    @Test
    public void testNoUidFromPassportUidProviderIfShowToAll() {
        setPassportUidProviderWithUid(null);
        assertShow(createPushMessage(SHOW_TO_LOGGED_IN | SHOW_TO_NOT_LOGGED_IN));
    }

    @Test
    public void testNoUidFromPassportUidProviderIfShowToNoOne() {
        setPassportUidProviderWithUid(null);
        assertSilence(createPushMessage(0));
    }

    @Test
    public void testNoUidFromPassportUidProviderIfShowToLoggedIn() {
        setPassportUidProviderWithUid(null);
        assertSilence(createPushMessage(SHOW_TO_LOGGED_IN));
    }

    @Test
    public void testNoUidFromPassportUidProviderIfShowToNotLoggedIn() {
        setPassportUidProviderWithUid(null);
        assertShow(createPushMessage(SHOW_TO_NOT_LOGGED_IN));
    }

    @Test
    public void testHasPassportUidIfShowToAll() {
        setPassportUidProviderWithUid(randomString());
        assertShow(createPushMessage(SHOW_TO_LOGGED_IN | SHOW_TO_NOT_LOGGED_IN));
    }

    @Test
    public void testHasPassportUidIfShowToNoOne() {
        setPassportUidProviderWithUid(randomString());
        assertSilence(createPushMessage(0));
    }

    @Test
    public void testHasPassportUidIfShowToLoggedIn() {
        setPassportUidProviderWithUid(randomString());
        assertShow(createPushMessage(SHOW_TO_LOGGED_IN));
    }

    @Test
    public void testHasPassportUidIfShowToNotLoggedIn() {
        setPassportUidProviderWithUid(randomString());
        assertSilence(createPushMessage(SHOW_TO_NOT_LOGGED_IN));
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
    private PushMessage createPushMessage(@Nullable Integer loginFilterType) {
        Filters filters = mock(Filters.class);
        when(filters.getLoginFilterType()).thenReturn(loginFilterType);

        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getFilters()).thenReturn(filters);
        return pushMessage;
    }
}

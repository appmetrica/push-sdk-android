package io.appmetrica.analytics.push.provider.hms.impl;

import android.content.Context;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.common.ApiException;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class TokenHolderTest {

    @Rule
    public final MockedStaticRule<HuaweiApiAvailability> sHuaweiApiAvailability =
        new MockedStaticRule<>(HuaweiApiAvailability.class);
    @Rule
    public final MockedStaticRule<HmsInstanceId> sHmsInstanceId = new MockedStaticRule<>(HmsInstanceId.class);
    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);

    private Identifier identifier;
    private HmsInstanceId hmsInstanceId;
    private TrackersHub trackersHub;
    private TokenHolder holder;

    @Before
    public void setUp() {
        identifier = new Identifier(randomString());
        hmsInstanceId = mock(HmsInstanceId.class);
        when(HmsInstanceId.getInstance(any(Context.class))).thenReturn(hmsInstanceId);
        trackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(trackersHub);
        holder = new TokenHolder();
    }

    @Test
    public void testGetToken() throws ApiException {
        holder.register(mock(Context.class));
        holder.getToken(identifier);

        ArgumentCaptor<String> appIdArg = ArgumentCaptor.forClass(String.class);
        verify(hmsInstanceId, times(1)).getToken(appIdArg.capture(), eq("HCM"));
        assertThat(appIdArg.getValue()).isEqualTo(identifier.getAppId());
    }

    @Test
    public void testGetTokenWithoutRegister() throws ApiException {
        holder.getToken(identifier);

        ArgumentCaptor<String> appIdArg = ArgumentCaptor.forClass(String.class);
        verify(hmsInstanceId, times(0)).getToken(appIdArg.capture(), anyString());

        verify(trackersHub, never()).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testRegister() throws ApiException {
        final Context context = mock(Context.class);
        holder.register(context);

        sHmsInstanceId.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                HmsInstanceId.getInstance(same(context));
            }
        });
    }
}

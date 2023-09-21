package io.appmetrica.analytics.push.provider.hms.impl;

import android.content.Context;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiAvailability;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class BasePushServiceControllerTests {

    @Rule
    public final MockedStaticRule<HuaweiApiAvailability> sHuaweiApiAvailability =
        new MockedStaticRule<>(HuaweiApiAvailability.class);
    @Rule
    public final MockedStaticRule<HmsInstanceId> sHmsInstanceId = new MockedStaticRule<>(HmsInstanceId.class);
    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);
    @Rule
    public final MockedStaticRule<TokenHolder> sTokenHolder = new MockedStaticRule<>(TokenHolder.class);

    private Context mContext;
    private Identifier mIdentifier;
    private IdentifierExtractor mIdentifierExtractor;
    private BasePushServiceController mController;
    private HmsInstanceId mHmsInstanceId;
    private TrackersHub mTrackersHub;
    private HuaweiApiAvailability mHuaweiApiAvailability;
    private TokenHolder holder;

    @Before
    public void setUp() {
        PushServiceFacade.setJobIntentServiceWrapper(mock(PushServiceFacade.CommandServiceWrapper.class));
        mContext = mock(Context.class);
        mIdentifier = new Identifier(randomString());
        mIdentifierExtractor = new MockableIdentifierExtractor(mContext, mIdentifier);
        mController = new BasePushServiceController(mContext, mIdentifierExtractor);
        mHmsInstanceId = mock(HmsInstanceId.class);
        when(HmsInstanceId.getInstance(nullable(Context.class))).thenReturn(mHmsInstanceId);
        mTrackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(mTrackersHub);
        holder = mock(TokenHolder.class);
        when(TokenHolder.getInstance()).thenReturn(holder);
    }

    @Test
    public void testDoesNotGetIdentifiersOnCreate() {
        final IdentifierExtractor extractor = mock(MockableIdentifierExtractor.class);
        final BasePushServiceController controller = new BasePushServiceController(mContext, extractor);
        verifyNoInteractions(extractor);
    }

    @Test
    public void testRegisterWithGoogleApi() {
        setGoogleApiAvailability(true);
        assertThat(mController.register()).isTrue();

        verify(holder).register(any(Context.class));
    }

    @Test
    public void testRegisterWithoutGoogleApi() {
        setGoogleApiAvailability(false);
        assertThat(mController.register()).isFalse();

        verify(holder, times(0)).register(any(Context.class));
        verify(mTrackersHub, times(1)).reportEvent(anyString());
    }

    @Test
    public void testGetToken() {
        setGoogleApiAvailability(true);
        mController.register();
        mController.getToken();

        verify(holder).getToken(mIdentifier);
    }

    @Test
    public void testGetIdentifier() {
        Identifier identifier = mController.getIdentifier();

        assertThat(identifier).isEqualToComparingFieldByField(mIdentifier);
    }

    public void setGoogleApiAvailability(boolean availability) {
        mHuaweiApiAvailability = mock(HuaweiApiAvailability.class);
        when(HuaweiApiAvailability.getInstance()).thenReturn(mHuaweiApiAvailability);
        if (availability) {
            when(mHuaweiApiAvailability.isHuaweiMobileServicesAvailable(nullable(Context.class)))
                .thenReturn(ConnectionResult.SUCCESS);
        } else {
            when(mHuaweiApiAvailability.isHuaweiMobileServicesAvailable(nullable(Context.class)))
                .thenReturn(ConnectionResult.API_UNAVAILABLE);
        }
    }
}

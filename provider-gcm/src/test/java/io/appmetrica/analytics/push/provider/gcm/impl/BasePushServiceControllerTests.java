package io.appmetrica.analytics.push.provider.gcm.impl;

import android.content.Context;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.iid.InstanceID;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import java.io.IOException;
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
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class BasePushServiceControllerTests {

    @Rule
    public final MockedStaticRule<InstanceID> sInstanceID = new MockedStaticRule<>(InstanceID.class);
    @Rule
    public final MockedStaticRule<GoogleApiAvailability> sGoogleApiAvailability =
        new MockedStaticRule<>(GoogleApiAvailability.class);
    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);

    private Context mContext;
    private Identifier mIdentifier;
    private IdentifierExtractor mIdentifierExtractor;
    private BasePushServiceController mController;
    private InstanceID mInstanceId;
    private TrackersHub mTrackersHub;
    private GoogleApiAvailability mGoogleApiAvailability;

    @Before
    public void setUp() {
        PushServiceFacade.setJobIntentServiceWrapper(mock(PushServiceFacade.CommandServiceWrapper.class));
        mContext = mock(Context.class);
        mIdentifier = new Identifier(randomString());
        mIdentifierExtractor = new MockableIdentifierExtractor(mContext, mIdentifier);
        mController = new BasePushServiceController(mContext, mIdentifierExtractor);
        mInstanceId = mock(InstanceID.class);
        when(InstanceID.getInstance(any(Context.class))).thenReturn(mInstanceId);
        mTrackersHub = mock(TrackersHub.class);
        when(TrackersHub.getInstance()).thenReturn(mTrackersHub);
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
        mController.register();

        sInstanceID.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                InstanceID.getInstance(any(Context.class));
            }
        });
    }

    @Test
    public void testRegisterWithoutGoogleApi() {
        setGoogleApiAvailability(false);
        mController.register();

        sInstanceID.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                InstanceID.getInstance(nullable(Context.class));
            }
        }, times(0));

        verify(mTrackersHub, times(1)).reportEvent(anyString());
    }

    @Test
    public void testGetToken() throws IOException {
        setGoogleApiAvailability(true);
        mController.register();
        mController.getToken();

        ArgumentCaptor<String> senderIdArg = ArgumentCaptor.forClass(String.class);
        verify(mInstanceId, times(1)).getToken(senderIdArg.capture(), anyString());
        assertThat(senderIdArg.getValue()).isEqualTo(mIdentifier.getSenderId());
    }

    @Test
    public void testGetTokenWithoutRegister() throws IOException {
        mController.getToken();

        ArgumentCaptor<String> senderIdArg = ArgumentCaptor.forClass(String.class);
        verify(mInstanceId, times(0)).getToken(senderIdArg.capture(), anyString());

        verify(mTrackersHub, never()).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testGetIdentifier() {
        Identifier identifier = mController.getIdentifier();

        assertThat(identifier).isEqualToComparingFieldByField(mIdentifier);
    }

    @Test
    public void getTransportId() {
        assertThat(mController.getTransportId()).isEqualTo(CoreConstants.Transport.GCM);
    }

    @Test
    public void getRestrictions() {
        assertThat(mController.getExecutionRestrictions().getMaxTaskExecutionDurationSeconds()).isEqualTo(20);
    }

    @Test
    public void shouldSendToken() {
        assertThat(mController.shouldSendToken("Any token")).isTrue();
    }

    public void setGoogleApiAvailability(boolean availability) {
        mGoogleApiAvailability = mock(GoogleApiAvailability.class);
        when(GoogleApiAvailability.getInstance()).thenReturn(mGoogleApiAvailability);
        if (availability) {
            when(mGoogleApiAvailability.isGooglePlayServicesAvailable(any(Context.class)))
                .thenReturn(ConnectionResult.SUCCESS);
        } else {
            when(mGoogleApiAvailability.isGooglePlayServicesAvailable(any(Context.class)))
                .thenReturn(ConnectionResult.API_UNAVAILABLE);
        }
    }
}

package io.appmetrica.analytics.push.provider.firebase.impl;

import android.content.Context;
import androidx.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class BasePushServiceControllerTests {
    private final static String TOKEN = "token";

    @Rule
    public final MockedStaticRule<FirebaseApp> sFirebaseApp = new MockedStaticRule<>(FirebaseApp.class);
    @Rule
    public final MockedStaticRule<GoogleApiAvailability> sGoogleApiAvailability =
        new MockedStaticRule<>(GoogleApiAvailability.class);
    @Rule
    public final MockedStaticRule<TrackersHub> sTrackersHub = new MockedStaticRule<>(TrackersHub.class);

    private Context mContext;
    private Identifier mIdentifier;
    private IdentifierExtractor mIdentifierExtractor;
    private BasePushServiceController mController;
    private TrackersHub mTrackersHub;
    private GoogleApiAvailability mGoogleApiAvailability;
    private FirebaseApp mFirebaseApp;
    private FirebaseMessaging mFirebaseMessaging;

    @Before
    public void setUp() {
        PushServiceFacade.setJobIntentServiceWrapper(mock(PushServiceFacade.CommandServiceWrapper.class));
        mContext = mock(Context.class);
        mIdentifier = new Identifier(randomString(), randomString(), randomString(), randomString());
        mIdentifierExtractor = new MockableIdentifierExtractor(mContext, mIdentifier);
        mController = new BasePushServiceController(mContext, mIdentifierExtractor);
        mTrackersHub = mock(TrackersHub.class);
        mFirebaseMessaging = mock(FirebaseMessaging.class);
        mFirebaseApp = mock(FirebaseApp.class);
        when(TrackersHub.getInstance()).thenReturn(mTrackersHub);
        when(FirebaseApp.getInstance()).thenReturn(mFirebaseApp);
        when(mFirebaseApp.get(eq(FirebaseMessaging.class))).thenReturn(mFirebaseMessaging);
    }

    @Test
    public void testDoesNotGetIdentifiersOnCreate() {
        final IdentifierExtractor extractor = mock(MockableIdentifierExtractor.class);
        final BasePushServiceController controller = new BasePushServiceController(mContext, extractor);
        verifyNoInteractions(extractor);
    }

    @Test
    public void testInitializeFirebaseApp() {
        final ArgumentCaptor<FirebaseOptions> optionsArg = ArgumentCaptor.forClass(FirebaseOptions.class);
        mController.initializeFirebaseApp(mIdentifier.toFirebaseOptions());
        sFirebaseApp.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                FirebaseApp.initializeApp(any(Context.class), optionsArg.capture());
            }
        });

        assertThat(optionsArg.getValue().getApiKey()).isEqualTo(mIdentifier.getApiKey());
        assertThat(optionsArg.getValue().getApplicationId()).isEqualTo(mIdentifier.getAppId());
        assertThat(optionsArg.getValue().getGcmSenderId()).isEqualTo(mIdentifier.getSenderId());
        assertThat(optionsArg.getValue().getProjectId()).isEqualTo(mIdentifier.getProjectId());
    }

    @Test
    public void testInitializeFirebaseAppIfAlreadyInitialized() {
        when(FirebaseApp.initializeApp(any(Context.class), any(FirebaseOptions.class)))
            .thenThrow(new IllegalStateException());

        mController.initializeFirebaseApp(mIdentifier.toFirebaseOptions());

        sFirebaseApp.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                FirebaseApp.getInstance();
            }
        });
    }

    @Test
    public void testGetTokenWithoutRegister() throws IOException {
        mController.getToken();

        verify(mTrackersHub, never()).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testGetTokenReturnsResult() {
        Task<String> successTokenTask = successTokenTask();
        when(mFirebaseMessaging.getToken())
            .thenReturn(successTokenTask);
        setGoogleApiAvailability(true);
        mController.register();

        String result = mController.getToken();

        assertThat(result).isEqualTo(TOKEN);
        verify(mTrackersHub, never()).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testGetTokenIsRetriedSuccessfully() {
        Task<String> failTokenTask = failTokenTask();
        Task<String> successTokenTask = successTokenTask();
        when(mFirebaseMessaging.getToken())
            .thenReturn(failTokenTask)
            .thenReturn(successTokenTask);
        setGoogleApiAvailability(true);
        mController.register();

        String result = mController.getToken();

        assertThat(result).isEqualTo(TOKEN);
        verify(mTrackersHub, never()).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testGetTokenRetriesOnTimeout() {
        Task timeoutTokenTask = mock(Task.class); // listener for the task never called
        Task<String> successTokenTask = successTokenTask();
        when(mFirebaseMessaging.getToken())
            .thenReturn(timeoutTokenTask)
            .thenReturn(successTokenTask);
        setGoogleApiAvailability(true);
        mController.register();

        String result = mController.getToken();

        assertThat(result).isEqualTo(TOKEN);
        verify(mTrackersHub, never()).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testGetTokenRetriesOnlyOnce() {
        Task<String> failTokenTask = failTokenTask();
        when(mFirebaseMessaging.getToken()).thenReturn(failTokenTask);
        setGoogleApiAvailability(true);
        mController.register();

        String result = mController.getToken();

        assertThat(result).isEqualTo(null);
        verify(mTrackersHub).reportError(anyString(), any(Throwable.class));
        verifyNoMoreInteractions(mTrackersHub);
    }

    @Test
    public void testGetIdentifier() {
        Identifier identifier = mController.getIdentifier();

        assertThat(identifier).isEqualToComparingFieldByField(mIdentifier);
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

    private Task<String> successTokenTask() {
        return tokenTask(true, TOKEN, null);
    }

    private Task<String> failTokenTask() {
        return tokenTask(false, null, new RuntimeException("failure"));
    }

    private Task<String> tokenTask(
        boolean success, @Nullable String token, @Nullable Exception exception) {
        final Task<String> tokenTask = mock(Task.class);
        when(tokenTask.isSuccessful()).thenReturn(success);
        when(tokenTask.getResult()).thenReturn(token);
        when(tokenTask.getException()).thenReturn(exception);
        when(tokenTask.addOnCompleteListener(any(OnCompleteListener.class))).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                ((OnCompleteListener<String>) invocation.getArgument(0)).onComplete(tokenTask);
                return null;
            }
        });
        return tokenTask;
    }
}

package io.appmetrica.analytics.push.provider.firebase.impl;

import android.content.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class CustomPushServiceControllerTests {

    @Rule
    public final MockedStaticRule<FirebaseApp> sFirebaseApp = new MockedStaticRule<>(FirebaseApp.class);

    private Context mContext;
    private Identifier mIdentifier;
    private IdentifierExtractor mIdentifierExtractor;
    private BasePushServiceController mController;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        mIdentifier = new Identifier(randomString(), randomString(), randomString(), randomString());
        mIdentifierExtractor = new MockableIdentifierExtractor(mContext, mIdentifier);
        mController = new CustomPushServiceController(mContext, mIdentifierExtractor);
    }

    @Test
    public void testInitializeFirebaseApp() {
        mController.initializeFirebaseApp(mIdentifier.toFirebaseOptions());

        final ArgumentCaptor<FirebaseOptions> optionsArg = ArgumentCaptor.forClass(FirebaseOptions.class);
        final ArgumentCaptor<String> firebaseAppNameArg = ArgumentCaptor.forClass(String.class);

        sFirebaseApp.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                FirebaseApp.initializeApp(any(Context.class), optionsArg.capture(), firebaseAppNameArg.capture());
            }
        });

        assertThat(optionsArg.getValue().getApiKey()).isEqualTo(mIdentifier.getApiKey());
        assertThat(optionsArg.getValue().getApplicationId()).isEqualTo(mIdentifier.getAppId());
        assertThat(optionsArg.getValue().getGcmSenderId()).isEqualTo(mIdentifier.getSenderId());
        assertThat(optionsArg.getValue().getProjectId()).isEqualTo(mIdentifier.getProjectId());
        assertThat(firebaseAppNameArg.getValue()).isEqualTo(CustomPushServiceController.CUSTOM_FIREBASE_APP_NAME);
    }

    @Test
    public void testFirebaseAppAlreadyInitialized() {
        when(FirebaseApp.initializeApp(any(Context.class), any(FirebaseOptions.class), anyString()))
            .thenThrow(new IllegalStateException());

        mController.initializeFirebaseApp(mIdentifier.toFirebaseOptions());

        sFirebaseApp.getStaticMock().verify(new MockedStatic.Verification() {
            @Override
            public void apply() throws Throwable {
                FirebaseApp.getInstance(CustomPushServiceController.CUSTOM_FIREBASE_APP_NAME);
            }
        });
    }
}

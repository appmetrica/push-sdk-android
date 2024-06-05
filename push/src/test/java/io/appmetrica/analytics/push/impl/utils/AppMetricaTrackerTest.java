package io.appmetrica.analytics.push.impl.utils;

import android.content.Context;
import io.appmetrica.analytics.AppMetrica;
import io.appmetrica.analytics.IReporter;
import io.appmetrica.analytics.push.BuildConfig;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.impl.AppMetricaPushCore;
import io.appmetrica.analytics.push.impl.PushServiceControllerComposite;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaTrackerTest {

    @Rule
    public final MockedStaticRule<AppMetrica> appMetricaMockedStaticRule = new MockedStaticRule<>(AppMetrica.class);
    @Rule
    public final MockedStaticRule<AppMetricaPushCore> sAppMetricaPushCore =
        new MockedStaticRule<>(AppMetricaPushCore.class);

    private Context mContext;
    private IReporter mReporter;
    private AppMetricaTracker mTracker;
    private AppMetricaPushCore core;
    private PushServiceControllerComposite controller;

    @Before
    public void setUp() {
        mContext = RuntimeEnvironment.application;
        mReporter = mock(IReporter.class);

        core = mock(AppMetricaPushCore.class);
        controller = mock(PushServiceControllerComposite.class);
        when(AppMetrica.getReporter(any(Context.class), anyString())).thenReturn(mReporter);
        when(AppMetricaPushCore.getInstance(any(Context.class))).thenReturn(core);
        doReturn(controller).when(core).getPushServiceController();
        doReturn(Arrays.asList(CoreConstants.Transport.FIREBASE, CoreConstants.Transport.UNKNOWN))
            .when(controller).getTransportIds();
        mTracker = new AppMetricaTracker(mContext, new RandomStringGenerator().nextString());
    }

    @Test
    public void testResumeSessionShouldSendResumeToReporter() {
        mTracker.resumeSession();
        verify(mReporter, times(1)).resumeSession();
    }

    @Test
    public void testPauseSessionShouldSendResumeToReporter() {
        mTracker.pauseSession();
        verify(mReporter, times(1)).pauseSession();
    }

    @Test
    public void testReportEventShouldSendEventNameToReporter() {
        String eventName = new RandomStringGenerator().nextString();
        mTracker.reportEvent(eventName);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mReporter, times(1)).reportEvent(arg.capture(), ArgumentMatchers.<String, Object>anyMap());
        assertThat(arg.getValue()).isEqualTo(eventName);
    }

    @Test
    public void testReportEventShouldIncludeSdkVersionToEnvironment() {
        mTracker.reportEvent(new RandomStringGenerator().nextString());
        ArgumentCaptor<Map> arg = ArgumentCaptor.forClass(Map.class);
        verify(mReporter, times(1)).reportEvent(anyString(), arg.capture());
        verifyConstainsVersion(arg.getValue());
    }

    @Test
    public void testReportEventWithAttributesShouldIncludeSdkVersionToEnvironment() {
        mTracker.reportEvent(new RandomStringGenerator().nextString(), new HashMap<String, Object>());
        ArgumentCaptor<Map> arg = ArgumentCaptor.forClass(Map.class);
        verify(mReporter, times(1)).reportEvent(anyString(), arg.capture());
        verifyConstainsVersion(arg.getValue());
    }

    private void verifyConstainsVersion(Map attributes) {
        assertThat(attributes).containsKey(AppMetricaTracker.SDK_VERSION_CODE_FIELD);
        assertThat(attributes.get(AppMetricaTracker.SDK_VERSION_CODE_FIELD))
            .isEqualTo(String.valueOf(BuildConfig.VERSION_CODE));
        assertThat(attributes).containsKey(AppMetricaTracker.TRANSPORT_FIELD);
        assertThat(attributes.get(AppMetricaTracker.TRANSPORT_FIELD))
            .isEqualTo("[" + CoreConstants.Transport.FIREBASE + ", " + CoreConstants.Transport.UNKNOWN + "]");
    }

    @Test
    public void testReportEventWithAttributesShouldSendEventNameToReporter() {
        String eventName = new RandomStringGenerator().nextString();
        mTracker.reportEvent(eventName, null);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mReporter, times(1)).reportEvent(arg.capture(), ArgumentMatchers.<String, Object>anyMap());
        assertThat(arg.getValue()).isEqualTo(eventName);
    }

    @Test
    public void testReportEventWithAttributesShouldSendAttributesToReporter() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        String key = new RandomStringGenerator().nextString();
        String value = new RandomStringGenerator().nextString();
        attributes.put(key, value);
        mTracker.reportEvent(new RandomStringGenerator().nextString(), attributes);
        ArgumentCaptor<Map> arg = ArgumentCaptor.forClass(Map.class);
        verify(mReporter, times(1)).reportEvent(anyString(), arg.capture());

        assertThat(arg.getValue()).containsKey(key);
        assertThat(arg.getValue().get(key)).isEqualTo(value);
    }

    @Test
    public void testReportErrorShouldSendMessageToReporter() {
        String message = new RandomStringGenerator().nextString();
        mTracker.reportError(message, null);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mReporter, times(1)).reportError(arg.capture(), nullable(Throwable.class));
        assertThat(arg.getValue()).contains(message);
    }

    @Test
    public void testReportErrorShouldSendThrowableToReporter() {
        Throwable throwable = mock(Throwable.class);
        mTracker.reportError(null, throwable);
        ArgumentCaptor<Throwable> arg = ArgumentCaptor.forClass(Throwable.class);
        verify(mReporter, times(1)).reportError(anyString(), arg.capture());
        assertThat(arg.getValue()).isEqualTo(throwable);
    }

    @Test
    public void testReportErrorShouldContainsSdkVersionInErrorMessage() {
        String message = new RandomStringGenerator().nextString();
        mTracker.reportError(message, null);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mReporter, times(1)).reportError(arg.capture(), nullable(Throwable.class));
        assertThat(arg.getValue())
            .contains(String.format("%s = %s", AppMetricaTracker.SDK_VERSION_CODE_FIELD, BuildConfig.VERSION_CODE));
//        assertThat(arg.getValue())
//            .contains(String.format("%s = %s", AppMetricaTracker.TRANSPORT_FIELD, AppMetricaPushCore.getProviderTitle()));
    }

    @Test
    public void testReportUnhandledExceptionShouldSendThrowableToReporter() {
        Throwable throwable = mock(Throwable.class);
        mTracker.reportUnhandledException(throwable);
        ArgumentCaptor<Throwable> arg = ArgumentCaptor.forClass(Throwable.class);
        verify(mReporter, times(1)).reportUnhandledException(arg.capture());
        assertThat(arg.getValue()).isEqualTo(throwable);
    }
}

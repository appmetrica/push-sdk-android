package io.appmetrica.analytics.push.impl.utils;

import io.appmetrica.analytics.push.coreutils.internal.utils.Tracker;
import io.appmetrica.analytics.push.coreutils.internal.utils.TrackersHub;
import io.appmetrica.analytics.AppMetrica;
import io.appmetrica.analytics.push.testutils.MockedStaticRule;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class TrackerHubTest {

    private Tracker mFirstTracker;
    private Tracker mSecondTracker;
    private TrackersHub mTrackersHub;

    @Rule
    public final MockedStaticRule<AppMetrica> appMetricaMockedStaticRule = new MockedStaticRule<>(AppMetrica.class);

    @Before
    public void setUp() {
        mFirstTracker = mock(Tracker.class);
        mSecondTracker = mock(Tracker.class);
        mTrackersHub = new TrackersHub();
    }

    @Test
    public void testReportResumeSessionShouldDoNothingIfTrackersListIsEmpty() {
        mTrackersHub.resumeSession();
        verify(mFirstTracker, never()).resumeSession();
        verify(mSecondTracker, never()).pauseSession();
    }

    @Test
    public void testReportResumeSessionShouldSendResumeToAllTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.registerTracker(mSecondTracker);
        mTrackersHub.resumeSession();
        verify(mFirstTracker, times(1)).resumeSession();
        verify(mSecondTracker, times(1)).resumeSession();
    }

    @Test
    public void testReportResumeShouldNotSendEventToTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.resumeSession();
        verify(mFirstTracker, never()).reportEvent(anyString());
    }

    @Test
    public void testReportResumeShouldNotSendEventWithAttributesToTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.resumeSession();
        verify(mFirstTracker, never()).reportEvent(anyString(), ArgumentMatchers.<String, Object>anyMap());
    }

    @Test
    public void testReportResumeShouldNotSendErrorToTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.resumeSession();
        verify(mFirstTracker, never()).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testReportResumeShouldNotSendUnhandledExceptionToTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.resumeSession();
        verify(mFirstTracker, never()).reportUnhandledException(any(Throwable.class));
    }

    @Test
    public void testReportResumeShouldNotSendPauseToTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.resumeSession();
        verify(mFirstTracker, never()).pauseSession();
    }

    @Test
    public void testPauseSessionShouldDoNothingIfTrackersListIsEmpty() {
        mTrackersHub.pauseSession();
        verify(mFirstTracker, never()).pauseSession();
        verify(mSecondTracker, never()).pauseSession();
    }

    @Test
    public void testPauseSessionShouldSendEventToAllTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.registerTracker(mSecondTracker);
        mTrackersHub.pauseSession();
        verify(mFirstTracker, times(1)).pauseSession();
        verify(mSecondTracker, times(1)).pauseSession();
    }

    @Test
    public void testPauseSessionShouldNotSendResumeToTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.pauseSession();
        verify(mFirstTracker, never()).resumeSession();
    }

    @Test
    public void testPauseSessionShouldNotSendEventToTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.pauseSession();
        verify(mFirstTracker, never()).reportEvent(anyString());
    }

    @Test
    public void testPauseSessionShouldNotSendEventWithAttributesToTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.pauseSession();
        verify(mFirstTracker, never()).reportEvent(anyString(), ArgumentMatchers.<String, Object>anyMap());
    }

    @Test
    public void testPauseSessionShouldNotSendErrorToTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.pauseSession();
        verify(mFirstTracker, never()).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testPauseSessionShouldNotSendUnhandledExceptionToTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.pauseSession();
        verify(mFirstTracker, never()).reportUnhandledException(any(Throwable.class));
    }

    @Test
    public void testReportEventShouldDoNothingIfTrackersListIsEmpty() {
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString());
        verify(mFirstTracker, never()).reportEvent(anyString());
        verify(mSecondTracker, never()).reportEvent(anyString());
    }

    @Test
    public void testReportEventShouldSendEventToAllTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.registerTracker(mSecondTracker);
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString());
        verify(mFirstTracker, times(1)).reportEvent(anyString());
        verify(mSecondTracker, times(1)).reportEvent(anyString());
    }

    @Test
    public void testReportEventShouldSendEventNameToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        String event = new RandomStringGenerator().nextString();
        mTrackersHub.reportEvent(event);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mFirstTracker, times(1)).reportEvent(arg.capture());
        assertThat(arg.getValue()).isEqualTo(event);
    }

    @Test
    public void testReportEventShouldNotSendResumeSessionToTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString());
        verify(mFirstTracker, never()).resumeSession();
    }

    @Test
    public void testReportEventShouldNotSendPauseSessionToTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString());
        verify(mFirstTracker, never()).pauseSession();
    }

    @Test
    public void testReportEventShouldNotSendReportEventWithAttributesToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString());
        verify(mFirstTracker, never()).reportEvent(anyString(), ArgumentMatchers.<String, Object>anyMap());
    }

    @Test
    public void testReportEventShouldNotSendErrorToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString());
        verify(mFirstTracker, never()).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testReportEventShouldNotSendUnhandledExceptionToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString());
        verify(mFirstTracker, never()).reportUnhandledException(any(Throwable.class));
    }

    @Test
    public void testReportEventWithAttributesShouldDoNothingIfTrackersListIsEmpty() {
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString(), null);
        verify(mFirstTracker, never()).reportEvent(anyString(), ArgumentMatchers.<String, Object>anyMap());
        verify(mSecondTracker, never()).reportEvent(anyString(), ArgumentMatchers.<String, Object>anyMap());
    }

    @Test
    public void testReportEventWithAttributesShouldSendEventToAllTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.registerTracker(mSecondTracker);
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString(), null);
        verify(mFirstTracker, times(1)).reportEvent(anyString(), nullable(Map.class));
        verify(mSecondTracker, times(1)).reportEvent(anyString(), nullable(Map.class));
    }

    @Test
    public void testReportEventWithAttributesShouldSendEventNameToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        String eventName = new RandomStringGenerator().nextString();
        mTrackersHub.reportEvent(eventName, null);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mFirstTracker, times(1)).reportEvent(arg.capture(), nullable(Map.class));
        assertThat(arg.getValue()).isEqualTo(eventName);
    }

    @Test
    public void testReportEventWithAttributesShouldSendAttributesToTracker() {
        final String eventName = "eventName";
        mTrackersHub.registerTracker(mFirstTracker);
        Map<String, Object> attributes = mock(Map.class);
        mTrackersHub.reportEvent(eventName, attributes);
        ArgumentCaptor<Map> arg = ArgumentCaptor.forClass(Map.class);
        verify(mFirstTracker, times(1)).reportEvent(eq(eventName), arg.capture());
        assertThat(arg.getValue()).isEqualTo(attributes);
    }

    @Test
    public void testReportEventWithAttributesShouldNotSendResumeToTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString(), new HashMap<String, Object>());
        verify(mFirstTracker, never()).resumeSession();
    }

    @Test
    public void testReportEventWithAttributesShouldNotSendPauseToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString(), new HashMap<String, Object>());
        verify(mFirstTracker, never()).pauseSession();
    }

    @Test
    public void testReportEventWithAttributesShouldNotSendEventWithoutAttributesToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString(), new HashMap<String, Object>());
        verify(mFirstTracker, never()).reportEvent(anyString());
    }

    @Test
    public void testReportEventWithAttributesShouldNotSendErrorToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString(), new HashMap<String, Object>());
        verify(mFirstTracker, never()).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testReportEventWithAttributesShouldNotSendUnhandledExceptionToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportEvent(new RandomStringGenerator().nextString(), new HashMap<String, Object>());
        verify(mFirstTracker, never()).reportUnhandledException(any(Throwable.class));
    }

    @Test
    public void testReportErrorShouldDoNothingIfTrackersListIsEmpty() {
        mTrackersHub.reportError(new RandomStringGenerator().nextString(), null);
        verify(mFirstTracker, never()).reportError(anyString(), any(Throwable.class));
        verify(mSecondTracker, never()).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testReportErrorShouldSendErrorToAllTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.registerTracker(mSecondTracker);
        mTrackersHub.reportError(new RandomStringGenerator().nextString(), mock(Throwable.class));
        verify(mFirstTracker, times(1)).reportError(anyString(), any(Throwable.class));
        verify(mSecondTracker, times(1)).reportError(anyString(), any(Throwable.class));
    }

    @Test
    public void testReportErrorShouldSendErrorMessageToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        String errorMessage = new RandomStringGenerator().nextString();
        mTrackersHub.reportError(errorMessage, mock(Throwable.class));
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        verify(mFirstTracker, times(1)).reportError(arg.capture(), any(Throwable.class));
        assertThat(arg.getValue()).isEqualTo(errorMessage);
    }

    @Test
    public void testReportErrorShouldSendThrowableToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        Throwable error = mock(Throwable.class);
        mTrackersHub.reportError(new RandomStringGenerator().nextString(), error);
        ArgumentCaptor<Throwable> arg = ArgumentCaptor.forClass(Throwable.class);
        verify(mFirstTracker, times(1)).reportError(anyString(), arg.capture());
        assertThat(arg.getValue()).isEqualTo(error);
    }

    @Test
    public void testReportErrorShouldNotSendResumeToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportError(new RandomStringGenerator().nextString(), mock(Throwable.class));
        verify(mFirstTracker, never()).resumeSession();
    }

    @Test
    public void testReportErrorShouldNotSendPauseToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportError(new RandomStringGenerator().nextString(), mock(Throwable.class));
        verify(mFirstTracker, never()).pauseSession();
    }

    @Test
    public void testReportErrorShouldNotSendEventToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportError(new RandomStringGenerator().nextString(), mock(Throwable.class));
        verify(mFirstTracker, never()).reportEvent(anyString());
    }

    @Test
    public void testReportErrorShouldNotSendEventWithAttributesToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportError(new RandomStringGenerator().nextString(), mock(Throwable.class));
        verify(mFirstTracker, never()).reportEvent(anyString(), ArgumentMatchers.<String, Object>anyMap());
    }

    @Test
    public void testReportErrorShouldNotSendUnhandledThrowableToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportError(new RandomStringGenerator().nextString(), mock(Throwable.class));
        verify(mFirstTracker, never()).reportUnhandledException(any(Throwable.class));
    }

    @Test
    public void testReportUnhandledExceptionShouldDoNothingIfTrackersListIsEmpty() {
        mTrackersHub.reportUnhandledException(mock(Throwable.class));
        verify(mFirstTracker, never()).reportUnhandledException(any(Throwable.class));
        verify(mSecondTracker, never()).reportUnhandledException(any(Throwable.class));
    }

    @Test
    public void testReportUnhandledExceptionShouldReportExceptionToAllTrackers() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.registerTracker(mSecondTracker);
        mTrackersHub.reportUnhandledException(mock(Throwable.class));
        verify(mFirstTracker, times(1)).reportUnhandledException(any(Throwable.class));
        verify(mSecondTracker, times(1)).reportUnhandledException(any(Throwable.class));
    }

    @Test
    public void testReportUnhandledExceptionShouldReportThrowableToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        Throwable throwable = mock(Throwable.class);
        mTrackersHub.reportUnhandledException(throwable);
        ArgumentCaptor<Throwable> arg = ArgumentCaptor.forClass(Throwable.class);
        verify(mFirstTracker, times(1)).reportUnhandledException(arg.capture());
        assertThat(arg.getValue()).isEqualTo(throwable);
    }

    @Test
    public void testReportUnhandledExceptionShouldNotReportResumeToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportUnhandledException(mock(Throwable.class));
        verify(mFirstTracker, never()).resumeSession();
    }

    @Test
    public void testReportUnhandledExceptionShouldNoReportPauseToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportUnhandledException(mock(Throwable.class));
        verify(mFirstTracker, never()).pauseSession();
    }

    @Test
    public void testReportUnhandledExceptionShouldNotSendEventToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportUnhandledException(mock(Throwable.class));
        verify(mFirstTracker, never()).reportEvent(anyString());
    }

    @Test
    public void testReportUnhandledExceptionShouldNotSendEventWithAttributesToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportUnhandledException(mock(Throwable.class));
        verify(mFirstTracker, never()).reportEvent(anyString(), ArgumentMatchers.<String, Object>anyMap());
    }

    @Test
    public void testReportUnhandledExceptionShouldNotSendErrorToTracker() {
        mTrackersHub.registerTracker(mFirstTracker);
        mTrackersHub.reportUnhandledException(mock(Throwable.class));
        verify(mFirstTracker, never()).reportError(anyString(), any(Throwable.class));
    }
}

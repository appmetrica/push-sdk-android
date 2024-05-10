package io.appmetrica.analytics.push;

import android.content.Intent;
import io.appmetrica.analytics.push.coreutils.internal.CoreConstants;
import io.appmetrica.analytics.push.intent.NotificationActionInfo;
import io.appmetrica.analytics.push.settings.PushMessageTracker;
import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class AppMetricaPushTrackerTest {

    private PushMessageTracker mTracker;
    private AppMetricaPushTracker mTrackingFacade;
    private String mPushId;
    private String mAdditionalActionId;
    private NotificationActionInfo mActionInfo;
    private Intent mIntent;
    private String category = "category";
    private String details = "details";
    private String transport = "transport";

    @Before
    public void setUp() {
        mTracker = mock(PushMessageTracker.class);
        mTrackingFacade = new AppMetricaPushTracker(mTracker);
        mPushId = new RandomStringGenerator().nextString();
        mAdditionalActionId = new RandomStringGenerator().nextString();
        mActionInfo = NotificationActionInfo.newBuilder(transport)
            .withPushId(mPushId)
            .withActionId(mAdditionalActionId)
            .build();
        mIntent = new Intent().putExtra(AppMetricaPush.EXTRA_ACTION_INFO, mActionInfo);
    }

    //region receive
    @Test
    public void testReportReceiveShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportReceive(mPushId);
        verify(mTracker).onMessageReceived(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportReceiveShouldSendEventToTracker() {
        mTrackingFacade.reportReceive(mPushId, transport);
        verify(mTracker).onMessageReceived(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportReceiveNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException()).when(mTracker).onMessageReceived(anyString(), anyString(), anyString());
        mTrackingFacade.reportReceive(mPushId);
    }

    @Test
    public void testReportReceiveFromActionInfoShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportReceive(mActionInfo);
        verify(mTracker).onMessageReceived(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportReceiveFromActionInfoShouldSendEventToTracker() {
        mTrackingFacade.reportReceive(mActionInfo, transport);
        verify(mTracker).onMessageReceived(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportReceiveFromActionInfoNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException()).when(mTracker).onMessageReceived(anyString(), anyString(), anyString());
        mTrackingFacade.reportReceive(mActionInfo);
    }

    @Test
    public void testReportReceiveFromIntentShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportReceive(mIntent);
        verify(mTracker).onMessageReceived(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportReceiveFromIntentShouldSendEventToTracker() {
        mTrackingFacade.reportReceive(mIntent, transport);
        verify(mTracker).onMessageReceived(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportReceiveFromIntentNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException()).when(mTracker).onMessageReceived(anyString(), anyString(), anyString());
        mTrackingFacade.reportReceive(mIntent);
    }
    //endregion

    //region open
    @Test
    public void testReportOpenShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportOpen(mPushId);
        verify(mTracker).onPushOpened(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportOpenShouldSendEventToTracker() {
        mTrackingFacade.reportOpen(mPushId, transport);
        verify(mTracker).onPushOpened(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportOpenNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException()).when(mTracker).onPushOpened(anyString(), anyString(), anyString());
        mTrackingFacade.reportOpen(mPushId);
    }

    @Test
    public void testReportOpenFromActionInfoShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportOpen(mActionInfo);
        verify(mTracker).onPushOpened(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportOpenFromActionInfoShouldSendEventToTracker() {
        mTrackingFacade.reportOpen(mActionInfo, transport);
        verify(mTracker).onPushOpened(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportOpenFromActionInfoNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException()).when(mTracker).onPushOpened(anyString(), anyString(), anyString());
        mTrackingFacade.reportOpen(mActionInfo);
    }

    @Test
    public void testReportOpenFromIntentShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportOpen(mIntent);
        verify(mTracker).onPushOpened(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportOpenFromIntentShouldSendEventToTracker() {
        mTrackingFacade.reportOpen(mIntent, transport);
        verify(mTracker).onPushOpened(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportOpenFromIntentNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException()).when(mTracker).onPushOpened(anyString(), anyString(), anyString());
        mTrackingFacade.reportOpen(mIntent);
    }
    //endregion

    //region clear
    @Test
    public void testReportClearShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportDismiss(mPushId);
        verify(mTracker)
            .onNotificationCleared(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportClearShouldSendEventToTracker() {
        mTrackingFacade.reportDismiss(mPushId, transport);
        verify(mTracker).onNotificationCleared(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportClearNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker)
            .onNotificationCleared(anyString(), anyString(), anyString());
        mTrackingFacade.reportDismiss(mPushId);
    }

    @Test
    public void testReportClearFromActionInfoShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportDismiss(mActionInfo);
        verify(mTracker)
            .onNotificationCleared(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportClearFromActionInfoShouldSendEventToTracker() {
        mTrackingFacade.reportDismiss(mActionInfo, transport);
        verify(mTracker).onNotificationCleared(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportClearFromActionInfoNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker)
            .onNotificationCleared(anyString(), anyString(), anyString());
        mTrackingFacade.reportDismiss(mActionInfo);
    }

    @Test
    public void testReportClearFromIntentShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportDismiss(mIntent);
        verify(mTracker)
            .onNotificationCleared(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportClearFromIntentShouldSendEventToTracker() {
        mTrackingFacade.reportDismiss(mIntent, transport);
        verify(mTracker).onNotificationCleared(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportClearFromIntentNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker)
            .onNotificationCleared(anyString(), anyString(), anyString());
        mTrackingFacade.reportDismiss(mIntent);
    }
    //endregion

    //region additional action
    @Test
    public void testReportAdditionalActionShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportAdditionalAction(mPushId, mAdditionalActionId);
        verify(mTracker)
            .onNotificationAdditionalAction(
                eq(mPushId),
                eq(mAdditionalActionId),
                nullable(String.class),
                eq(CoreConstants.Transport.UNKNOWN)
            );
    }

    @Test
    public void testReportAdditionalActionShouldSendEventToTracker() {
        mTrackingFacade.reportAdditionalAction(mPushId, mAdditionalActionId, transport);
        verify(mTracker)
            .onNotificationAdditionalAction(
                eq(mPushId),
                eq(mAdditionalActionId),
                nullable(String.class),
                eq(transport)
            );
    }

    @Test
    public void testReportAdditionalActionNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker).onNotificationAdditionalAction(anyString(), anyString(), anyString(), anyString());
        mTrackingFacade.reportAdditionalAction(mPushId, mAdditionalActionId);
    }

    @Test
    public void testReportAdditionalActionFromActionInfoShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportAdditionalAction(mActionInfo);
        verify(mTracker)
            .onNotificationAdditionalAction(
                eq(mPushId),
                eq(mAdditionalActionId),
                nullable(String.class),
                eq(CoreConstants.Transport.UNKNOWN)
            );
    }

    @Test
    public void testReportAdditionalActionFromActionInfoShouldSendEventToTracker() {
        mTrackingFacade.reportAdditionalAction(mActionInfo, transport);
        verify(mTracker)
            .onNotificationAdditionalAction(
                eq(mPushId),
                eq(mAdditionalActionId),
                nullable(String.class),
                eq(transport)
            );
    }

    @Test
    public void testReportAdditionalActionFromActionInfoNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker).onNotificationAdditionalAction(anyString(), anyString(), anyString(), anyString());
        mTrackingFacade.reportAdditionalAction(mActionInfo);
    }

    @Test
    public void testReportAdditionalActionFromIntentShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportAdditionalAction(mIntent);
        verify(mTracker)
            .onNotificationAdditionalAction(
                eq(mPushId),
                eq(mAdditionalActionId),
                nullable(String.class),
                eq(CoreConstants.Transport.UNKNOWN)
            );
    }

    @Test
    public void testReportAdditionalActionFromIntentShouldSendEventToTracker() {
        mTrackingFacade.reportAdditionalAction(mIntent, transport);
        verify(mTracker)
            .onNotificationAdditionalAction(
                eq(mPushId),
                eq(mAdditionalActionId),
                nullable(String.class),
                eq(transport)
            );
    }

    @Test
    public void testReportAdditionalActionFromIntentNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker)
            .onNotificationAdditionalAction(anyString(), anyString(), anyString(), anyString());
        mTrackingFacade.reportAdditionalAction(mIntent);
    }
    //endregion

    //region process
    @Test
    public void testReportProcessShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportProcess(mPushId);
        verify(mTracker)
            .onSilentPushProcessed(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportProcessShouldSendEventToTracker() {
        mTrackingFacade.reportProcess(mPushId, transport);
        verify(mTracker).onSilentPushProcessed(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportProcessNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker).onSilentPushProcessed(anyString(), anyString(), anyString());
        mTrackingFacade.reportProcess(mPushId);
    }

    @Test
    public void testReportProcessFromActionInfoShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportProcess(mActionInfo);
        verify(mTracker)
            .onSilentPushProcessed(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportProcessFromActionInfoShouldSendEventToTracker() {
        mTrackingFacade.reportProcess(mActionInfo, transport);
        verify(mTracker).onSilentPushProcessed(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportProcessFromActionInfoNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker).onSilentPushProcessed(anyString(), anyString(), anyString());
        mTrackingFacade.reportProcess(mActionInfo);
    }

    @Test
    public void testReportProcessShouldFromIntentSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportProcess(mIntent);
        verify(mTracker)
            .onSilentPushProcessed(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportProcessShouldFromIntentSendEventToTracker() {
        mTrackingFacade.reportProcess(mIntent, transport);
        verify(mTracker).onSilentPushProcessed(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportProcessNotShouldFromIntentThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker).onSilentPushProcessed(anyString(), anyString(), anyString());
        mTrackingFacade.reportProcess(mIntent);
    }
    //endregion

    //region shown
    @Test
    public void testReportShownShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportShown(mPushId);
        verify(mTracker).onNotificationShown(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportShownShouldSendEventToTracker() {
        mTrackingFacade.reportShown(mPushId, transport);
        verify(mTracker).onNotificationShown(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportShownNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker).onNotificationShown(anyString(), anyString(), anyString());
        mTrackingFacade.reportShown(mPushId);
    }

    @Test
    public void testReportShownFromActionInfoShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportShown(mActionInfo);
        verify(mTracker).onNotificationShown(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportShownFromActionInfoShouldSendEventToTracker() {
        mTrackingFacade.reportShown(mActionInfo, transport);
        verify(mTracker).onNotificationShown(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportShownFromActionInfoNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker).onNotificationShown(anyString(), anyString(), anyString());
        mTrackingFacade.reportShown(mActionInfo);
    }

    @Test
    public void testReportShownShouldFromIntentSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportShown(mIntent);
        verify(mTracker).onNotificationShown(eq(mPushId), nullable(String.class), eq(CoreConstants.Transport.UNKNOWN));
    }

    @Test
    public void testReportShownShouldFromIntentSendEventToTracker() {
        mTrackingFacade.reportShown(mIntent, transport);
        verify(mTracker).onNotificationShown(eq(mPushId), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportShownNotShouldFromIntentThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker).onNotificationShown(anyString(), anyString(), anyString());
        mTrackingFacade.reportShown(mIntent);
    }
    //endregion

    //region ignored
    @Test
    public void testReportIgnoredShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportIgnored(mPushId, category, details);
        verify(mTracker)
            .onNotificationIgnored(
                eq(mPushId),
                eq(category),
                eq(details),
                nullable(String.class),
                eq(CoreConstants.Transport.UNKNOWN)
            );
    }

    @Test
    public void testReportIgnoredShouldSendEventToTracker() {
        mTrackingFacade.reportIgnored(mPushId, category, details, transport);
        verify(mTracker)
            .onNotificationIgnored(eq(mPushId), eq(category), eq(details), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportIgnoredNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker)
            .onNotificationIgnored(anyString(), anyString(), anyString(), anyString(), anyString());
        mTrackingFacade.reportIgnored(mPushId, category, details);
    }

    @Test
    public void testReportIgnoredFromActionInfoShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportIgnored(mActionInfo, category, details);
        verify(mTracker)
            .onNotificationIgnored(
                eq(mPushId),
                eq(category),
                eq(details),
                nullable(String.class),
                eq(CoreConstants.Transport.UNKNOWN)
            );
    }

    @Test
    public void testReportIgnoredFromActionInfoShouldSendEventToTracker() {
        mTrackingFacade.reportIgnored(mActionInfo, category, details, transport);
        verify(mTracker)
            .onNotificationIgnored(eq(mPushId), eq(category), eq(details), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportIgnoredFromActionInfoNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker).onNotificationIgnored(anyString(), anyString(), anyString(), anyString(), anyString());
        mTrackingFacade.reportIgnored(mActionInfo, category, details);
    }

    @Test
    public void testReportIgnoredShouldFromIntentSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportIgnored(mIntent, category, details);
        verify(mTracker)
            .onNotificationIgnored(
                eq(mPushId),
                eq(category),
                eq(details),
                nullable(String.class),
                eq(CoreConstants.Transport.UNKNOWN)
            );
    }

    @Test
    public void testReportIgnoredShouldFromIntentSendEventToTracker() {
        mTrackingFacade.reportIgnored(mIntent, category, details, transport);
        verify(mTracker)
            .onNotificationIgnored(eq(mPushId), eq(category), eq(details), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportIgnoredNotShouldFromIntentThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker).onNotificationIgnored(anyString(), anyString(), anyString(), anyString(), anyString());
        mTrackingFacade.reportIgnored(mIntent, category, details);
    }
    //endregion

    //region expired
    @Test
    public void testReportExpiredShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportExpired(mPushId, category);
        verify(mTracker)
            .onNotificationExpired(
                eq(mPushId),
                eq(category),
                nullable(String.class),
                eq(CoreConstants.Transport.UNKNOWN)
            );
    }

    @Test
    public void testReportExpiredShouldSendEventToTracker() {
        mTrackingFacade.reportExpired(mPushId, category, transport);
        verify(mTracker).onNotificationExpired(eq(mPushId), eq(category), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportExpiredNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker).onNotificationExpired(anyString(), anyString(), anyString(), anyString());
        mTrackingFacade.reportExpired(mPushId, category);
    }

    @Test
    public void testReportExpiredFromActionInfoShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportExpired(mActionInfo, category);
        verify(mTracker)
            .onNotificationExpired(
                eq(mPushId),
                eq(category),
                nullable(String.class),
                eq(CoreConstants.Transport.UNKNOWN)
            );
    }

    @Test
    public void testReportExpiredFromActionInfoShouldSendEventToTracker() {
        mTrackingFacade.reportExpired(mActionInfo, category, transport);
        verify(mTracker).onNotificationExpired(eq(mPushId), eq(category), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportExpiredFromActionInfoNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker).onNotificationExpired(anyString(), anyString(), anyString(), anyString());
        mTrackingFacade.reportExpired(mActionInfo, category);
    }

    @Test
    public void testReportExpiredShouldFromIntentSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportExpired(mIntent, category);
        verify(mTracker)
            .onNotificationExpired(
                eq(mPushId),
                eq(category),
                nullable(String.class),
                eq(CoreConstants.Transport.UNKNOWN)
            );
    }

    @Test
    public void testReportExpiredShouldFromIntentSendEventToTracker() {
        mTrackingFacade.reportExpired(mIntent, category, transport);
        verify(mTracker).onNotificationExpired(eq(mPushId), eq(category), nullable(String.class), eq(transport));
    }

    @Test
    public void testReportExpiredNotShouldFromIntentThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker).onNotificationExpired(anyString(), anyString(), anyString(), anyString());
        mTrackingFacade.reportExpired(mIntent, category);
    }
    //endregion

    //region removed
    @Test
    public void testReportRemovedShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportRemoved(mPushId, category, details);
        verify(mTracker)
            .onRemovingSilentPushProcessed(
                eq(mPushId),
                eq(category),
                eq(details),
                nullable(String.class),
                eq(CoreConstants.Transport.UNKNOWN)
            );
    }

    @Test
    public void testReportRemovedShouldSendEventToTracker() {
        mTrackingFacade.reportRemoved(mPushId, category, details, transport);
        verify(mTracker)
            .onRemovingSilentPushProcessed(
                eq(mPushId),
                eq(category),
                eq(details),
                nullable(String.class),
                eq(transport)
            );
    }

    @Test
    public void testReportRemovedNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker)
            .onRemovingSilentPushProcessed(anyString(), anyString(), anyString(), anyString(), anyString());
        mTrackingFacade.reportRemoved(mPushId, category, details);
    }

    @Test
    public void testReportRemovedFromActionInfoShouldSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportRemoved(mActionInfo, category, details);
        verify(mTracker)
            .onRemovingSilentPushProcessed(
                eq(mPushId),
                eq(category),
                eq(details),
                nullable(String.class),
                eq(CoreConstants.Transport.UNKNOWN)
            );
    }

    @Test
    public void testReportRemovedFromActionInfoShouldSendEventToTracker() {
        mTrackingFacade.reportRemoved(mActionInfo, category, details, transport);
        verify(mTracker)
            .onRemovingSilentPushProcessed(
                eq(mPushId),
                eq(category),
                eq(details),
                nullable(String.class),
                eq(transport)
            );
    }

    @Test
    public void testReportRemovedFromActionInfoNotShouldThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker)
            .onRemovingSilentPushProcessed(anyString(), anyString(), anyString(), anyString(), anyString());
        mTrackingFacade.reportRemoved(mActionInfo, category, details);
    }

    @Test
    public void testReportRemovedShouldFromIntentSendEventToTrackerWithUnknownTransport() {
        mTrackingFacade.reportRemoved(mIntent, category, details);
        verify(mTracker)
            .onRemovingSilentPushProcessed(
                eq(mPushId),
                eq(category),
                eq(details),
                nullable(String.class),
                eq(CoreConstants.Transport.UNKNOWN)
            );
    }

    @Test
    public void testReportRemovedShouldFromIntentSendEventToTracker() {
        mTrackingFacade.reportRemoved(mIntent, category, details, transport);
        verify(mTracker)
            .onRemovingSilentPushProcessed(
                eq(mPushId),
                eq(category),
                eq(details),
                nullable(String.class),
                eq(transport)
            );
    }

    @Test
    public void testReportRemovedNotShouldFromIntentThrowExceptionIfTrackerHasThrown() {
        doThrow(new IllegalStateException())
            .when(mTracker)
            .onRemovingSilentPushProcessed(anyString(), anyString(), anyString(), anyString(), anyString());
        mTrackingFacade.reportRemoved(mIntent, category, details);
    }
    //endregion
}

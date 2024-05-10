package io.appmetrica.analytics.push.coreutils.internal.service;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Bundle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class PushJobServiceControllerTest {

    public static final int SERVICE_START_JOB_ID = 27117998;
    private static final String PACKAGE_NAME = "com.test.package.name";
    private static final String PUSH_JOB_SERVICE = "io.appmetrica.analytics.push.internal.service.PushJobService";

    private static final String COMMAND_BUNDLE_KEY = "Test command bundle key";
    private static final String COMMAND_BUNDLE_VALUE = "Test command bundle value";

    private Context mContext;
    private JobScheduler mJobScheduler;
    private final Bundle mCommandBundle = new Bundle();

    private PushJobServiceController mConfigurationJobServiceController;

    @Before
    public void setUp() throws Exception {
        mContext = mock(Context.class);
        when(mContext.getPackageName()).thenReturn(PACKAGE_NAME);
        mJobScheduler = mock(JobScheduler.class);
        mConfigurationJobServiceController = new PushJobServiceController(mContext, mJobScheduler);
        mCommandBundle.putString(COMMAND_BUNDLE_KEY, COMMAND_BUNDLE_VALUE);
    }
    //endregion

    //region launchService(@NonNull String packageName, @NonNull final Bundle extras)
    @Test
    public void testLaunchCommandCreatesJobInfoWithValidId() {
        mConfigurationJobServiceController.launchService(mCommandBundle);
        assertThat(interceptJobInfo().getId()).isEqualTo(SERVICE_START_JOB_ID);
    }

    @Test
    public void testLaunchCommandCreatesJobInfoWithValidPackageName() {
        mConfigurationJobServiceController.launchService(mCommandBundle);
        assertThat(interceptJobInfo().getService().getPackageName()).isEqualTo(PACKAGE_NAME);
    }

    @Test
    public void testLaunchCommandCreatesJobInfoWithValidClassName() {
        mConfigurationJobServiceController.launchService(mCommandBundle);
        assertThat(interceptJobInfo().getService().getClassName()).isEqualTo(PUSH_JOB_SERVICE);
    }

    @Test
    public void testLaunchCommandCreatesJobInfoWithValidExtras() {
        mConfigurationJobServiceController.launchService(mCommandBundle);
        assertThat(interceptJobInfo().getTransientExtras().getString(COMMAND_BUNDLE_KEY))
            .isEqualTo(COMMAND_BUNDLE_VALUE);
    }

    @Test
    public void testLaunchCommandDoNothingIfJobSchedulerReturnsSuccess() {
        when(mJobScheduler.schedule(any(JobInfo.class))).thenReturn(JobScheduler.RESULT_SUCCESS);
        mConfigurationJobServiceController.launchService(mCommandBundle);
    }

    @Test
    public void testLaunchCommandDoNothingIfJobSchedulerReturnsFailure() {
        when(mJobScheduler.schedule(any(JobInfo.class))).thenReturn(JobScheduler.RESULT_FAILURE);
        mConfigurationJobServiceController.launchService(mCommandBundle);
    }
    //endregion

    private JobInfo interceptJobInfo() {
        ArgumentCaptor<JobInfo> arg1 = ArgumentCaptor.forClass(JobInfo.class);
        verify(mJobScheduler, times(1)).schedule(arg1.capture());
        return arg1.getValue();
    }
}

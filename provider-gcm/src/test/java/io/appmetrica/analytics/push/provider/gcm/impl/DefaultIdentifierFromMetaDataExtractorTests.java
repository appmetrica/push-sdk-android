package io.appmetrica.analytics.push.provider.gcm.impl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class DefaultIdentifierFromMetaDataExtractorTests extends ExtractorTests {

    private static final String META_DATA_PROJECT_NUMBER = "ymp_gcm_project_number";

    private Context mContext;
    private ApplicationInfo mInfo;

    @Before
    public void setUp() throws PackageManager.NameNotFoundException {
        mContext = spy(RuntimeEnvironment.application);
        super.setUp(new DefaultIdentifierFromMetaDataExtractor(mContext));

        mInfo = new ApplicationInfo();
        mInfo.metaData = new Bundle();
        PackageManager pm = mock(PackageManager.class);
        doReturn(mInfo).when(pm).getApplicationInfo(anyString(), anyInt());
        doReturn(pm).when(mContext).getPackageManager();
    }

    @NonNull
    @Override
    String createSenderId() {
        String senderId = randomString();
        mInfo.metaData.putString(META_DATA_PROJECT_NUMBER, "number:" + senderId);

        return senderId;
    }
}

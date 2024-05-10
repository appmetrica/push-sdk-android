package io.appmetrica.analytics.push.provider.hms.impl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class DefaultIdentifierFromMetaDataExtractorTests extends ExtractorTests {

    private static final String META_DATA_DEFAULT_APP_ID = "ymp_hms_default_app_id";

    private ApplicationInfo mInfo;

    @Before
    public void setUp() throws PackageManager.NameNotFoundException {
        Context mContext = spy(RuntimeEnvironment.application);
        super.setUp(new DefaultIdentifierFromMetaDataExtractor(mContext));

        mInfo = new ApplicationInfo();
        mInfo.metaData = new Bundle();
        PackageManager pm = mock(PackageManager.class);
        doReturn(mInfo).when(pm).getApplicationInfo(anyString(), anyInt());
        doReturn(pm).when(mContext).getPackageManager();
    }

    @NonNull
    @Override
    String createAppId() {
        String appId = "1455";
        mInfo.metaData.putString(META_DATA_DEFAULT_APP_ID, "number:" + appId);

        return appId;
    }

}

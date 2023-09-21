package io.appmetrica.analytics.push.provider.rustore.impl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class DefaultIdentifierFromMetaDataExtractorTest extends ExtractorTests {

    private static final String META_DATA_DEFAULT_PROJECT_ID = "ymp_rustore_default_project_id";

    @Mock
    private Context context;

    private ApplicationInfo applicationInfo;

    @Before
    public void setUp() throws PackageManager.NameNotFoundException {
        MockitoAnnotations.openMocks(this);
        super.setUp(new DefaultIdentifierFromMetaDataExtractor(context));

        applicationInfo = new ApplicationInfo();
        applicationInfo.metaData = new Bundle();

        PackageManager packageManager = mock(PackageManager.class);
        doReturn(packageManager).when(context).getPackageManager();
        doReturn("packageName").when(context).getPackageName();
        doReturn(applicationInfo).when(packageManager).getApplicationInfo(anyString(), anyInt());
    }

    @NonNull
    @Override
    String createProjectId() {
        String projectId = randomString();
        applicationInfo.metaData.putString(META_DATA_DEFAULT_PROJECT_ID, projectId);

        return projectId;
    }
}

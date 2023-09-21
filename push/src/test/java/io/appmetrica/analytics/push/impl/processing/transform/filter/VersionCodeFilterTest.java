package io.appmetrica.analytics.push.impl.processing.transform.filter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.appmetrica.analytics.push.model.Filters;
import io.appmetrica.analytics.push.model.PushMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static io.appmetrica.analytics.push.testutils.Rand.randomInt;
import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class VersionCodeFilterTest extends PushFilterTest {

    private Context context;
    private PackageManager packageManager;
    private PackageInfo packageInfo;
    private int versionCode;

    @Before
    public void setUp() throws Exception {
        context = mock(Context.class);
        when(context.getPackageName()).thenReturn("io.appmetrica.analytics");
        super.setUp(new VersionCodeFilter(context));
        packageManager = mock(PackageManager.class);
        when(context.getPackageManager()).thenReturn(packageManager);
        packageInfo = mock(PackageInfo.class);
        when(packageManager.getPackageInfo(anyString(), anyInt())).thenReturn(packageInfo);
        versionCode = randomInt();
        packageInfo.versionCode = versionCode;
    }

    @Test
    public void testNoFilters() {
        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getFilters()).thenReturn(null);
        assertShow(pushMessage);
    }

    @Test
    public void testNoVersionCode() throws PackageManager.NameNotFoundException {
        when(packageManager.getPackageInfo(anyString(), anyInt()))
            .thenThrow(new PackageManager.NameNotFoundException(randomString()));
        //Check that the version code is -1
        assertShow(createPushMessage(-1, -1));
    }

    @Test
    public void testMinVersionCode() {
        assertShow(createPushMessage(versionCode, null));
        assertSilence(createPushMessage(versionCode + 1, null));
    }

    @Test
    public void testMaxVersionCode() {
        assertShow(createPushMessage(null, versionCode));
        assertSilence(createPushMessage(null, versionCode - 1));
    }

    @NonNull
    private PushMessage createPushMessage(@Nullable Integer minVersionCode, @Nullable Integer maxVersionCode) {
        Filters filters = mock(Filters.class);
        when(filters.getMinVersionCode()).thenReturn(minVersionCode);
        when(filters.getMaxVersionCode()).thenReturn(maxVersionCode);

        PushMessage pushMessage = mock(PushMessage.class);
        when(pushMessage.getFilters()).thenReturn(filters);
        return pushMessage;
    }
}

package io.appmetrica.analytics.push.provider.hms.impl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class IdentifierExtractorTests {

    private Identifier mIdentifier;
    private IdentifierExtractor mIdentifierExtractor;

    @Before
    public void setUp() throws PackageManager.NameNotFoundException {
        Context mContext = spy(RuntimeEnvironment.application);
        mIdentifier = new Identifier(randomString());
        mIdentifierExtractor = new MockableIdentifierExtractor(mContext, mIdentifier);
        ApplicationInfo mApplicationInfo = new ApplicationInfo();
        PackageManager pm = mock(PackageManager.class);
        doReturn(mApplicationInfo).when(pm).getApplicationInfo(anyString(), anyInt());
        doReturn(pm).when(mContext).getPackageManager();
    }

    @Test
    public void testExtractIdentifier() {
        Identifier identifier = mIdentifierExtractor.extractIdentifier();

        assertThat(identifier).isEqualToComparingFieldByField(mIdentifier);
    }

}

package io.appmetrica.analytics.push.provider.firebase.impl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

    private static final String META_DATA_NAME = "SENDER_ID";

    private Context mContext;
    private Identifier mIdentifier;
    private IdentifierExtractor mIdentifierExtractor;
    private ApplicationInfo mApplicationInfo;
    private String mSenderId;

    @Before
    public void setUp() throws PackageManager.NameNotFoundException {
        mContext = spy(RuntimeEnvironment.application);
        mIdentifier = new Identifier(randomString(), randomString(), randomString(), randomString());
        mIdentifierExtractor = new MockableIdentifierExtractor(mContext, mIdentifier);
        mApplicationInfo = new ApplicationInfo();
        mApplicationInfo.metaData = new Bundle();
        PackageManager pm = mock(PackageManager.class);
        doReturn(mApplicationInfo).when(pm).getApplicationInfo(anyString(), anyInt());
        doReturn(pm).when(mContext).getPackageManager();
        mSenderId = "1234567";
    }

    @Test
    public void testGetSenderIdFromMetaDataIfSenderIdValid() {
        mApplicationInfo.metaData.putString(META_DATA_NAME, "number:" + mSenderId);

        String senderId = mIdentifierExtractor.getSenderIdFromMetaData(mContext, META_DATA_NAME);
        assertThat(senderId).isEqualTo(mSenderId);
    }

    @Test
    public void testGetSenderIdFromMetaDataIfSenderIdNotFound() {
        String senderId = mIdentifierExtractor.getSenderIdFromMetaData(mContext, META_DATA_NAME);
        assertThat(senderId).isEqualTo(null);
    }

    @Test
    public void testGetSenderIdFromMetaDataIfWithoutNumber() {
        mApplicationInfo.metaData.putString(META_DATA_NAME, mSenderId);

        String senderId = mIdentifierExtractor.getSenderIdFromMetaData(mContext, META_DATA_NAME);
        assertThat(senderId).isEqualTo(null);
    }

    @Test
    public void testGetSenderIdFromMetaDataWithAnotherLength() {
        mApplicationInfo.metaData.putString(META_DATA_NAME, "id:number:" + mSenderId);

        String senderId = mIdentifierExtractor.getSenderIdFromMetaData(mContext, META_DATA_NAME);
        assertThat(senderId).isEqualTo(null);
    }

    @Test
    public void testGetSenderIdFromMetaDataWithAnotherPrefix() {
        mApplicationInfo.metaData.putString(META_DATA_NAME, "id:" + mSenderId);

        String senderId = mIdentifierExtractor.getSenderIdFromMetaData(mContext, META_DATA_NAME);
        assertThat(senderId).isEqualTo(null);
    }

    @Test
    public void testGetSenderIdFromMetaDataWithEmptySenderId() {
        mApplicationInfo.metaData.putString(META_DATA_NAME, "number:");

        String senderId = mIdentifierExtractor.getSenderIdFromMetaData(mContext, META_DATA_NAME);
        assertThat(senderId).isEqualTo(null);
    }

    @Test
    public void testExtractIdentifier() {
        Identifier identifier = mIdentifierExtractor.extractIdentifier();

        assertThat(identifier).isEqualToComparingFieldByField(mIdentifier);
    }

}

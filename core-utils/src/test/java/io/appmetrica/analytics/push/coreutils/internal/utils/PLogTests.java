package io.appmetrica.analytics.push.coreutils.internal.utils;

import io.appmetrica.analytics.push.testutils.RandomStringGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PLogTests {

    @Test(expected = Test.None.class)
    public void testDShouldNotThrowExceptionIfMsgIsNull() {
        PLog.d(null, new RandomStringGenerator().nextString());
    }

    @Test(expected = Test.None.class)
    public void testIShouldNotThrowExceptionIfMsgIsNull() {
        PLog.i(null, new RandomStringGenerator().nextString());
    }

    @Test(expected = Test.None.class)
    public void testWShouldNotThrowExceptionIfMsgIsNull() {
        PLog.w(null, new RandomStringGenerator().nextString());
    }

    @Test(expected = Test.None.class)
    public void testEShouldNotThrowExceptionIfMsgIsNull() {
        PLog.e((String) null, new RandomStringGenerator().nextString());
    }

    @Test
    public void testEWithExceptionShouldNotThrowExceptionIfMsgIsNull() {
        PLog.e(new Throwable(), null, new RandomStringGenerator().nextString());
    }
}

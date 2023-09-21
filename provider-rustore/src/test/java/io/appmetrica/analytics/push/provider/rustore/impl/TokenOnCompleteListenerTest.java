package io.appmetrica.analytics.push.provider.rustore.impl;

import java.util.concurrent.CountDownLatch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static io.appmetrica.analytics.push.testutils.Rand.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class TokenOnCompleteListenerTest {

    @Mock
    private CountDownLatch countDownLatch;

    private TokenOnCompleteListener listener;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        listener = new TokenOnCompleteListener(countDownLatch);
    }

    @Test
    public void onSuccess() {
        final String token = randomString();
        listener.onSuccess(token);

        final TokenResult tokenResult = listener.getTokenResult();
        assertThat(tokenResult).isNotNull();
        assertThat(tokenResult.token).isEqualTo(token);
        assertThat(tokenResult.exception).isNull();
        verify(countDownLatch).countDown();
    }

    @Test
    public void onFailure() {
        final Throwable throwable = new Throwable();
        listener.onFailure(throwable);

        final TokenResult tokenResult = listener.getTokenResult();
        assertThat(tokenResult).isNotNull();
        assertThat(tokenResult.token).isNull();
        assertThat(tokenResult.exception).isEqualTo(throwable);
        verify(countDownLatch).countDown();
    }
}

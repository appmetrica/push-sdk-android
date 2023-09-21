package io.appmetrica.analytics.push.provider.rustore.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class TokenResultTest {

    @Test
    public void resultIsSuccessfulIfTokenIsNotNullAndExceptionIsNull() {
        final TokenResult tokenResult = new TokenResult("token", null);
        assertThat(tokenResult.isSuccessful()).isTrue();
    }

    @Test
    public void resultIsNotSuccessfulIfExceptionIsNotNull() {
        final TokenResult tokenResult = new TokenResult("token", new Throwable());
        assertThat(tokenResult.isSuccessful()).isFalse();
    }

    @Test
    public void resultIsNotSuccessfulIfTokenIsNull() {
        final TokenResult tokenResult = new TokenResult(null, null);
        assertThat(tokenResult.isSuccessful()).isFalse();
    }
}

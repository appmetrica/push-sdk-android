package io.appmetrica.analytics.push.impl.token

import android.content.Context
import io.appmetrica.analytics.push.TokenUpdateListener
import io.appmetrica.analytics.push.impl.AppMetricaPushCore
import io.appmetrica.analytics.push.impl.PreferenceManager
import io.appmetrica.analytics.push.impl.storage.Token
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.on
import io.appmetrica.analytics.push.testutils.staticRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class TokenManagerTest : CommonTest() {

    private val preferenceTokens: Map<String, Token> = mutableMapOf(
        "provider1" to Token("token1", 1000),
        "provider2" to Token("token2", 2000)
    )
    private val preferenceTokensString = "preferenceTokens"
    @get:Rule
    val tokenRule = staticRule<Token> {
        on { Token.parseTokens(preferenceTokensString) } doReturn preferenceTokens
        on { Token.saveToString(any()) } doReturn "Token.saveToString"
    }

    private val context: Context = mock()

    private val preferenceManager: PreferenceManager = mock {
        on { tokens } doReturn preferenceTokensString
    }
    private val tokenUpdateListener: TokenUpdateListener = mock()
    private val appMetricaPushCore: AppMetricaPushCore = mock {
        on { preferenceManager } doReturn preferenceManager
        on { tokenUpdateListener } doReturn tokenUpdateListener
    }
    @get:Rule
    val appMetricaPushCoreRule = staticRule<AppMetricaPushCore> {
        on { AppMetricaPushCore.getInstance(context) } doReturn appMetricaPushCore
    }

    private val providerIds = setOf("provider1")
    private val tokenManager = TokenManager(context, providerIds)

    @Test
    fun saveToken() {
        val currentTime = System.currentTimeMillis()

        tokenManager.saveToken("new token", "provider1", currentTime)

        verify(preferenceManager).tokens
        verify(preferenceManager).saveTokens("Token.saveToString")
        verify(tokenUpdateListener).onTokenUpdated(mutableMapOf("provider1" to "new token"))

        val tokens = tokenManager.getTokens()

        verifyNoMoreInteractions(preferenceManager, tokenUpdateListener)

        val newToken = tokens["provider1"]!!
        assertThat(newToken.token).isEqualTo("new token")
        assertThat(newToken.lastUpdateTime).isEqualTo(currentTime)
    }

    @Test
    fun saveTokenTwiceDoesNotReloadsItFromPreferences() {
        tokenManager.saveToken("new token1", "provider1", System.currentTimeMillis())

        val provider2 = "provider2"
        val token2 = "new token2"
        val currentTime2 = System.currentTimeMillis()

        tokenManager.saveToken(token2, provider2, currentTime2)

        verify(preferenceManager, times(1)).tokens
    }

    @Test
    fun saveTokenIfWrongProvider() {
        tokenManager.saveToken("new token", "provider2", System.currentTimeMillis())

        verifyNoMoreInteractions(preferenceManager, tokenUpdateListener)
    }

    @Test
    fun getToken() {
        val newToken1 = tokenManager.getToken("provider1")!!

        verify(preferenceManager).tokens
        verifyNoMoreInteractions(preferenceManager, tokenUpdateListener)
        assertThat(newToken1.token).isEqualTo("token1")

        val newToken2 = tokenManager.getToken("provider1")!!

        verifyNoMoreInteractions(preferenceManager, tokenUpdateListener)
        assertThat(newToken2.token).isEqualTo("token1")
    }

    @Test
    fun getTokens() {
        val tokens1 = tokenManager.getTokens()
        val newToken1 = tokens1["provider1"]!!

        verify(preferenceManager).tokens
        verifyNoMoreInteractions(preferenceManager, tokenUpdateListener)
        assertThat(newToken1.token).isEqualTo("token1")

        val tokens2 = tokenManager.getTokens()
        val newToken2 = tokens2["provider1"]!!

        verifyNoMoreInteractions(preferenceManager, tokenUpdateListener)
        assertThat(newToken2.token).isEqualTo("token1")
    }

    @Test
    fun getPlainTokens() {
        val tokens1 = tokenManager.getPlainTokens()
        val newToken1 = tokens1["provider1"]!!

        verify(preferenceManager).tokens
        verifyNoMoreInteractions(preferenceManager, tokenUpdateListener)
        assertThat(newToken1).isEqualTo("token1")

        val tokens2 = tokenManager.getPlainTokens()
        val newToken2 = tokens2["provider1"]!!

        verifyNoMoreInteractions(preferenceManager, tokenUpdateListener)
        assertThat(newToken2).isEqualTo("token1")
    }

    @Test
    fun init() {
        tokenManager.init()

        verify(preferenceManager).tokens
        verify(tokenUpdateListener).onTokenUpdated(mutableMapOf("provider1" to "token1"))
        verifyNoMoreInteractions(preferenceManager, tokenUpdateListener)

        val newToken = tokenManager.getTokens()["provider1"]!!

        verifyNoMoreInteractions(preferenceManager, tokenUpdateListener)
        assertThat(newToken.token).isEqualTo("token1")
    }
}

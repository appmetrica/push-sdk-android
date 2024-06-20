package io.appmetrica.analytics.push.impl.command

import io.appmetrica.analytics.push.coreutils.internal.PushServiceFacade
import io.appmetrica.analytics.push.testutils.CommonTest
import io.appmetrica.analytics.push.testutils.constructionRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

internal class CommandHolderTest : CommonTest() {

    @get:Rule
    val initPushServiceCommandMockedConstructionRule = constructionRule<InitPushServiceCommand>()

    @get:Rule
    val initPushTokenCommandMockedConstructionRule = constructionRule<InitPushTokenCommand>()

    @get:Rule
    val updatePushTokenCommandMockedConstructionRule = constructionRule<UpdatePushTokenCommand>()

    @get:Rule
    val processPushCommandMockedConstructionRule = constructionRule<ProcessPushCommand>()

    @get:Rule
    val commandWithProcessingMinTimeMockedConstructionRule = constructionRule<CommandWithProcessingMinTime>()

    @get:Rule
    val processPushCommandMinTimeProviderMockedConstructionRule = constructionRule<ProcessPushCommandMinTimeProvider>()

    @get:Rule
    val sendTokenCommandMockedConstructionRule = constructionRule<SendTokenCommand>()

    @get:Rule
    val sendTokenProcessingMinTimeProviderMockedConstructionRule =
        constructionRule<SendTokenProcessingMinTimeProvider>()

    private val commandHolder: CommandHolder by setUp { CommandHolder() }

    @Test
    fun `get for COMMAND_INIT_PUSH_SERVICE`() {
        assertThat(commandHolder[PushServiceFacade.COMMAND_INIT_PUSH_SERVICE])
            .isEqualTo(initPushServiceCommandMockedConstructionRule.constructionMock.constructed().first())
    }

    @Test
    fun `get for COMMAND_INIT_PUSH_TOKEN`() {
        assertThat(commandHolder[PushServiceFacade.COMMAND_INIT_PUSH_TOKEN])
            .isEqualTo(initPushTokenCommandMockedConstructionRule.constructionMock.constructed().first())
    }

    @Test
    fun `get for COMMAND_UPDATE_TOKEN`() {
        assertThat(commandHolder[PushServiceFacade.COMMAND_UPDATE_TOKEN])
            .isEqualTo(commandWithProcessingMinTimeMockedConstructionRule.constructionMock.constructed().first())
        assertThat(commandWithProcessingMinTimeMockedConstructionRule.argumentInterceptor.arguments.first())
            .containsExactly(
                updatePushTokenCommandMockedConstructionRule.constructionMock.constructed().first(),
                sendTokenProcessingMinTimeProviderMockedConstructionRule.constructionMock.constructed().first()
            )
    }

    @Test
    fun `get for COMMAND_PROCESS_PUSH`() {
        assertThat(commandHolder[PushServiceFacade.COMMAND_PROCESS_PUSH])
            .isEqualTo(commandWithProcessingMinTimeMockedConstructionRule.constructionMock.constructed()[1])
        assertThat(commandWithProcessingMinTimeMockedConstructionRule.argumentInterceptor.arguments[1])
            .containsExactly(
                processPushCommandMockedConstructionRule.constructionMock.constructed().first(),
                processPushCommandMinTimeProviderMockedConstructionRule.constructionMock.constructed().first()
            )
    }

    @Test
    fun `get for COMMAND_SEND_PUSH_TOKEN_ON_REFRESH`() {
        assertThat(commandHolder[PushServiceFacade.COMMAND_SEND_PUSH_TOKEN_ON_REFRESH])
            .isEqualTo(commandWithProcessingMinTimeMockedConstructionRule.constructionMock.constructed()[2])
        assertThat(commandWithProcessingMinTimeMockedConstructionRule.argumentInterceptor.arguments[2])
            .containsExactly(
                sendTokenCommandMockedConstructionRule.constructionMock.constructed().first(),
                sendTokenProcessingMinTimeProviderMockedConstructionRule.constructionMock.constructed()[1]
            )
    }

    @Test
    fun `get for unknown`() {
        assertThat(commandHolder["unknown"]).isNull()
    }
}

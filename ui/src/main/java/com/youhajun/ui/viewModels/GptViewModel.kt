package com.youhajun.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.data.onSuccess
import com.youhajun.data.repositories.GptRepository
import com.youhajun.model_ui.types.gpt.GptAIType
import com.youhajun.model_ui.types.gpt.GptMessageType
import com.youhajun.model_ui.types.gpt.GptRoleType
import com.youhajun.model_ui.types.gpt.GptType
import com.youhajun.model_ui.vo.gpt.ChatGptMessageVo
import com.youhajun.model_ui.vo.gpt.ChatGptRequestVo
import com.youhajun.model_ui.vo.gpt.ChatGptRequestVo.Companion.toDto
import com.youhajun.model_ui.vo.gpt.ChatGptResponseVo
import com.youhajun.model_ui.vo.gpt.ChatGptResponseVo.Companion.toModel
import com.youhajun.model_ui.vo.gpt.GptAssistantVo
import com.youhajun.model_ui.vo.gpt.GptAssistantVo.Companion.toDto
import com.youhajun.model_ui.vo.gpt.GptAssistantVo.Companion.toModel
import com.youhajun.model_ui.vo.gpt.GptChannelVo
import com.youhajun.model_ui.vo.gpt.GptChannelVo.Companion.toDto
import com.youhajun.model_ui.vo.gpt.GptChannelVo.Companion.toModel
import com.youhajun.model_ui.vo.gpt.GptMessageVo
import com.youhajun.model_ui.vo.gpt.GptMessageVo.Companion.toDto
import com.youhajun.model_ui.vo.gpt.GptMessageVo.Companion.toModel
import com.youhajun.model_ui.vo.gpt.UpdateGptChannelInfoRequestVo
import com.youhajun.model_ui.vo.gpt.UpdateGptChannelInfoRequestVo.Companion.toDto
import com.youhajun.ui.R
import com.youhajun.model_ui.sideEffects.GptSideEffect
import com.youhajun.model_ui.states.GptState
import com.youhajun.ui.utils.ResourceProviderUtil
import com.youhajun.ui.utils.TimeStampUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

private interface GptIntent {
    fun onChangedGptInput(input: String)
    fun onChangedAddRoleInput(input: String)
    fun onClickSendAnswer()
    fun onClickGptType(gptType: GptType)
    fun onClickRoleExpandIcon()
    fun onClickRole(role: String)
    fun onClickAddRole()
    fun onClickDeleteRole(role: String)
    fun onClickHeaderBackIcon()
    fun onClickHeaderMenuIcon()
    fun onClickChannel(idx: Long)
    fun onClickCreateChannel()
    fun onClickDeleteChannel(idx: Long)
}

@HiltViewModel
class GptViewModel @Inject constructor(
    private val resourceProviderUtil: ResourceProviderUtil,
    private val gptRepository: GptRepository,
) : ContainerHost<GptState, GptSideEffect>, ViewModel(), GptIntent {

    private val _gptInputStateOf: MutableState<String> = mutableStateOf("")
    val gptInputStateOf: State<String> = _gptInputStateOf
    private val _addRoleInputStateOf: MutableState<String> = mutableStateOf("")
    val addRoleInputStateOf: State<String> = _addRoleInputStateOf
    private var currentChannelIdx: Long? = null
    private var channelCollectJob: Job? = null

    override val container: Container<GptState, GptSideEffect> = container(GptState()) {
        onCollectAllRoles()
        onCollectAllChannels()
        initInsertNewChannelCheck()
    }

    override fun onClickHeaderBackIcon() {
        intent { postSideEffect(GptSideEffect.Navigation.NavigateUp) }
    }

    override fun onClickHeaderMenuIcon() {
        intent {
            if (state.drawerState.isClosed) postSideEffect(GptSideEffect.DrawerMenuOpen)
            else postSideEffect(GptSideEffect.DrawerMenuClose)
        }
    }

    override fun onClickChannel(idx: Long) {
        onChannelChanged(idx)
        intent {
            postSideEffect(GptSideEffect.DrawerMenuClose)
        }
    }

    override fun onClickDeleteChannel(idx: Long) {
        intent {
            val isLastChannel = state.gptChannelList.size == 1
            val isCurrentChannel = idx == state.currentGptChannel?.channelIdx

            viewModelScope.launch {
                gptRepository.deleteGptChannel(idx)
                if (isCurrentChannel && !isLastChannel) insertNewChannel()
            }
        }
    }

    override fun onClickCreateChannel() {
        insertNewChannel()
    }

    override fun onChangedGptInput(input: String) {
        _gptInputStateOf.value = input
    }

    override fun onChangedAddRoleInput(input: String) {
        _addRoleInputStateOf.value = input
    }

    override fun onClickSendAnswer() {
        val message = _gptInputStateOf.value
        if (message.isEmpty()) return

        intent {
            val currentChannel = state.currentGptChannel ?: return@intent
            val gptType =
                if (currentChannel.gptType == GptType.NONE) state.selectedGptType else currentChannel.gptType
            val currentRole =
                if (currentChannel.gptType == GptType.NONE) state.selectedRole else currentChannel.roleOfAi

            when (gptType.toGptAiType()) {
                GptAIType.NONE -> {}
                GptAIType.CHAT_GPT -> launcherChatGptPrompt(message, gptType, currentRole)
                GptAIType.GEMINI -> TODO()
            }
        }
    }

    override fun onClickGptType(gptType: GptType) {
        intent {
            reduce { state.copy(selectedGptType = gptType) }
        }
    }

    override fun onClickAddRole() {
        intent {
            viewModelScope.launch {
                val role = _addRoleInputStateOf.value
                gptRepository.insertGptRole(role)
                postSideEffect(GptSideEffect.HideKeyboard)
                _addRoleInputStateOf.value = ""
            }
        }
    }

    override fun onClickDeleteRole(role: String) {
        viewModelScope.launch {
            gptRepository.deleteGptRole(role)
        }
    }

    override fun onClickRole(role: String) {
        intent {
            reduce { state.copy(selectedRole = role) }
        }
    }

    override fun onClickRoleExpandIcon() {
        intent {
            reduce { state.copy(isRoleExpanded = !state.isRoleExpanded) }
        }
    }

    private fun onCollectAllRoles() {
        intent {
            viewModelScope.launch {
                gptRepository.selectAllGptRoles().collect {
                    reduce {
                        state.copy(roleList = it.map { it.role }.toImmutableList())
                    }
                }
            }
        }
    }

    private fun onCollectAllChannels() {
        intent {
            viewModelScope.launch {
                gptRepository.selectAllGptChannels().onEach {
                    if (it.isEmpty()) insertNewChannel()
                }.collect {
                    reduce {
                        state.copy(gptChannelList = it.map { it.toModel() }.toImmutableList())
                    }
                }
            }
        }
    }

    private fun onChannelChanged(channelIdx: Long) {
        currentChannelIdx = channelIdx
        channelCollectJob?.cancel()

        channelCollectJob = viewModelScope.launch {
            launch { initChannel() }
            launch { onCollectGptChannel(channelIdx) }
            launch { onCollectAllMessages(channelIdx) }
            launch { onCollectAllAssistant(channelIdx) }
        }
    }

    private suspend fun initChannel() {
        intent {
            reduce {
                state.copy(
                    selectedGptType = GptType.CHAT_GPT_3_5_TURBO,
                    selectedRole = "",
                    isRoleExpanded = false,
                )
            }
        }
    }

    private suspend fun onCollectGptChannel(idx: Long) = intent {
        gptRepository.selectGptChannel(idx).collect {
            reduce { state.copy(currentGptChannel = it.toModel()) }
        }
    }

    private suspend fun onCollectAllMessages(idx: Long) = intent {
        gptRepository.selectAllGptMessages(idx).collect {
            reduce {
                state.copy(currentGptMessages = it.map { it.toModel() }.toImmutableList())
            }
        }
    }

    private suspend fun onCollectAllAssistant(idx: Long) = intent {
        gptRepository.selectAllGptAssistants(idx).collect {
            reduce {
                state.copy(currentGptAssistants = it.map { it.toModel() }.toImmutableList())
            }
        }
    }

    private fun launcherChatGptPrompt(message: String, gptType: GptType, role: String?) = intent {
        val prefixPrompt = makePrefixPrompt(role)
        val assistantPrompt = makeAssistantPrompt(state.currentGptAssistants)
        val userPrompt = makeUserPrompt(message)
        val prompt = prefixPrompt + assistantPrompt + userPrompt
        val request = ChatGptRequestVo(gptType, prompt)
        onPostChatGptPrompt(request, message, role)
    }

    private fun makePrefixPrompt(role: String?): List<ChatGptMessageVo> {
        if (role.isNullOrEmpty()) return emptyList()
        val prefixPrompt = resourceProviderUtil.string(R.string.gpt_prompt_prefix, role)
        return listOf(ChatGptMessageVo(GptRoleType.SYSTEM, prefixPrompt))
    }

    private fun makeAssistantPrompt(list: List<GptAssistantVo>): List<ChatGptMessageVo> =
        list.map { ChatGptMessageVo(GptRoleType.ASSISTANT, it.assistantMessage) }

    private fun makeUserPrompt(message: String): List<ChatGptMessageVo> =
        listOf(ChatGptMessageVo(GptRoleType.USER, message))

    private fun onPostChatGptPrompt(request: ChatGptRequestVo, message: String, role: String?) =
        intent {
            viewModelScope.launch {
                insertMessage(message, GptMessageType.QUESTION, TimeStampUtil.currentTimestamp)
                clearGptInput()
                updateChannelLastQuestion(message, role, request.type)
                gptRepository.postChatGptPrompt(request.toDto()).onSuccess {
                    handlePostChatGptSuccess(it.toModel())
                }
            }
        }

    private fun handlePostChatGptSuccess(chatGptResponseVo: ChatGptResponseVo) {
        val message = chatGptResponseVo.message.first().messageVo.content
        val createdAt = chatGptResponseVo.createdAtUnixTimestamp
        insertAssistant(message, createdAt)
        insertMessage(message, GptMessageType.ANSWER, createdAt)
    }

    private fun insertAssistant(assistantMessage: String, createdAt: Long) {
        viewModelScope.launch {
            currentChannelIdx?.let { channelIdx ->
                val request = GptAssistantVo(
                    channelIdx = channelIdx,
                    assistantMessage = assistantMessage,
                    createdAtUnixTimeStamp = createdAt
                )

                gptRepository.insertGptAssistant(request.toDto())
            }
        }
    }

    private fun insertMessage(message: String, messageType: GptMessageType, createdAt: Long) =
        intent {
            currentChannelIdx?.let { channelIdx ->
                viewModelScope.launch {
                    val request = GptMessageVo(
                        channelIdx = channelIdx,
                        gptMessageType = messageType,
                        message = message,
                        createdAtUnixTimeStamp = createdAt
                    )
                    val insertIdx = gptRepository.insertGptMessage(request.toDto())
                    if (messageType == GptMessageType.ANSWER) {
                        postSideEffect(GptSideEffect.RunTypingAnimation(insertIdx))
                    }
                }
            }
        }

    private fun insertNewChannel() = intent {
        viewModelScope.launch {
            reduce { state.copy(onLoading = true) }
            val newChannel = GptChannelVo(
                gptType = GptType.NONE,
                createdAtUnixTimeStamp = TimeStampUtil.currentTimestamp
            )
            val insertIdx = gptRepository.insertGptChannel(newChannel.toDto())
            onChannelChanged(insertIdx)
            reduce { state.copy(onLoading = false) }
        }
    }

    private fun updateChannelLastQuestion(question: String, roleOfAi: String?, gptType: GptType) {
        currentChannelIdx?.let { channelIdx ->
            val request = UpdateGptChannelInfoRequestVo(channelIdx, gptType, roleOfAi, question)
            viewModelScope.launch {
                gptRepository.updateGptChannelInfo(request.toDto())
            }
        }
    }

    private fun initInsertNewChannelCheck() = intent {
        viewModelScope.launch {
            val channel = gptRepository.selectLatestChannel().first().toModel()
            val isNewCreate = channel.gptType != GptType.NONE
            if (isNewCreate) insertNewChannel()
            else onChannelChanged(channel.channelIdx)
        }
    }

    private fun clearGptInput() = intent {
        _gptInputStateOf.value = ""
        postSideEffect(GptSideEffect.HideKeyboard)
    }
}
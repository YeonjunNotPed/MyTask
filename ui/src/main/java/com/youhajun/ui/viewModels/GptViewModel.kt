package com.youhajun.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.enums.GptAiType
import com.youhajun.domain.models.enums.GptMessageType
import com.youhajun.domain.models.enums.GptRoleType
import com.youhajun.domain.models.enums.GptType
import com.youhajun.domain.models.inspectUiState
import com.youhajun.domain.models.vo.gpt.ChatGptMessageVo
import com.youhajun.domain.models.vo.gpt.ChatGptRequestVo
import com.youhajun.domain.models.vo.gpt.ChatGptResponseVo
import com.youhajun.domain.models.vo.gpt.GptAssistantVo
import com.youhajun.domain.models.vo.gpt.GptChannelVo
import com.youhajun.domain.models.vo.gpt.GptMessageVo
import com.youhajun.domain.models.vo.gpt.UpdateGptChannelInfoRequestVo
import com.youhajun.domain.usecase.gpt.DeleteGptChannelUseCase
import com.youhajun.domain.usecase.gpt.DeleteGptRoleUseCase
import com.youhajun.domain.usecase.gpt.InsertGptAssistantUseCase
import com.youhajun.domain.usecase.gpt.InsertGptChannelUseCase
import com.youhajun.domain.usecase.gpt.InsertGptMessageUseCase
import com.youhajun.domain.usecase.gpt.InsertGptRoleUseCase
import com.youhajun.domain.usecase.gpt.PostChatGptPromptUseCase
import com.youhajun.domain.usecase.gpt.SelectAllGptAssistantsUseCase
import com.youhajun.domain.usecase.gpt.SelectAllGptChannelsUseCase
import com.youhajun.domain.usecase.gpt.SelectAllGptMessagesUseCase
import com.youhajun.domain.usecase.gpt.SelectAllGptRolesUseCase
import com.youhajun.domain.usecase.gpt.SelectGptChannelUseCase
import com.youhajun.domain.usecase.gpt.SelectLatestChannelsUseCase
import com.youhajun.domain.usecase.gpt.UpdateGptChannelInfoUseCase
import com.youhajun.ui.R
import com.youhajun.ui.models.sideEffects.GptSideEffect
import com.youhajun.ui.models.states.GptState
import com.youhajun.ui.utils.ResourceProviderUtil
import com.youhajun.ui.utils.TimeStampUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

interface GptIntent {
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
    private val postChatGptPromptUseCase: PostChatGptPromptUseCase,
    private val insertGptRoleUseCase: InsertGptRoleUseCase,
    private val insertGptChannelUseCase: InsertGptChannelUseCase,
    private val insertGptAssistantUseCase: InsertGptAssistantUseCase,
    private val insertGptMessagesUseCase: InsertGptMessageUseCase,
    private val selectAllGptRolesUseCase: SelectAllGptRolesUseCase,
    private val selectAllGptChannelsUseCase: SelectAllGptChannelsUseCase,
    private val selectAllGptMessagesUseCase: SelectAllGptMessagesUseCase,
    private val selectAllGptAssistantsUseCase: SelectAllGptAssistantsUseCase,
    private val selectLatestGptChannelsUseCase: SelectLatestChannelsUseCase,
    private val selectGptChannelUseCase: SelectGptChannelUseCase,
    private val deleteGptRoleUseCase: DeleteGptRoleUseCase,
    private val deleteGptChannelUseCase: DeleteGptChannelUseCase,
    private val updateGptChannelInfoUseCase: UpdateGptChannelInfoUseCase,
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
                deleteGptChannelUseCase(idx).first().inspectUiState {
                    if (isCurrentChannel && !isLastChannel) insertNewChannel()
                }
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

            when (GptAiType.gptTypeOf(gptType)) {
                GptAiType.NONE -> {}
                GptAiType.CHAT_GPT -> {
                    launcherChatGptPrompt(message, gptType, currentRole)
                }

                GptAiType.GEMINI -> TODO()
            }
        }
    }

    override fun onClickGptType(gptType: GptType) {
        intent {
            reduce { state.copy(selectedGptType = gptType) }
        }
    }

    override fun onClickAddRole() {
        val role = _addRoleInputStateOf.value
        viewModelScope.launch {
            insertGptRoleUseCase(role).first().inspectUiState {
                intent {
                    postSideEffect(GptSideEffect.HideKeyboard)
                    _addRoleInputStateOf.value = ""
                }
            }
        }
    }

    override fun onClickDeleteRole(role: String) {
        viewModelScope.launch {
            deleteGptRoleUseCase(role).first()
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
                selectAllGptRolesUseCase(Unit).collect {
                    it.inspectUiState {
                        reduce { state.copy(roleList = it) }
                    }
                }
            }
        }
    }

    private fun onCollectAllChannels() {
        intent {
            viewModelScope.launch {
                selectAllGptChannelsUseCase(Unit)
                    .onEach {
                        it.inspectUiState {
                            if (it.isEmpty()) insertNewChannel()
                        }
                    }
                    .collect {
                        it.inspectUiState {
                            reduce { state.copy(gptChannelList = it) }
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

    private suspend fun onCollectGptChannel(idx: Long) {
        selectGptChannelUseCase(idx).collect {
            it.inspectUiState {
                intent {
                    reduce { state.copy(currentGptChannel = it) }
                }
            }
        }
    }

    private suspend fun onCollectAllMessages(idx: Long) {
        selectAllGptMessagesUseCase(idx).collect {
            it.inspectUiState {
                intent {
                    reduce { state.copy(currentGptMessages = it) }
                }
            }
        }
    }

    private suspend fun onCollectAllAssistant(idx: Long) {
        selectAllGptAssistantsUseCase(idx).collect {
            it.inspectUiState {
                intent {
                    reduce { state.copy(currentGptAssistants = it) }
                }
            }
        }
    }

    private fun launcherChatGptPrompt(message: String, gptType: GptType, role: String?) {
        intent {
            val prefixPrompt = makePrefixPrompt(role)
            val assistantPrompt = makeAssistantPrompt(state.currentGptAssistants)
            val userPrompt = makeUserPrompt(message)
            val prompt = prefixPrompt + assistantPrompt + userPrompt
            val request = ChatGptRequestVo(gptType, prompt)
            onPostChatGptPrompt(request, message, role)
        }
    }

    private fun makePrefixPrompt(role: String?): List<ChatGptMessageVo> {
        if (role.isNullOrEmpty()) return emptyList()
        val prefixPrompt = resourceProviderUtil.string(R.string.gpt_prompt_prefix, role)
        return listOf(ChatGptMessageVo(GptRoleType.SYSTEM, prefixPrompt))
    }

    private fun makeAssistantPrompt(list: List<GptAssistantVo>): List<ChatGptMessageVo> {
        return list.map { ChatGptMessageVo(GptRoleType.ASSISTANT, it.assistantMessage) }
    }

    private fun makeUserPrompt(message: String): List<ChatGptMessageVo> {
        return listOf(ChatGptMessageVo(GptRoleType.USER, message))
    }

    private fun onPostChatGptPrompt(request: ChatGptRequestVo, message: String, role: String?) {
        intent {
            viewModelScope.launch {
                postChatGptPromptUseCase(request)
                    .onStart {
                        insertMessage(
                            message,
                            GptMessageType.QUESTION,
                            TimeStampUtil.currentTimestamp
                        )
                        clearGptInput()
                        updateChannelLastQuestion(message, role, request.model)
                    }
                    .collect {
                        it.inspectUiState {
                            handlePostChatGptSuccess(it)
                        }
                    }
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
                insertGptAssistantUseCase(request).first()
            }
        }
    }

    private fun insertMessage(message: String, messageType: GptMessageType, createdAt: Long) {
        currentChannelIdx?.let { channelIdx ->
            viewModelScope.launch {
                val request = GptMessageVo(
                    channelIdx = channelIdx,
                    gptMessageType = messageType,
                    message = message,
                    createdAtUnixTimeStamp = createdAt
                )
                insertGptMessagesUseCase(request).first().inspectUiState {
                    if (messageType == GptMessageType.ANSWER) {
                        intent {
                            postSideEffect(GptSideEffect.RunTypingAnimation(it))
                        }
                    }
                }
            }
        }
    }

    private fun insertNewChannel() {
        intent {
            viewModelScope.launch {
                val newChannel = GptChannelVo(
                    gptType = GptType.NONE,
                    createdAtUnixTimeStamp = TimeStampUtil.currentTimestamp
                )
                insertGptChannelUseCase(newChannel).collect {
                    it.inspectUiState(onLoading = { showLoading() }) {
                        onChannelChanged(it)
                        reduce { state.copy(onLoading = false) }
                    }
                }
            }
        }
    }

    private fun updateChannelLastQuestion(question: String, roleOfAi: String?, gptType: GptType) {
        currentChannelIdx?.let { channelIdx ->
            val request = UpdateGptChannelInfoRequestVo(channelIdx, gptType, roleOfAi, question)
            viewModelScope.launch {
                updateGptChannelInfoUseCase(request).first()
            }
        }
    }

    private fun initInsertNewChannelCheck() {
        intent {
            viewModelScope.launch {
                selectLatestGptChannelsUseCase(Unit)
                    .filter { it is UiState.Success }
                    .first()
                    .inspectUiState {
                        val isNewCreate = it.gptType != GptType.NONE

                        if (isNewCreate) insertNewChannel()
                        else onChannelChanged(it.channelIdx)
                    }
            }
        }
    }

    private fun showLoading() {
        intent {
            reduce { state.copy(onLoading = true) }
        }
    }

    private fun clearGptInput() {
        intent {
            postSideEffect(GptSideEffect.HideKeyboard)
        }
        _gptInputStateOf.value = ""
    }
}
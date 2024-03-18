package com.youhajun.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.enums.GptRoleType
import com.youhajun.domain.models.enums.GptType
import com.youhajun.domain.models.inspectUiState
import com.youhajun.domain.models.vo.gpt.ChatGptMessageVo
import com.youhajun.domain.models.vo.gpt.ChatGptRequestVo
import com.youhajun.domain.models.vo.gpt.ChatGptResponseVo
import com.youhajun.domain.models.vo.gpt.GptAssistantVo
import com.youhajun.domain.usecase.gpt.DeleteGptAssistantUseCase
import com.youhajun.domain.usecase.gpt.DeleteGptRoleUseCase
import com.youhajun.domain.usecase.gpt.InsertGptAssistantUseCase
import com.youhajun.domain.usecase.gpt.InsertGptRoleUseCase
import com.youhajun.domain.usecase.gpt.PostChatGptPromptUseCase
import com.youhajun.domain.usecase.gpt.SelectAllGptAssistantsUseCase
import com.youhajun.domain.usecase.gpt.SelectAllGptRolesUseCase
import com.youhajun.domain.usecase.gpt.SelectLatestAssistantsUseCase
import com.youhajun.ui.R
import com.youhajun.ui.models.sideEffects.GptSideEffect
import com.youhajun.ui.models.states.GptState
import com.youhajun.ui.utils.ResourceProviderUtil
import com.youhajun.ui.utils.TimeStampUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
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
    fun onClickAssistant(idx: Long)
    fun onClickCreateAssistant()
    fun onClickDeleteAssistant(idx: Long)
}

@HiltViewModel
class GptViewModel @Inject constructor(
    private val resourceProviderUtil: ResourceProviderUtil,
    private val postChatGptPromptUseCase: PostChatGptPromptUseCase,
    private val insertGptRoleUseCase: InsertGptRoleUseCase,
    private val insertGptAssistantUseCase: InsertGptAssistantUseCase,
    private val selectAllGptRolesUseCase: SelectAllGptRolesUseCase,
    private val selectAllGptAssistantsUseCase: SelectAllGptAssistantsUseCase,
    private val selectLatestGptAssistantsUseCase: SelectLatestAssistantsUseCase,
    private val deleteGptRoleUseCase: DeleteGptRoleUseCase,
    private val deleteGptAssistantUseCase: DeleteGptAssistantUseCase,
) : ContainerHost<GptState, GptSideEffect>, ViewModel(), GptIntent {

    private val _gptInputStateOf: MutableState<String> = mutableStateOf("")
    val gptInputStateOf: State<String> = _gptInputStateOf
    private val _addRoleInputStateOf: MutableState<String> = mutableStateOf("")
    val addRoleInputStateOf: State<String> = _addRoleInputStateOf

    override val container: Container<GptState, GptSideEffect> = container(GptState()) {
        onFetchAllAssistants()
        onFetchAllRoles()
        initInsertNewAssistantCheck()
    }

    override fun onClickHeaderBackIcon() {
        intent { postSideEffect(GptSideEffect.Navigation.NavigateUp) }
    }

    override fun onClickHeaderMenuIcon() {
        intent {
            if (state.drawerState.isClosed) {
                postSideEffect(GptSideEffect.DrawerMenuOpen)
            } else {
                postSideEffect(GptSideEffect.DrawerMenuClose)
            }
        }
    }

    override fun onClickAssistant(idx: Long) {
        intent {
            val clickedAssistant = state.gptAssistantList.find { it.assistantIdx == idx }
            reduce { state.copy(currentGptAssistant = clickedAssistant) }
        }
    }

    override fun onClickDeleteAssistant(idx: Long) {
        intent {
            val isLastAssistant = state.gptAssistantList.size == 1
            val isCurrentAssistant = idx == state.currentGptAssistant?.assistantIdx

            viewModelScope.launch {
                deleteGptAssistantUseCase(idx).first().inspectUiState {
                    if (isCurrentAssistant && !isLastAssistant) insertNewAssistant()
                }
            }
        }
    }

    override fun onClickCreateAssistant() {
        insertNewAssistant()
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
            val currentAssistant = state.currentGptAssistant ?: return@intent

            val gptType =
                if (currentAssistant.gptType == GptType.NONE) state.selectedGptType else currentAssistant.gptType

            val prefixPrompt = makePrefixPrompt(state.currentRole)
            val prompt = makeMessagePrompt(message, prefixPrompt)
            //TODO addAssistant Prompt
            val request = ChatGptRequestVo(state.selectedGptType, prompt)
            onPostChatGptPrompt(request)
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
            reduce { state.copy(currentRole = role) }
        }
    }

    override fun onClickRoleExpandIcon() {
        intent {
            reduce { state.copy(isRoleExpanded = !state.isRoleExpanded) }
        }
    }

    private fun makePrefixPrompt(role: String): List<ChatGptMessageVo> {
        if (role.isEmpty()) return emptyList()
        val prefixPrompt = resourceProviderUtil.string(R.string.gpt_prompt_prefix, role)
        return listOf(ChatGptMessageVo(GptRoleType.SYSTEM, prefixPrompt))
    }

    private fun makeMessagePrompt(
        message: String,
        prefixPrompt: List<ChatGptMessageVo>
    ): List<ChatGptMessageVo> {
        return prefixPrompt.toMutableList().apply {
            add(ChatGptMessageVo(GptRoleType.USER, message))
        }
    }

    private fun onFetchAllRoles() {
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

    private fun onPostChatGptPrompt(request: ChatGptRequestVo) {
        intent {
            viewModelScope.launch {
                postChatGptPromptUseCase(request).collect {
                    it.inspectUiState {
                        postSideEffect(GptSideEffect.HideKeyboard)
                        _gptInputStateOf.value = ""
                        handlePostChatGptSuccess(it)
                    }
                }
            }
        }
    }

    private fun handlePostChatGptSuccess(chatGptResponseVo: ChatGptResponseVo) {

    }

    private fun onFetchAllAssistants() {
        intent {
            viewModelScope.launch {
                selectAllGptAssistantsUseCase(Unit)
                    .onEach {
                        it.inspectUiState {
                            if (it.isEmpty()) insertNewAssistant()
                        }
                    }
                    .collect {
                        it.inspectUiState {
                            reduce { state.copy(gptAssistantList = it) }
                        }
                    }
            }
        }
    }

    private fun insertNewAssistant() {
        intent {
            viewModelScope.launch {
                val newAssistant = GptAssistantVo(
                    gptType = GptType.NONE,
                    createdAtUnixTimeStamp = TimeStampUtil.currentTimestamp
                )
                insertGptAssistantUseCase(newAssistant).collect {
                    it.inspectUiState(
                        onLoading = { showLoading() }
                    ) {
                        reduce {
                            state.copy(
                                onLoading = false,
                                currentGptAssistant = it
                            )
                        }
                    }
                }
            }
        }
    }

    private fun initInsertNewAssistantCheck() {
        intent {
            viewModelScope.launch {
                selectLatestGptAssistantsUseCase(Unit)
                    .filter {
                        if (it is UiState.Success) {
                            it.data != null
                        } else false
                    }
                    .first()
                    .inspectUiState {

                        val isNewCreate = it!!.gptType != GptType.NONE

                        if (isNewCreate) insertNewAssistant()
                        else reduce { state.copy(currentGptAssistant = it) }
                    }
            }
        }
    }

    private fun showLoading() {
        intent {
            reduce { state.copy(onLoading = true) }
        }
    }
}
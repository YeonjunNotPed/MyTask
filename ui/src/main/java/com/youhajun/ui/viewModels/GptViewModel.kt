package com.youhajun.ui.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.domain.models.enums.GptType
import com.youhajun.domain.models.vo.gpt.ChatGptRequestVo
import com.youhajun.domain.usecase.gpt.PostChatGptPromptUseCase
import com.youhajun.ui.models.sideEffects.GptSideEffect
import com.youhajun.ui.models.states.GptState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


interface GptIntent {
    fun onChangedGptInput(input: String)
    fun onClickSendAnswer()

    fun onClickGptType(gptType: GptType)
}

@HiltViewModel
class GptViewModel @Inject constructor(
    private val postChatGptPromptUseCase: PostChatGptPromptUseCase
) : ContainerHost<GptState, GptSideEffect>, ViewModel(), GptIntent {

    private val _gptInputStateOf: MutableState<String> = mutableStateOf("")
    val gptInputStateOf: State<String> = _gptInputStateOf

    override val container: Container<GptState, GptSideEffect> = container(GptState())

    override fun onChangedGptInput(input: String) {
        _gptInputStateOf.value = input
    }

    override fun onClickSendAnswer() {
        intent {
            val request = ChatGptRequestVo(state.gptType, listOf())
            viewModelScope.launch {
                postChatGptPromptUseCase(request)
            }
        }
    }

    override fun onClickGptType(gptType: GptType) {
        intent {
            reduce { state.copy(gptType = gptType) }
        }
    }
}
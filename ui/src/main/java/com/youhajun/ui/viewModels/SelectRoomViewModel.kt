package com.youhajun.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.domain.models.inspectUiState
import com.youhajun.domain.usecase.room.GetRoomPreviewInfoUseCase
import com.youhajun.ui.models.sideEffects.SelectRoomSideEffect
import com.youhajun.ui.models.states.SelectRoomState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

interface SelectRoomIntent {
    fun onClickRoom(idx:Long)
    fun onClickHeaderBackIcon()
    fun onClickCreateRoom()

}

@HiltViewModel
class SelectRoomViewModel @Inject constructor(
    private val getRoomPreviewInfoUseCase: GetRoomPreviewInfoUseCase
) : ContainerHost<SelectRoomState, SelectRoomSideEffect>, ViewModel(), SelectRoomIntent {

    companion object {
        private const val POLLING_DISABLE = 0L
    }

    override val container: Container<SelectRoomState, SelectRoomSideEffect> = container(SelectRoomState()) {
        onRepeatFetchRoomPreviewInfo()
    }
    override fun onClickHeaderBackIcon() {
        intent { postSideEffect(SelectRoomSideEffect.Navigation.NavigateUp) }
    }

    override fun onClickRoom(idx: Long) {
        intent {
            postSideEffect(SelectRoomSideEffect.Navigation.NavigateToLiveRoom(idx))
        }
    }

    override fun onClickCreateRoom() {

    }

    private fun onRepeatFetchRoomPreviewInfo() {
        viewModelScope.launch {
            getRoomPreviewInfoUseCase(Unit).onEach {
                it.inspectUiState {
                    intent {
                        reduce { state.copy(roomList = it.roomList) }
                    }
                    if(it.pollingTime != POLLING_DISABLE) {
                        delay(it.pollingTime)
                        onRepeatFetchRoomPreviewInfo()
                    }
                }
            }.collect()
        }
    }
}
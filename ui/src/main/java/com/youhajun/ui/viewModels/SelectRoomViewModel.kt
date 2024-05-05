package com.youhajun.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.data.onSuccess
import com.youhajun.data.repositories.RoomRepository
import com.youhajun.model_ui.sideEffects.SelectRoomSideEffect
import com.youhajun.model_ui.states.SelectRoomState
import com.youhajun.model_ui.vo.room.RoomPreviewVo.Companion.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

private interface SelectRoomIntent {
    fun onClickRoom(idx: Long)
    fun onClickHeaderBackIcon()
    fun onClickCreateRoom()

}

@HiltViewModel
class SelectRoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ContainerHost<SelectRoomState, SelectRoomSideEffect>, ViewModel(), SelectRoomIntent {

    companion object {
        private const val POLLING_DISABLE = 0L
    }

    override val container: Container<SelectRoomState, SelectRoomSideEffect> =
        container(SelectRoomState()) {
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

    private fun onRepeatFetchRoomPreviewInfo():Job = intent {
        viewModelScope.launch {
            roomRepository.getRoomPreviewInfo().onSuccess {
                if (it.pollingTime != POLLING_DISABLE) {
                    delay(it.pollingTime)
                    onRepeatFetchRoomPreviewInfo()
                }

                reduce { state.copy(roomList = it.roomList.map { it.toModel() }.toImmutableList()) }
            }
        }
    }
}
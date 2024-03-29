package com.youhajun.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.domain.models.UiStateErrorVo
import com.youhajun.domain.models.enums.SignalingType
import com.youhajun.domain.models.inspectUiState
import com.youhajun.domain.models.sealeds.WebSocketState
import com.youhajun.domain.usecase.room.ConnectLiveRoomUseCase
import com.youhajun.domain.usecase.room.DisposeLiveRoomUseCase
import com.youhajun.domain.usecase.room.GetRoomSignalingCommandUseCase
import com.youhajun.domain.usecase.room.GetRoomSocketStateUseCase
import com.youhajun.domain.usecase.room.GetRoomWebRTCSessionUseCase
import com.youhajun.domain.usecase.room.SendLiveRoomSignalingMsgUseCase
import com.youhajun.ui.R
import com.youhajun.ui.models.sideEffects.LiveRoomSideEffect
import com.youhajun.ui.models.states.LiveRoomState
import com.youhajun.ui.utils.ResourceProviderUtil
import com.youhajun.ui.utils.webRtc.WebRTCContract
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.log.taggedLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import org.webrtc.EglBase

interface LiveRoomIntent {
    fun onClickHeaderBackIcon()
    fun onLiveRoomSignalingConnect()
    fun onLiveRoomPermissionDenied()
}

@HiltViewModel(assistedFactory = LiveRoomViewModel.LiveRoomViewModelFactory::class)
class LiveRoomViewModel @AssistedInject constructor(
    @Assisted private val roomIdx: Long,
    private val resourceProviderUtil: ResourceProviderUtil,
    private val connectLiveRoomUseCase: ConnectLiveRoomUseCase,
    private val disposeLiveRoomUseCase: DisposeLiveRoomUseCase,
    private val sendLiveRoomSignalingMsgUseCase: SendLiveRoomSignalingMsgUseCase,
    private val getRoomSocketStateUseCase: GetRoomSocketStateUseCase,
    private val getRoomSignalingCommandUseCase: GetRoomSignalingCommandUseCase,
    private val getRoomWebRTCSessionUseCase: GetRoomWebRTCSessionUseCase,
    webRtcSessionManagerFactory: WebRTCContract.Factory,
    eglBaseContext: EglBase.Context
) : ContainerHost<LiveRoomState, LiveRoomSideEffect>, ViewModel(), LiveRoomIntent,
    WebRTCContract.Signaling {

    private val logger by taggedLogger("LiveRoomViewModel")

    @AssistedFactory
    interface LiveRoomViewModelFactory {
        fun create(roomIdx: Long): LiveRoomViewModel
    }

    private val _signalingCommandFlow = MutableSharedFlow<Pair<SignalingType, String>>()
    override val signalingCommandFlow: Flow<Pair<SignalingType, String>> = _signalingCommandFlow
    private val sessionManager: WebRTCContract.SessionManager = webRtcSessionManagerFactory.createSessionManager(this)

    override val container: Container<LiveRoomState, LiveRoomSideEffect> = container(
        LiveRoomState(eglContext = eglBaseContext, mySessionId = sessionManager.sessionId)
    ) {
        onCollectSignaling()
        onCollectMediaTrack()
        onLiveRoomSignalingConnect()
    }


    override fun onClickHeaderBackIcon() {
        intent { postSideEffect(LiveRoomSideEffect.Navigation.NavigateUp) }
    }

    override fun sendCommand(signalingCommand: SignalingType, message: String) {
        viewModelScope.launch {
            sendLiveRoomSignalingMsgUseCase(signalingCommand to message)
        }
    }

    override fun onLiveRoomSignalingConnect() {
        intent {
            postSideEffect(LiveRoomSideEffect.LivePermissionLauncher {
                viewModelScope.launch {
                    connectLiveRoomUseCase(Unit)
                }
            })
        }
    }

    override fun onLiveRoomPermissionDenied() {
        intent {
            val msg = resourceProviderUtil.string(R.string.room_permission_denied)
            postSideEffect(LiveRoomSideEffect.Toast(msg))
            postSideEffect(LiveRoomSideEffect.Navigation.NavigateUp)
        }
    }

    override fun dispose() {
        viewModelScope.launch {
            disposeLiveRoomUseCase(Unit)
        }
    }

    override fun onCleared() {
        super.onCleared()

        sessionManager.disconnect()
    }

    private fun onCollectMediaTrack() {
        intent {
            viewModelScope.launch {
                sessionManager.videoTrackFlow.collect {
                    reduce { state.copy(videoTrackMap = it) }
                }
            }
        }
    }

    private fun onCollectSignaling() {
        viewModelScope.launch {
            launch {
                onGetRoomSocketState()
            }
            launch {
                onGetRoomSignalingCommand()
            }
            launch {
                onGetRoomWebRTCSession()
            }
        }
    }

    private suspend fun onGetRoomSocketState() {
        getRoomSocketStateUseCase(Unit).collect {
            it.inspectUiState(
                onError = ::handleWebSocketStateError,
                onSuccess = ::handleWebSocketStateSuccess
            )
        }
    }

    private suspend fun onGetRoomSignalingCommand() {
        getRoomSignalingCommandUseCase(Unit).collect {
            logger.d { "webRTCSignalingCmd : ${it.first} : ${it.second}" }
            _signalingCommandFlow.emit(it)
        }
    }

    private suspend fun onGetRoomWebRTCSession() {
        getRoomWebRTCSessionUseCase(Unit).collect {
            logger.d { "webRTCSessionType : ${it.type}" }
            intent {
                reduce {
                    state.copy(
                        webRTCSessionType = it
                    )
                }
            }
        }
    }

    private fun handleWebSocketStateSuccess(wss: WebSocketState) {
        logger.d { "socketStateSuccess : $wss" }
        when (wss as WebSocketState.Success) {
            WebSocketState.Success.Close,
            is WebSocketState.Success.Message -> Unit

            WebSocketState.Success.Open -> sessionManager.onSessionScreenReady()
        }
    }

    private fun handleWebSocketStateError(errorVo: UiStateErrorVo<WebSocketState>) {
        logger.d { "socketStateError : ${errorVo.message}" }
        when (errorVo.data as? WebSocketState.Error) {
            WebSocketState.Error.Close,
            null -> Unit

            WebSocketState.Error.Failure -> {

            }
        }
    }
}
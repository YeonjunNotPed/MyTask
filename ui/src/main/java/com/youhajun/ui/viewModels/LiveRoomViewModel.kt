package com.youhajun.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.domain.models.enums.SignalingType
import com.youhajun.domain.models.enums.WebRTCSessionType
import com.youhajun.domain.models.sealeds.CallControlAction
import com.youhajun.domain.models.vo.CallMediaStateVo
import com.youhajun.domain.usecase.room.ConnectLiveRoomUseCase
import com.youhajun.domain.usecase.room.DisposeLiveRoomUseCase
import com.youhajun.domain.usecase.room.GetRoomSignalingCommandUseCase
import com.youhajun.domain.usecase.room.GetRoomWebRTCSessionUseCase
import com.youhajun.domain.usecase.room.SendLiveRoomSignalingMsgUseCase
import com.youhajun.ui.R
import com.youhajun.ui.models.sideEffects.LiveRoomSideEffect
import com.youhajun.ui.models.states.LiveRoomState
import com.youhajun.ui.utils.ResourceProviderUtil
import com.youhajun.ui.utils.webRtc.WebRTCContract
import com.youhajun.ui.utils.webRtc.models.SessionInfoVo
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
    fun onClickCallControlAction(action: CallControlAction)
    fun onLiveRoomSignalingConnect()
    fun onLiveRoomPermissionDenied()
    fun onTabCallingScreen()
}

@HiltViewModel(assistedFactory = LiveRoomViewModel.LiveRoomViewModelFactory::class)
class LiveRoomViewModel @AssistedInject constructor(
    @Assisted private val roomIdx: Long,
    private val resourceProviderUtil: ResourceProviderUtil,
    private val connectLiveRoomUseCase: ConnectLiveRoomUseCase,
    private val disposeLiveRoomUseCase: DisposeLiveRoomUseCase,
    private val sendLiveRoomSignalingMsgUseCase: SendLiveRoomSignalingMsgUseCase,
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
        LiveRoomState(eglContext = eglBaseContext, mySessionId = sessionManager.mySessionId)
    ) {
        onCollectSignaling()
        onCollectMediaTrack()
        sessionManager.onScreenReady()
        onLiveRoomSignalingConnect()
    }


    override fun onClickHeaderBackIcon() {
        intent { postSideEffect(LiveRoomSideEffect.Navigation.NavigateUp) }
    }

    override fun onClickCallControlAction(action: CallControlAction) {
        intent {
            when(action) {
                CallControlAction.CallingEnd -> {
                }
                CallControlAction.FlipCamera -> sessionManager.flipCamera {
                    it.onSuccess {
                        val mediaState = state.mySessionInfoVo?.callMediaStateVo?.copy(isFrontCamera = it)
                        updateMediaState(mediaState)
                    }
                }
                is CallControlAction.ToggleCamera -> sessionManager.enableCamera(!action.isEnabled)
                is CallControlAction.ToggleMicroPhone -> sessionManager.enableMicrophone(!action.isEnabled)
            }
        }
    }

    override fun sendCommand(signalingCommand: SignalingType, message: String) {
        viewModelScope.launch {
            sendLiveRoomSignalingMsgUseCase(signalingCommand to message)
        }
    }

    override fun onLiveRoomSignalingConnect() {
        intent {
            postSideEffect(LiveRoomSideEffect.LivePermissionLauncher {
                connectLiveRoomUseCase(Unit)
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

    override fun onTabCallingScreen() {
        intent {
            reduce { state.copy(isVisibleBottomAction = !state.isVisibleBottomAction) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        callingEnd()
    }
    private fun onCollectMediaTrack() {
        intent {
            viewModelScope.launch {
                sessionManager.sessionFlow.collect {
                    val mySessionInfo = findSessionInfo(it, state.mySessionId)
                    val partnerSessionInfo = findPartnerSessionInfo(it, state.mySessionId)

                    reduce {
                        state.copy(
                            mySessionInfoVo = mySessionInfo,
                            partnerSessionInfoVo = partnerSessionInfo
                        )
                    }
                }
            }
        }
    }

    private fun onCollectSignaling() {
        viewModelScope.launch {
            launch {
                onGetRoomSignalingCommand()
            }
            launch {
                onGetRoomWebRTCSession()
            }
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
            if (it == WebRTCSessionType.Ready) sessionManager.onSignalingImpossible()

            intent {
                reduce {
                    state.copy(webRTCSessionType = it)
                }
            }
        }
    }
    private fun findPartnerSessionInfo(session: Map<String, SessionInfoVo>, mySessionId: String) =
        session.filterKeys { it != mySessionId }.values.firstOrNull()

    private fun findSessionInfo(session: Map<String, SessionInfoVo>, sessionId: String) = session[sessionId]

    private fun callingEnd() {
        sessionManager.disconnect()
        disposeLiveRoomUseCase(Unit)
    }


    private fun updateMediaState(mediaStateVo: CallMediaStateVo?) {
        intent {
            reduce {
                val updatedSessionInfoVo = state.mySessionInfoVo?.copy(callMediaStateVo = mediaStateVo ?: CallMediaStateVo())
                state.copy(mySessionInfoVo = updatedSessionInfoVo)
            }
        }
    }
}
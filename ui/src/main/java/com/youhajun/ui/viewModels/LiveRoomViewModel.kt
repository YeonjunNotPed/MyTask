package com.youhajun.ui.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.domain.models.enums.SignalingType
import com.youhajun.domain.models.enums.SocketMessageType
import com.youhajun.domain.models.enums.WebRTCSessionType
import com.youhajun.domain.models.sealeds.CallControlAction
import com.youhajun.domain.usecase.room.ConnectLiveRoomUseCase
import com.youhajun.domain.usecase.room.DisposeLiveRoomUseCase
import com.youhajun.domain.usecase.room.GetRoomSignalingCommandUseCase
import com.youhajun.domain.usecase.room.GetRoomWebRTCSessionUseCase
import com.youhajun.domain.usecase.room.SendLiveRoomSignalingMsgUseCase
import com.youhajun.domain.usecase.room.SendSocketMsgUseCase
import com.youhajun.ui.R
import com.youhajun.ui.models.destinations.MyTaskDestination
import com.youhajun.ui.models.sideEffects.LiveRoomSideEffect
import com.youhajun.ui.models.states.LiveRoomState
import com.youhajun.ui.utils.ResourceProviderUtil
import com.youhajun.ui.utils.webRtc.WebRTCContract
import com.youhajun.ui.utils.webRtc.models.SessionInfoVo
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
import javax.inject.Inject

interface LiveRoomIntent {
    fun onClickHeaderBackIcon()
    fun onClickCallControlAction(action: CallControlAction)
    fun onLiveRoomSignalingConnect()
    fun onLiveRoomPermissionDenied()
    fun onTabCallingScreen()
}

@HiltViewModel
class LiveRoomViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val resourceProviderUtil: ResourceProviderUtil,
    private val connectLiveRoomUseCase: ConnectLiveRoomUseCase,
    private val disposeLiveRoomUseCase: DisposeLiveRoomUseCase,
    private val sendLiveRoomSignalingMsgUseCase: SendLiveRoomSignalingMsgUseCase,
    private val sendSocketMsgUseCase: SendSocketMsgUseCase,
    private val getRoomSignalingCommandUseCase: GetRoomSignalingCommandUseCase,
    private val getRoomWebRTCSessionUseCase: GetRoomWebRTCSessionUseCase,
    webRtcSessionManagerFactory: WebRTCContract.Factory,
    eglBaseContext: EglBase.Context
) : ContainerHost<LiveRoomState, LiveRoomSideEffect>, ViewModel(), LiveRoomIntent,
    WebRTCContract.Signaling {

    private val logger by taggedLogger("LiveRoomViewModel")

    private val roomIdx: Long = checkNotNull(savedStateHandle[MyTaskDestination.LiveRoom.IDX_ARG_KEY])
    private val _signalingCommandFlow = MutableSharedFlow<Pair<SignalingType, String>>()
    override val signalingCommandFlow: Flow<Pair<SignalingType, String>> = _signalingCommandFlow
    private val sessionManager: WebRTCContract.SessionManager = webRtcSessionManagerFactory.createSessionManager(this)

    override val container: Container<LiveRoomState, LiveRoomSideEffect> = container(
        LiveRoomState(eglContext = eglBaseContext, mySessionId = sessionManager.mySessionId)
    ) {
        onCollectSignaling()
        onCollectMedia()
        sessionManager.onScreenReady()
        onLiveRoomSignalingConnect()
    }


    override fun onClickHeaderBackIcon() {
        intent { postSideEffect(LiveRoomSideEffect.Navigation.NavigateUp) }
    }

    override fun onClickCallControlAction(action: CallControlAction) {
        intent {
            when (action) {
                CallControlAction.CallingEnd -> postSideEffect(LiveRoomSideEffect.Navigation.NavigateUp)
                CallControlAction.FlipCamera -> sessionManager.flipCamera()
                is CallControlAction.ToggleCamera -> sessionManager.setEnableCamera(!action.isEnabled)
                is CallControlAction.ToggleSpeakerphone -> sessionManager.setEnableSpeakerphone(!action.isEnabled)
                is CallControlAction.ToggleMicMute -> sessionManager.setMicMute(!action.isMute)
            }
        }
    }

    override fun sendCommand(signalingCommand: SignalingType, message: String) {
        sendLiveRoomSignalingMsgUseCase(signalingCommand to message)
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
    private fun onCollectMedia() {
        intent {
            viewModelScope.launch {
                launch {
                    onCollectSession()
                }
                launch {
                    onCollectAudioLevels()
                }
            }
        }
    }

    private suspend fun onCollectSession() {
        sessionManager.sessionFlow.collect {
            intent {
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

    private suspend fun onCollectAudioLevels() {
        sessionManager.audioLevelListFlow.collect {
            val msg = SocketMessageType.AUDIO_LEVELS to it.toString()
            sendSocketMsgUseCase(msg)
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
    private fun findPartnerSessionInfo(session: Map<String, SessionInfoVo>, mySessionId: String) = session.filterKeys { it != mySessionId }.values.firstOrNull()

    private fun findSessionInfo(session: Map<String, SessionInfoVo>, sessionId: String) = session[sessionId]

    private fun callingEnd() {
        sessionManager.disconnect()
        disposeLiveRoomUseCase(Unit)
    }
}
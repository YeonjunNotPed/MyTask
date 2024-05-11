package com.youhajun.ui.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.data.repositories.RoomRepository
import com.youhajun.model_ui.holder.CallControlAction
import com.youhajun.model_ui.sideEffects.LiveRoomSideEffect
import com.youhajun.model_ui.states.LiveRoomState
import com.youhajun.model_ui.types.webrtc.SignalingCommandType
import com.youhajun.model_ui.types.webrtc.SignalingCommandType.Companion.toDto
import com.youhajun.model_ui.types.webrtc.SignalingCommandType.Companion.toModel
import com.youhajun.model_ui.types.webrtc.SocketMessageType
import com.youhajun.model_ui.types.webrtc.SocketMessageType.Companion.toDto
import com.youhajun.model_ui.types.webrtc.TrackType
import com.youhajun.model_ui.types.webrtc.VideoScreenType
import com.youhajun.model_ui.types.webrtc.WebRTCSessionType
import com.youhajun.model_ui.types.webrtc.WebRTCSessionType.Companion.toModel
import com.youhajun.model_ui.types.webrtc.WebSocketState
import com.youhajun.model_ui.types.webrtc.WebSocketState.Companion.toModel
import com.youhajun.model_ui.vo.webrtc.findSessionTrackInfo
import com.youhajun.model_ui.vo.webrtc.findSessionTrackInfoById
import com.youhajun.model_ui.vo.webrtc.partnerSessionTrackInfo
import com.youhajun.model_ui.vo.webrtc.toSessionVideoTrackVoList
import com.youhajun.model_ui.wrapper.EglBaseContextWrapper
import com.youhajun.ui.R
import com.youhajun.ui.destinations.MyTaskDestination
import com.youhajun.ui.utils.ResourceProviderUtil
import com.youhajun.ui.utils.webRtc.WebRTCContract
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.log.taggedLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import org.webrtc.EglBase
import javax.inject.Inject

private interface LiveRoomIntent {
    fun onClickHeaderBackIcon()
    fun onClickCallControlAction(action: CallControlAction)
    fun onLiveRoomSignalingConnect()
    fun onLiveRoomPermissionDenied()
    fun onTabCallingScreen()
    fun onDoubleTabCallingScreen(screenType: VideoScreenType, trackId: String?)
}

@HiltViewModel
class LiveRoomViewModel @Inject constructor(
    private val resourceProviderUtil: ResourceProviderUtil,
    private val roomRepository: RoomRepository,
    savedStateHandle: SavedStateHandle,
    webRtcSessionManagerFactory: WebRTCContract.Factory,
    eglBaseContext: EglBase.Context
) : ContainerHost<LiveRoomState, LiveRoomSideEffect>, ViewModel(), LiveRoomIntent,
    WebRTCContract.Signaling {

    private val logger by taggedLogger("LiveRoomViewModel")

    private val roomIdx: Long =
        checkNotNull(savedStateHandle[MyTaskDestination.LiveRoom.IDX_ARG_KEY])

    private val _signalingCommandFlow = MutableSharedFlow<Pair<SignalingCommandType, String>>()
    override val signalingCommandFlow: Flow<Pair<SignalingCommandType, String>> =
        _signalingCommandFlow

    private val sessionManager: WebRTCContract.SessionManager =
        webRtcSessionManagerFactory.createSessionManager(this)

    override val container: Container<LiveRoomState, LiveRoomSideEffect> = container(
        LiveRoomState(
            eglContextWrapper = EglBaseContextWrapper(eglBaseContext),
            mySessionId = sessionManager.mySessionId
        )
    ) {
        onCollectSignaling()
        onCollectMedia()
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

    override fun sendCommand(signalingCommand: SignalingCommandType, message: String) {
        roomRepository.sendSignalingMessage(signalingCommand.toDto(), message)
    }

    override fun onLiveRoomSignalingConnect() {
        intent {
            postSideEffect(LiveRoomSideEffect.LivePermissionLauncher {
                roomRepository.connectLiveRoom()
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

    override fun onDoubleTabCallingScreen(screenType: VideoScreenType, trackId: String?) {
        if (screenType == VideoScreenType.SPLIT) {
            splitScreenDoubleTab(trackId)
        } else {
            floatingScreenDoubleTab()
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

    private suspend fun onCollectSession() = intent {
        sessionManager.sessionFlow.collect { sessionMap ->
            val sessions = sessionMap.values.toSessionVideoTrackVoList()
            val floatingSessionInfo = state.floatingSessionTrackInfo
                ?: sessions.findSessionTrackInfo(state.mySessionId, TrackType.VIDEO)
            val fillMaxSessionInfo = state.fillMaxSessionTrackInfo
                ?: sessions.partnerSessionTrackInfo(state.mySessionId, TrackType.VIDEO)

            reduce {
                state.copy(
                    sessionTrackInfoList = sessions,
                    floatingSessionTrackInfo = floatingSessionInfo,
                    fillMaxSessionTrackInfo = fillMaxSessionInfo
                )
            }
        }
    }

    private suspend fun onCollectAudioLevels() {
        sessionManager.audioLevelListFlow.collect {
            roomRepository.sendSocketMessage(SocketMessageType.AUDIO_LEVELS.toDto(), it.toString())
        }
    }

    private fun onCollectSignaling() {
        viewModelScope.launch {
            launch {
                onCollectSocketState()
            }
            launch {
                onCollectRoomSignalingCommand()
            }
            launch {
                onCollectRoomWebRTCSession()
            }
        }
    }

    private suspend fun onCollectSocketState() {
        roomRepository.socketFlow.map { it.toModel() }
            .filterNot { it is WebSocketState.Message }
            .collect {
                when(it) {
                    WebSocketState.Open -> sessionManager.onScreenReady()
                    else -> Unit
                }
            }
    }

    private suspend fun onCollectRoomSignalingCommand() {
        roomRepository.signalingCommandTypeFlow.collect {
            val (command, message) = it
            logger.d { "signalingCommandType : $command : $message" }
            _signalingCommandFlow.emit(command.toModel() to message)
        }
    }

    private suspend fun onCollectRoomWebRTCSession() = intent {
        roomRepository.sessionStateFlow.collect {
            val sessionType = it.toModel()
            logger.d { "webRTCSessionState : $sessionType" }
            if (sessionType == WebRTCSessionType.Ready) sessionManager.onSignalingImpossible()
            reduce {
                state.copy(webRTCSessionType = sessionType)
            }
        }
    }

    private fun splitScreenDoubleTab(trackId: String?) = intent {
        reduce {
            state.copy(
                videoScreenType = VideoScreenType.FLOATING,
                fillMaxSessionTrackInfo = state.sessionTrackInfoList.findSessionTrackInfoById(
                    trackId
                ),
                floatingSessionTrackInfo = state.sessionTrackInfoList.findSessionTrackInfo(
                    state.mySessionId,
                    TrackType.VIDEO
                )
            )
        }
    }

    private fun floatingScreenDoubleTab() = intent {
        reduce {
            state.copy(
                videoScreenType = VideoScreenType.SPLIT,
            )
        }
    }

    private fun callingEnd() {
        sessionManager.disconnect()
        roomRepository.dispose()
    }
}
package com.youhajun.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.domain.models.enums.SignalingType
import com.youhajun.domain.models.enums.WebRTCSessionType
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
import com.youhajun.ui.utils.webRtc.models.TrackType
import com.youhajun.ui.utils.webRtc.models.TrackVo
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

    override fun onCleared() {
        super.onCleared()
        sessionManager.disconnect()
        disposeLiveRoomUseCase(Unit)
    }

    private fun onCollectMediaTrack() {
        intent {
            viewModelScope.launch {
                sessionManager.trackFlow.collect {
                    val myVideoTrack = findTrack(it, state.mySessionId, TrackType.VIDEO)?.videoTrack
                    val partnerVideoTrack = findPartnerTrack(it, state.mySessionId, TrackType.VIDEO)?.videoTrack

                    reduce {
                        state.copy(
                            myVideoTrack = myVideoTrack,
                            partnerVideoTrack = partnerVideoTrack
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

    private fun findTrack(
        tracks: Map<String, List<TrackVo>>,
        sessionId: String,
        trackType: TrackType
    ): TrackVo? {
        return tracks[sessionId]?.find { it.trackType == trackType }
    }

    private fun findPartnerTrack(
        tracks: Map<String, List<TrackVo>>,
        sessionId: String,
        trackType: TrackType
    ): TrackVo? {
        return tracks.filterKeys { it != sessionId }.values.firstOrNull()?.find { it.trackType == trackType }
    }
}
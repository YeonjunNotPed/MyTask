package com.youhajun.data.repositories

import com.youhajun.data.Resource
import com.youhajun.data.di.DefaultDispatcher
import com.youhajun.data.models.dto.room.RoomPreviewInfo
import com.youhajun.data.models.enums.SignalingCommand
import com.youhajun.data.models.enums.WebRTCSessionState
import com.youhajun.data.models.sealeds.WebSocketStateDTO
import com.youhajun.data.repositories.base.BaseRepository
import com.youhajun.data.repositories.remoteDataSource.RoomRemoteDataSource
import com.youhajun.data.repositories.remoteDataSource.WebSocketDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val roomRemoteDataSource: RoomRemoteDataSource,
    private val webSocketDataSource: WebSocketDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : BaseRepository() {

    private val scope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    val socketFlow: Flow<Resource<WebSocketStateDTO>> = webSocketDataSource.socketFlow
        .onEach { if(it is WebSocketStateDTO.Message) onMessage(it.text) }
        .filterNot { it is WebSocketStateDTO.Message }
        .map { socketConverter(it) }

    private val _signalingCommandFlow = MutableSharedFlow<Pair<SignalingCommand, String>>()
    val signalingCommandFlow: Flow<Pair<SignalingCommand, String>> = _signalingCommandFlow

    private val _sessionStateFlow = MutableStateFlow(WebRTCSessionState.Offline)
    val sessionStateFlow: Flow<WebRTCSessionState> = _sessionStateFlow
    suspend fun getRoomPreviewInfo(): Flow<Resource<RoomPreviewInfo>> =
        roomRemoteDataSource.getRoomPreviewInfo().map { myTaskApiConverter(it) }

    fun connectLiveRoom() {
        webSocketDataSource.connect()
    }

    fun dispose() {
        _sessionStateFlow.value = WebRTCSessionState.Offline
        scope.cancel()
        webSocketDataSource.disconnect()
    }

    fun sendSignalingMessage(signalingCommand: SignalingCommand, message: String) {
        webSocketDataSource.sendMessage("$signalingCommand $message")
    }

    fun sendMessage(message: String) {
        webSocketDataSource.sendMessage(message)
    }

    private fun onMessage(text: String) {
        when {
            text.startsWith(SignalingCommand.STATE.toString(), true) ->
                handleStateMessage(text)

            text.startsWith(SignalingCommand.OFFER.toString(), true) ->
                handleSignalingCommand(SignalingCommand.OFFER, text)

            text.startsWith(SignalingCommand.ANSWER.toString(), true) ->
                handleSignalingCommand(SignalingCommand.ANSWER, text)

            text.startsWith(SignalingCommand.ICE.toString(), true) ->
                handleSignalingCommand(SignalingCommand.ICE, text)
        }
    }

    private fun handleStateMessage(message: String) {
        val state = getSeparatedMessage(message)
        _sessionStateFlow.value = WebRTCSessionState.valueOf(state)
    }

    private fun handleSignalingCommand(command: SignalingCommand, text: String) {
        val value = getSeparatedMessage(text)
        scope.launch {
            _signalingCommandFlow.emit(command to value)
        }
    }

    private fun getSeparatedMessage(text: String) = text.substringAfter(' ')


}

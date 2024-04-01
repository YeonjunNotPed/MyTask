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
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScoped
class RoomRepository @Inject constructor(
    private val roomRemoteDataSource: RoomRemoteDataSource,
    private val webSocketDataSource: WebSocketDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : BaseRepository() {

    private lateinit var scope: CoroutineScope

    private val _signalingCommandFlow = MutableSharedFlow<Pair<SignalingCommand, String>>()
    val signalingCommandFlow: SharedFlow<Pair<SignalingCommand, String>> = _signalingCommandFlow.asSharedFlow()

    private val _sessionStateFlow = MutableStateFlow(WebRTCSessionState.Offline)
    val sessionStateFlow: StateFlow<WebRTCSessionState> = _sessionStateFlow.asStateFlow()
    suspend fun getRoomPreviewInfo(): Flow<Resource<RoomPreviewInfo>> =
        roomRemoteDataSource.getRoomPreviewInfo().map { myTaskApiConverter(it) }

    fun connectLiveRoom() {
        scope = CoroutineScope(SupervisorJob() + defaultDispatcher)
        collectSocket()
        webSocketDataSource.connect()
    }

    fun dispose() {
        _sessionStateFlow.update { WebRTCSessionState.Offline }
        scope.cancel()
        webSocketDataSource.disconnect()
    }

    fun sendSignalingMessage(signalingCommand: SignalingCommand, message: String) {
        val totalMessage = "$signalingCommand $message"
        sendMessage(totalMessage)
    }

    fun sendMessage(message: String) {
        webSocketDataSource.sendMessage(message)
    }

    private fun collectSocket() {
        scope.launch {
            webSocketDataSource.socketFlow
                .filter { it is WebSocketStateDTO.Message }
                .collect {
                    if (it is WebSocketStateDTO.Message) onMessage(it.text)
                }
        }
    }

    private suspend fun onMessage(text: String) {
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
        val sessionState = WebRTCSessionState.typeOf(state)
        _sessionStateFlow.update { sessionState }
    }

    private suspend fun handleSignalingCommand(command: SignalingCommand, text: String) {
        val value = getSeparatedMessage(text)
        _signalingCommandFlow.emit(command to value)
    }

    private fun getSeparatedMessage(text: String) = text.substringAfter(' ')
}

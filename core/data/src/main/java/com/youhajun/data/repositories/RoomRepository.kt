package com.youhajun.data.repositories

import com.youhajun.common.DefaultDispatcher
import com.youhajun.data.repositories.remoteDataSource.RoomRemoteDataSource
import com.youhajun.data.repositories.remoteDataSource.WebSocketDataSource
import com.youhajun.model_data.room.RoomPreviewInfo
import com.youhajun.model_data.types.SignalingCommandTypeDto
import com.youhajun.model_data.types.SocketMessageTypeDto
import com.youhajun.model_data.types.WebRTCSessionTypeDto
import com.youhajun.model_data.types.WebSocketStateDto
import com.youhajun.model_data.ApiResult
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScoped
class RoomRepository @Inject constructor(
    private val roomRemoteDataSource: RoomRemoteDataSource,
    private val webSocketDataSource: WebSocketDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) {

    private lateinit var scope: CoroutineScope

    private val _signalingCommandTypeFlow = MutableSharedFlow<Pair<SignalingCommandTypeDto, String>>()
    val signalingCommandTypeFlow: SharedFlow<Pair<SignalingCommandTypeDto, String>> = _signalingCommandTypeFlow.asSharedFlow()

    private val _sessionStateFlow = MutableStateFlow(WebRTCSessionTypeDto.Offline)
    val sessionStateFlow: StateFlow<WebRTCSessionTypeDto> = _sessionStateFlow.asStateFlow()

    val socketFlow: SharedFlow<WebSocketStateDto> = webSocketDataSource.socketFlow

    suspend fun getRoomPreviewInfo(): ApiResult<RoomPreviewInfo> = roomRemoteDataSource.getRoomPreviewInfo()

    fun connectLiveRoom() {
        scope = CoroutineScope(SupervisorJob() + defaultDispatcher)
        collectSocket()
        webSocketDataSource.connect()
    }

    fun dispose() {
        _sessionStateFlow.update { WebRTCSessionTypeDto.Offline }
        scope.cancel()
        webSocketDataSource.disconnect()
    }

    fun sendSignalingMessage(signalingCommandTypeDto: SignalingCommandTypeDto, message: String) {
        val totalMessage = "$signalingCommandTypeDto $message"
        sendMessage(totalMessage)
    }

    fun sendSocketMessage(socketMessageTypeDto: SocketMessageTypeDto, message: String) {
        val totalMessage = "$socketMessageTypeDto $message"
        sendMessage(totalMessage)
    }

    private fun sendMessage(message: String) {
        webSocketDataSource.sendMessage(message)
    }

    private fun collectSocket() {
        scope.launch {
            socketFlow
                .filter { it is WebSocketStateDto.Message }
                .collect {
                    if (it is WebSocketStateDto.Message) onMessage(it.text)
                }
        }
    }

    private suspend fun onMessage(text: String) {
        when {
            text.startsWith(SignalingCommandTypeDto.STATE.toString(), true) ->
                handleStateMessage(text)

            text.startsWith(SignalingCommandTypeDto.OFFER.toString(), true) ->
                handleSignalingCommand(SignalingCommandTypeDto.OFFER, text)

            text.startsWith(SignalingCommandTypeDto.ANSWER.toString(), true) ->
                handleSignalingCommand(SignalingCommandTypeDto.ANSWER, text)

            text.startsWith(SignalingCommandTypeDto.ICE.toString(), true) ->
                handleSignalingCommand(SignalingCommandTypeDto.ICE, text)
        }
    }

    private fun handleStateMessage(message: String) {
        val state = getSeparatedMessage(message)
        val sessionState = WebRTCSessionTypeDto.typeOf(state)
        _sessionStateFlow.update { sessionState }
    }

    private suspend fun handleSignalingCommand(command: SignalingCommandTypeDto, text: String) {
        val value = getSeparatedMessage(text)
        _signalingCommandTypeFlow.emit(command to value)
    }

    private fun getSeparatedMessage(text: String) = text.substringAfter(' ')
}

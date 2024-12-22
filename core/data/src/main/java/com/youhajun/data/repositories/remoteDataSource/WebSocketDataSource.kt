package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.common.DefaultDispatcher
import com.youhajun.data.error.MyTaskCode
import com.youhajun.model_data.types.WebSocketStateDto
import com.youhajun.remote.di.MyTaskWebSocketOkHttpClient
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

@ViewModelScoped
class WebSocketDataSource @Inject constructor(
    @MyTaskWebSocketOkHttpClient private val client: OkHttpClient,
    @MyTaskWebSocketOkHttpClient private val request: Request,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) {

    private lateinit var webSocket:WebSocket
    private val scope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    private val _socketFlow = MutableSharedFlow<WebSocketStateDto>()
    val socketFlow: SharedFlow<WebSocketStateDto> = _socketFlow.asSharedFlow()
    private val sendMessageFlow = MutableSharedFlow<String>(
        replay = 10,
        extraBufferCapacity = 10,
        onBufferOverflow = BufferOverflow.SUSPEND)

    private val webSocketListener = object : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            scope.launch {
                _socketFlow.emit(WebSocketStateDto.Message(text))
            }
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            collectMessage()
            scope.launch {
                _socketFlow.emit(WebSocketStateDto.Open(response))
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            scope.launch {
                _socketFlow.emit(WebSocketStateDto.Failure(t, response))
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            scope.launch {
                _socketFlow.emit(WebSocketStateDto.Close(code, reason))
            }
        }
    }

    fun connect() {
        webSocket = client.newWebSocket(request, webSocketListener)
    }

    fun disconnect() {
        val callSuccess = webSocket.close(MyTaskCode.WEB_SOCKET_SUCCESS_CODE, null)
        if(!callSuccess) webSocket.cancel()
        scope.cancel()
    }

    fun sendMessage(message: String) {
        scope.launch {
            sendMessageFlow.emit(message)
        }
    }

    private fun collectMessage() {
        scope.launch {
            sendMessageFlow.collect {
                webSocket.send(it)
            }
        }
    }
}
package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.data.di.DefaultDispatcher
import com.youhajun.data.di.MyTaskWebSocketOkHttpClient
import com.youhajun.data.models.MyTaskCode
import com.youhajun.data.models.sealeds.WebSocketStateDTO
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject
import javax.inject.Singleton

@ViewModelScoped
class WebSocketDataSource @Inject constructor(
    @MyTaskWebSocketOkHttpClient private val client: OkHttpClient,
    @MyTaskWebSocketOkHttpClient private val request: Request,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) {

    private lateinit var webSocket:WebSocket
    private val mutex = Mutex()
    private val scope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    private val _socketFlow = MutableSharedFlow<WebSocketStateDTO>()
    val socketFlow: SharedFlow<WebSocketStateDTO> = _socketFlow

    private val pendingMessagesQueue:Queue<String> = LinkedList()
    private var isConnect = false

    private val webSocketListener = object : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            scope.launch {
                _socketFlow.emit(WebSocketStateDTO.Message(text))
            }
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            isConnect = true
            consumePendingMessage()
            scope.launch {
                _socketFlow.emit(WebSocketStateDTO.Open(response))
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            scope.launch {
                _socketFlow.emit(WebSocketStateDTO.Failure(t, response))
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            scope.launch {
                _socketFlow.emit(WebSocketStateDTO.Close(code, reason))
            }
        }
    }

    fun connect() {
        webSocket = client.newWebSocket(request, webSocketListener)
    }

    fun disconnect() {
        isConnect = false
        pendingMessagesQueue.clear()
        val callSuccess = webSocket.close(MyTaskCode.WEB_SOCKET_SUCCESS_CODE, null)
        if(!callSuccess) webSocket.cancel()
        scope.cancel()
    }

    fun sendMessage(message: String) {
        scope.launch {
            mutex.withLock {
                if(isConnect) {
                    webSocket.send(message)
                } else {
                    pendingMessagesQueue.add(message)
                }
            }
        }
    }

    private fun consumePendingMessage() {
        scope.launch {
            mutex.withLock {
                while(pendingMessagesQueue.isNotEmpty()) {
                    val msg = pendingMessagesQueue.poll()
                    msg?.let { webSocket.send(it) }
                }
            }
        }
    }
}
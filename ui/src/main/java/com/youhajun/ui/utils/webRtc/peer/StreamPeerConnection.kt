/*
 * Copyright 2023 Stream.IO, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.youhajun.ui.utils.webRtc.peer

import com.youhajun.ui.utils.webRtc.Loggers
import com.youhajun.ui.utils.webRtc.addRtcIceCandidate
import com.youhajun.ui.utils.webRtc.models.StreamPeerType
import com.youhajun.ui.utils.webRtc.managers.SDPManager
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.SessionDescription


/**
 * PeerConnection을 가지고 있는 실질적인 PeerConnection 객체로
 * Offer sdp, Answer sdp 를 생성하고 localDescription 과 remoteDescription 에 sdp 를 set 한다.
 * 또한 ice 후보를 추가하는 역할또한 함
 */
class StreamPeerConnection(
    private val type: StreamPeerType,
    private val mediaConstraints: MediaConstraints,
) {
    lateinit var connection: PeerConnection
        private set

    private val pendingIceMutex = Mutex()
    private val pendingIceCandidates = mutableListOf<IceCandidate>()

    init {
        Loggers.StreamConnection.setTypeTag(type)
    }

    fun initialize(peerConnection: PeerConnection) {
        Loggers.StreamConnection.initialize(peerConnection)
        this.connection = peerConnection
    }

    fun dispose() {
        connection.dispose()
    }

    suspend fun createOffer(): Result<SessionDescription> {
        Loggers.StreamConnection.createOffer()
        return SDPManager.observeCreatedSDP { connection.createOffer(it, mediaConstraints) }
    }

    suspend fun createAnswer(): Result<SessionDescription> {
        Loggers.StreamConnection.createAnswer()
        return SDPManager.observeCreatedSDP { connection.createAnswer(it, mediaConstraints) }
    }

    suspend fun setLocalDescription(sessionDescription: SessionDescription): Result<Unit> {
        Loggers.StreamConnection.setLocalDescription(sessionDescription)
        val sdp = SessionDescription(
            sessionDescription.type,
            sessionDescription.description.mungeCodecs()
        )
        return SDPManager.observeSetSDP { connection.setLocalDescription(it, sdp) }.also {
            consumePendingIceCandidate()
        }
    }

    suspend fun setRemoteDescription(sessionDescription: SessionDescription): Result<Unit> {
        Loggers.StreamConnection.setRemoteDescription(sessionDescription)
        val sdp = SessionDescription(
            sessionDescription.type,
            sessionDescription.description.mungeCodecs()
        )
        return SDPManager.observeSetSDP { connection.setRemoteDescription(it, sdp) }.also {
            consumePendingIceCandidate()
        }
    }

    suspend fun addIceCandidate(iceCandidate: IceCandidate): Result<Unit> {
        Loggers.StreamConnection.addIceCandidate(iceCandidate)

        return if (connection.signalingState() == PeerConnection.SignalingState.STABLE) {
            addPendingIceCandidate(iceCandidate)
            Result.failure(RuntimeException("RemoteDescription is not set"))
        } else {
            connection.addRtcIceCandidate(iceCandidate).also {
                Loggers.StreamConnection.successAddIceCandidate(it)
            }
        }
    }

    private suspend fun addPendingIceCandidate(iceCandidate: IceCandidate) {
        Loggers.StreamConnection.pendingIceCandidate(iceCandidate)
        pendingIceMutex.withLock {
            pendingIceCandidates.add(iceCandidate)
        }
    }

    /**
     * local, remote Description이 모두 set 되어 signalingState가 Stable이면
     * pending되어 있던 iceCandidate를 소비한다
     */
    private suspend fun consumePendingIceCandidate() {
        if (connection.signalingState() == PeerConnection.SignalingState.STABLE) {
            pendingIceMutex.withLock {
                pendingIceCandidates.forEach { iceCandidate ->
                    Loggers.StreamConnection.consumePendingIceCandidate(iceCandidate)
                    connection.addRtcIceCandidate(iceCandidate)
                }
                pendingIceCandidates.clear()
            }
        }
    }

    private fun String.mungeCodecs(): String {
        return this.replace("vp9", "VP9").replace("vp8", "VP8").replace("h264", "H264")
    }

    override fun toString(): String =
        "StreamPeerConnection(type='$type', constraints=$mediaConstraints)"
}

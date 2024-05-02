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
import com.youhajun.ui.utils.webRtc.WebRTCContract
import com.youhajun.model_ui.types.webrtc.StreamPeerType
import org.webrtc.CandidatePairChangeEvent
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.IceCandidateErrorEvent
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.RtpReceiver
import org.webrtc.RtpTransceiver


/**
 * PeerConnection의 옵저버로써 해당 역할만을 하도록 함
 */
class PeerConnectionObserver(
    private val type: StreamPeerType,
    private val peerConnectionListener: WebRTCContract.PeerConnectionListener,
) : PeerConnection.Observer {

    init {
        Loggers.Connection.setTypeTag(type)
    }

    /**
     * localDescription과 remoteDescription 둘 다 set 되면 iceCandidate 탐색을 시작하는데,
     * 이 때 새로운 ice후보가 발견될 때 마다 트리거됨
     */
    override fun onIceCandidate(candidate: IceCandidate?) {
        Loggers.Connection.onIceCandidate(candidate)

        if (candidate == null) return
        peerConnectionListener.onIceCandidate(candidate, type)
    }

    override fun onTrack(transceiver: RtpTransceiver?) {
        Loggers.Connection.onTrack(transceiver)
    }

    /**
     * 새로운 미디어 스트림이 생길 때마다 트리거됨
     * 최신 WebRTC에서 더 이상 사용되지 않음
     */
    override fun onAddStream(stream: MediaStream?) {
        Loggers.Connection.onAddStream(stream)
    }

    /**
     * 추가된 새로운 미디어 스트림 혹은 미디어 스트림 트랙이 있을 때마다 트리거됨
     * 특정 세션에 대한 오디오, 비디오 트랙이 포함됨
     */
    override fun onAddTrack(receiver: RtpReceiver?, mediaStreams: Array<out MediaStream>?) {
        Loggers.Connection.onAddTrack(receiver, mediaStreams)
        val id = mediaStreams?.firstOrNull()?.id ?: return
        val track = receiver?.track() ?: return

        peerConnectionListener.onStreamAdded(id, track)
    }

    /**
     * PeerConnection 을 active 시키기 위해 필요한 새로운 negotiation 이 있을 때마다 트리거됨
     */
    override fun onRenegotiationNeeded() {
        Loggers.Connection.onRenegotiationNeeded()
    }

    /**
     * connection state 가 변경될 때마다 트리거됨.
     * state 관찰을 시작하거나 멈출 때 사용됨
     */
    override fun onIceConnectionChange(newState: PeerConnection.IceConnectionState?) {
        Loggers.Connection.onIceConnectionChange(newState)

        when (newState) {
            PeerConnection.IceConnectionState.CLOSED,
            PeerConnection.IceConnectionState.FAILED,
            PeerConnection.IceConnectionState.DISCONNECTED,
            PeerConnection.IceConnectionState.CONNECTED -> Unit
            else -> Unit
        }
    }

    override fun onRemoveTrack(receiver: RtpReceiver?) {
        Loggers.Connection.onRemoveTrack(receiver)
    }

    override fun onSignalingChange(newState: PeerConnection.SignalingState?) {
        Loggers.Connection.onSignalingChange(newState)
    }

    override fun onIceConnectionReceivingChange(receiving: Boolean) {
        Loggers.Connection.onIceConnectionReceivingChange(receiving)
    }

    override fun onIceGatheringChange(newState: PeerConnection.IceGatheringState?) {
        Loggers.Connection.onIceGatheringChange(newState)
    }

    override fun onIceCandidatesRemoved(iceCandidates: Array<IceCandidate>?) {
        Loggers.Connection.onIceCandidatesRemoved(iceCandidates)
    }

    override fun onIceCandidateError(event: IceCandidateErrorEvent?) {
        Loggers.Connection.onIceCandidateError(event)
    }

    override fun onConnectionChange(newState: PeerConnection.PeerConnectionState?) {
        Loggers.Connection.onConnectionChange(newState)
    }

    override fun onSelectedCandidatePairChanged(event: CandidatePairChangeEvent?) {
        Loggers.Connection.onSelectedCandidatePairChanged(event)
    }

    override fun onDataChannel(channel: DataChannel?): Unit = Unit

    override fun onRemoveStream(var1: MediaStream?) {}
}
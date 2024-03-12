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

package com.youhajun.ui.utils.webRtc.managers

import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object SDPManager {
    suspend fun observeCreatedSDP(onCall: (SdpObserver) -> Unit): Result<SessionDescription> =
        suspendCoroutine {
            val observer = object : SdpObserver {
                override fun onCreateSuccess(sdp: SessionDescription?) {
                    if (sdp != null) {
                        it.resume(Result.success(sdp))
                    } else {
                        it.resume(Result.failure(RuntimeException("SessionDescription is null!")))
                    }
                }

                override fun onCreateFailure(error: String?) = it.resume(Result.failure(RuntimeException(error)))

                override fun onSetSuccess() = Unit
                override fun onSetFailure(error: String?) = Unit
            }
            onCall.invoke(observer)
        }

    suspend fun observeSetSDP(onCall: (SdpObserver) -> Unit): Result<Unit> = suspendCoroutine {
        val observer = object : SdpObserver {
            override fun onSetSuccess() = it.resume(Result.success(Unit))
            override fun onSetFailure(error: String?) = it.resume(Result.failure(RuntimeException(error)))

            override fun onCreateSuccess(sdp: SessionDescription?) = Unit
            override fun onCreateFailure(error: String?) = Unit
        }
        onCall.invoke(observer)
    }
}

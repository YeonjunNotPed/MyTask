package com.youhajun.model_ui.types.webrtc

import com.youhajun.model_data.types.WebRTCSessionTypeDto
import com.youhajun.model_ui.ToDto
import com.youhajun.model_ui.ToModel

enum class WebRTCSessionType(val type: String) {
    Active("active"),
    Creating("creating"),
    Ready("ready"),
    Impossible("impossible"),
    Offline("offline");

    companion object: ToDto<WebRTCSessionType, WebRTCSessionTypeDto>, ToModel<WebRTCSessionType, WebRTCSessionTypeDto> {
        fun typeOf(type: String): WebRTCSessionType {
            return entries.find {
                it.type == type
            } ?: Offline
        }

        override fun WebRTCSessionTypeDto.toModel(): WebRTCSessionType {
            return WebRTCSessionType.valueOf(this.name)
        }

        override fun WebRTCSessionType.toDto(): WebRTCSessionTypeDto {
            return WebRTCSessionTypeDto.valueOf(this.name)
        }
    }
}

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

package com.youhajun.ui.utils.webRtc.audio

sealed class AudioDevice {

  /** The friendly name of the device.*/
  abstract val name: String

  /** An [AudioDevice] representing a Bluetooth Headset.*/
  data class BluetoothHeadset internal constructor(override val name: String = "Bluetooth") :
    AudioDevice()

  /** An [AudioDevice] representing a Wired Headset.*/
  data class WiredHeadset internal constructor(override val name: String = "Wired Headset") :
    AudioDevice()

  /** An [AudioDevice] representing the Earpiece.*/
  data class Earpiece internal constructor(override val name: String = "Earpiece") : AudioDevice()

  /** An [AudioDevice] representing the Speakerphone.*/
  data class Speakerphone internal constructor(override val name: String = "Speakerphone") :
    AudioDevice()
}

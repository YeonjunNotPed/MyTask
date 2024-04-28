package com.youhajun.model_ui.states

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import com.youhajun.model_ui.types.gpt.GptType
import com.youhajun.model_ui.vo.gpt.GptAssistantVo
import com.youhajun.model_ui.vo.gpt.GptChannelVo
import com.youhajun.model_ui.vo.gpt.GptMessageVo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class GptState(
    val onLoading: Boolean = false,
    val onError: Boolean = false,
    val selectedGptType: GptType = GptType.CHAT_GPT_3_5_TURBO,
    val selectedRole: String = "",
    val drawerState:DrawerState = DrawerState(initialValue = DrawerValue.Closed),
    val gptChannelList: ImmutableList<GptChannelVo> = persistentListOf(),
    val gptTypeList: ImmutableList<GptType> = GptType.entries.filterNot { it.type == GptType.NONE.type }.toImmutableList(),
    val currentGptChannel: GptChannelVo? = null,
    val currentGptAssistants: ImmutableList<GptAssistantVo> = persistentListOf(),
    val currentGptMessages: ImmutableList<GptMessageVo> = persistentListOf(),
    val isRoleExpanded: Boolean = false,
    val roleList: ImmutableList<String> = persistentListOf(),
) {
    val wasSentMessage = currentGptChannel?.let { it.gptType == GptType.NONE } ?: false
}
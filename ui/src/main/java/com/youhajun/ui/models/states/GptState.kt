package com.youhajun.ui.models.states

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import com.youhajun.domain.models.enums.GptType
import com.youhajun.domain.models.vo.gpt.GptAssistantVo
import com.youhajun.domain.models.vo.gpt.GptMessageVo

data class GptState(
    val onLoading: Boolean = false,
    val onError: Boolean = false,
    val selectedGptType: GptType = GptType.CHAT_GPT_3_5_TURBO,
    val drawerState:DrawerState = DrawerState(initialValue = DrawerValue.Closed),
    val gptAssistantList: List<GptAssistantVo> = emptyList(),
    val gptMessageList: List<GptMessageVo> = emptyList(),
    val gptTypeList: List<GptType> = GptType.values().filterNot { it.type == GptType.NONE.type },
    val currentRole: String = "",
    val currentGptAssistant: GptAssistantVo? = null,
    val isRoleExpanded: Boolean = false,
    val roleList: List<String> = emptyList(),
) {
    val isShowSelectGptTypeList = currentGptAssistant?.let { it.gptType == GptType.NONE } ?: false
}
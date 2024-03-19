package com.youhajun.ui.components.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.domain.models.enums.GptMessageType
import com.youhajun.domain.models.sealeds.GptMessageFooterState
import com.youhajun.domain.models.vo.gpt.GptMessageVo
import com.youhajun.ui.R
import com.youhajun.ui.components.TypingAnimationComponent

@Composable
fun GptMessageItemComp(
    gptMessageVo: GptMessageVo,
    isTypingAnimationTarget: Boolean,
    typingTrigger: Boolean,
    role: String?,
) {

    val isQuestion = gptMessageVo.gptMessageType == GptMessageType.QUESTION
    val backgroundColor = if (isQuestion) colorResource(id = R.color.purple_200) else Color.White
    val textColor = if (isQuestion) Color.White else Color.Black
    val alignment = if (isQuestion) Alignment.End else Alignment.Start
    val shapeCornerRadius = 4.dp
    val textBoundaryPadding = 12.dp
    val defaultHorizontalPadding = 4.dp
    val defaultBottomPadding = 12.dp

    Column(
        horizontalAlignment = alignment,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(
                if (isQuestion) Modifier.padding(
                    start = textBoundaryPadding,
                    end = defaultHorizontalPadding,
                    bottom = defaultBottomPadding
                )
                else Modifier.padding(
                    start = defaultHorizontalPadding,
                    end = textBoundaryPadding,
                    bottom = defaultBottomPadding
                )
            )

    ) {
        if (!role.isNullOrEmpty() && !isQuestion) {
            Text(
                modifier = Modifier.wrapContentSize().padding(start = 4.dp),
                text = role,
                fontSize = 12.sp,
                fontWeight = FontWeight.W800,
                color = Color.Gray,
            )
        }

        Surface(
            shadowElevation = 4.dp,
            color = backgroundColor,
            shape = if (isQuestion) RoundedCornerShape(
                topEnd = shapeCornerRadius,
                topStart = shapeCornerRadius,
                bottomStart = shapeCornerRadius
            ) else RoundedCornerShape(
                topEnd = shapeCornerRadius,
                topStart = shapeCornerRadius,
                bottomEnd = shapeCornerRadius
            ),
            modifier = Modifier.wrapContentSize()
        ) {
            if (isTypingAnimationTarget) {
                TypingAnimationComponent(
                    gptMessageVo.message,
                    typingTrigger,
                    50L
                ) {
                    TextContentComp(it, textColor)
                }
            } else {
                TextContentComp(gptMessageVo.message, textColor)
            }
        }

        gptMessageVo.messageFooterState.forEach {
            when (it) {
                GptMessageFooterState.ShowCreateAt -> GptFooterCreateAt(gptMessageVo.createdAtUnixTimeStamp)
                is GptMessageFooterState.ShowRecommendingUrl -> GptFooterRecommendUrl(
                    recommendUrlVoList = it.recommendUrlVoList
                )
            }
        }
    }
}

@Composable
private fun TextContentComp(message: String, textColor: Color) {
    Text(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
        text = message,
        fontSize = 12.sp,
        fontWeight = FontWeight.W700,
        color = textColor,
    )
}
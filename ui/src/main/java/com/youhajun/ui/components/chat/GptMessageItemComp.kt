package com.youhajun.ui.components.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.domain.models.enums.GptMessageType
import com.youhajun.domain.models.sealeds.GptMessageFooterState
import com.youhajun.domain.models.vo.GptMessageVo
import com.youhajun.ui.R

@Composable
fun GptMessageItemComp(
    gptMessageVo: GptMessageVo
) {
    val (align, backgroundColor, textColor) = when (gptMessageVo.gptMessageType) {
        GptMessageType.ANSWER -> Triple(
            Alignment.End,
            Color.White,
            Color.Black
        )

        GptMessageType.QUESTION -> Triple(
            Alignment.End,
            colorResource(id = R.color.purple_200),
            Color.White
        )
    }
    Column(horizontalAlignment = align) {
        Surface(
            shadowElevation = 8.dp,
            color = backgroundColor,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                text = gptMessageVo.message,
                fontSize = 12.sp,
                fontWeight = FontWeight.W700,
                color = textColor,
            )
        }

        gptMessageVo.messageFooterState.forEach {
            when (it) {
                GptMessageFooterState.ShowCreateAt -> GptFooterCreateAt(gptMessageVo.createAt)
                is GptMessageFooterState.ShowRecommendingUrl -> GptFooterRecommendUrl(
                    recommendUrlVoList = it.recommendUrlVoList
                )
            }
        }
    }
}
package com.youhajun.ui.components.chat

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.model_ui.types.gpt.GptType
import com.youhajun.ui.R

@Composable
fun GptTypeItemComp(
    gptType: GptType,
    isSelected: Boolean,
    onClickGptType: (GptType) -> Unit
) {
    val scale = animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.95f,
        animationSpec = tween(
            durationMillis = 100,
            easing = LinearEasing
        ),
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else colorResource(
            id = R.color.color_fbfbf9
        ).copy(alpha = 0.8f), label = "backgroundColorAnime"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.Black else colorResource(
            id = R.color.color_292929
        ), label = "textColorAnime"
    )
    val iconId = when (gptType) {
        GptType.CHAT_GPT_3_5_TURBO -> R.drawable.ic_chat_gpt_logo_green
        GptType.CHAT_GPT_4_TURBO_PREVIEW -> R.drawable.ic_chat_gpt_logo_purple
        GptType.GEMINI -> R.drawable.ic_gemini_logo
        GptType.NONE -> throw IllegalAccessException("gptTypeNone")
    }
    Row(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .scale(scale.value)
            .clip(shape = RoundedCornerShape(14.dp))
            .background(color = backgroundColor)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 100,
                    easing = LinearEasing
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClickGptType(gptType)
            }
            .padding(
                horizontal = 8.dp,
                vertical = 3.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(17.dp).scale(scale.value)
        )

        Spacer(modifier = Modifier.width(2.dp))

        Text(
            text = gptType.type,
            fontSize = 12.sp,
            fontWeight = FontWeight.W800,
            color = textColor,
            modifier = Modifier.scale(scale.value)
        )
    }
}
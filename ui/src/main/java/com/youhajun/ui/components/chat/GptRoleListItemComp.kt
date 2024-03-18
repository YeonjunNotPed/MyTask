package com.youhajun.ui.components.chat

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.ui.R

@Composable
fun GptRoleListItemComp(
    role: String,
    isSelected: Boolean,
    onClickDeleteRole: (String) -> Unit,
    onClickRole: (String) -> Unit
) {

    val scale = animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.9f,
        animationSpec = tween(
            durationMillis = 100,
            easing = LinearEasing
        ),
        label = "scale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) colorResource(id = R.color.color_add8e6)
        else colorResource(id = R.color.color_e6f4fa).copy(alpha = 0.9f),
        label = "backgroundColorAnime"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.LightGray.copy(alpha = 0.9f),
        label = "textColorAnime"
    )

    Row(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClickRole(role)
            }
            .wrapContentSize()
            .scale(scale.value)
            .clip(shape = RoundedCornerShape(14.dp))
            .background(color = backgroundColor)
            .padding(
                horizontal = 6.dp,
                vertical = 3.dp
            )
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 100,
                    easing = LinearEasing
                )
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = role,
            fontSize = 8.sp,
            fontWeight = FontWeight.W800,
            color = textColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            modifier = Modifier
                .size(6.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClickDeleteRole(role) },
            imageVector = Icons.Filled.Clear,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}
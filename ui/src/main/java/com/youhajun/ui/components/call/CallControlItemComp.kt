package com.youhajun.ui.components.call

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.youhajun.domain.models.sealeds.CallControlAction
import com.youhajun.ui.R
import com.youhajun.ui.models.holder.CallControlActionHolder

@Composable
fun CallControlItemComp(
    callControlActionHolder: CallControlActionHolder,
    onClick: (CallControlAction) -> Unit
) {
    callControlActionHolder.run {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (isEnable) backgroundColor else colorResource(id = R.color.color_161616))
                .clickable {
                    if (!isEnable) return@clickable
                    onClick(callAction)
                }
        ) {
            Icon(
                modifier = Modifier
                    .size(26.dp)
                    .align(Alignment.Center),
                tint = if (isEnable) iconTint else colorResource(id = R.color.color_373737),
                painter = painterResource(id = icon),
                contentDescription = null
            )
        }
    }
}
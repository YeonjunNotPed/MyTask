package com.youhajun.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.youhajun.model_ui.types.gpt.GptType
import com.youhajun.model_ui.vo.gpt.GptChannelVo
import com.youhajun.ui.R


@Composable
fun GptChannelDrawerItemComp(
    item: GptChannelVo,
    isCurrentChannel: Boolean,
    onClickChannel: (idx: Long) -> Unit,
    onClickDeleteChannel: (idx: Long) -> Unit,
) {

    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    val gptType = item.gptType
    val lastQuestion = item.lastQuestion
    val gptTypeText =
        if (gptType == GptType.NONE) stringResource(id = R.string.gpt_drawer_item_default_title) else gptType.type
    val lastQuestionText: String =
        if (lastQuestion.isNullOrEmpty()) stringResource(id = R.string.gpt_drawer_item_default_content) else lastQuestion
    val backgroundColor =
        if (isCurrentChannel) colorResource(id = R.color.color_add8e6) else Color.LightGray
    val title = if(item.roleOfAi.isNullOrEmpty()) gptTypeText else "${item.roleOfAi} - $gptTypeText"

    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .border(1.dp, Color.White, RoundedCornerShape(8.dp))
            .padding(0.5.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClickChannel(item.channelIdx) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.W600,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = lastQuestionText,
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.W800,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            contentAlignment = Alignment.BottomStart
        ) {
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = null,
                modifier = Modifier
                    .clickable { dropDownMenuExpanded = !dropDownMenuExpanded }
                    .size(24.dp),
                tint = Color.Gray
            )

            DropdownMenu(
                expanded = dropDownMenuExpanded,
                onDismissRequest = { dropDownMenuExpanded = false },
                properties = PopupProperties(
                    focusable = true,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                DropdownMenuItem(
                    onClick = { onClickDeleteChannel(item.channelIdx) },
                    text = {
                        Text(
                            text = stringResource(id = R.string.gpt_drawer_item_dropdown_delete_channel),
                            fontSize = 13.sp,
                            color = Color.Black,
                        )
                    },
                )
            }
        }
    }
}
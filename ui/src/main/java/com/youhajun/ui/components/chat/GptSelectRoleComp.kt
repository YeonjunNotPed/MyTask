package com.youhajun.ui.components.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.ui.R
import kotlinx.collections.immutable.ImmutableList

@Composable
fun GptSelectRoleComp(
    modifier: Modifier = Modifier,
    selectedRole:String,
    isExpanded: Boolean,
    roleList: ImmutableList<String>,
    addRoleInput: String,
    onAddRoleValueChange: (String) -> Unit,
    onClickExpandIcon: () -> Unit,
    onClickRole: (String) -> Unit,
    onClickAddRole: () -> Unit,
    onClickDeleteRole: (String) -> Unit
) {
    Column(
        modifier = modifier
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .padding(0.5.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(8.dp)
            .width(200.dp)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.gpt_select_role),
                fontSize = 13.sp,
                color = Color.Black,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.Center
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClickExpandIcon() }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    tint = colorResource(id = R.color.purple_200),
                    contentDescription = null
                )
            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Column {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    items(
                        items = roleList,
                        key = { it },
                    ) {
                        val isSelected = selectedRole == it
                        GptRoleListItemComp(it, isSelected, onClickRole = onClickRole, onClickDeleteRole = onClickDeleteRole)
                    }
                }

                GptAddRoleComp(addRoleInput, onAddRoleValueChange, onClickAddRole)
            }
        }
    }
}
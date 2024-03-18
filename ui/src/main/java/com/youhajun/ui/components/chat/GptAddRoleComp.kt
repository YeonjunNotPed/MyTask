package com.youhajun.ui.components.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.ui.R
import com.youhajun.ui.components.input.MyTaskTextField

@Composable
fun GptAddRoleComp(
    addRoleInput: String,
    onAddRoleValueChange: (String) -> Unit,
    onClickAddRole: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MyTaskTextField(
            value = addRoleInput,
            onValueChange = onAddRoleValueChange,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .horizontalScroll(rememberScrollState())
                .border(
                    border = BorderStroke(1.dp, colorResource(id = R.color.color_fbfbf9)),
                    shape = RoundedCornerShape(12.dp)
                ),
            textStyle = TextStyle(color = Color.Black, fontSize = 11.sp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    stringResource(id = R.string.gpt_add_role_hint),
                    style = TextStyle(
                        colorResource(id = R.color.color_b9b9b9),
                        fontSize = 11.sp
                    ),
                )
            },

            cursorBrush = SolidColor(Color.Gray),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = colorResource(id = R.color.color_e6f4fa),
                unfocusedContainerColor = colorResource(id = R.color.color_e6f4fa),
            ),
            paddingValues = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
        )


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable { onClickAddRole() }
                .wrapContentSize()
                .aspectRatio(1f)
        ) {
            Icon(
                Icons.Filled.AddCircle,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = colorResource(id = R.color.purple_200)
            )
        }
    }
}
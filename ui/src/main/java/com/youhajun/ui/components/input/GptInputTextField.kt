package com.youhajun.ui.components.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.ui.R

@Composable
fun GptInputTextField(
    modifier: Modifier = Modifier,
    input: String,
    onValueChange: (String) -> Unit,
    onClickSend: () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            MyTaskTextField(
                value = input,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                minLines = 1,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                placeholder = {
                    Text(
                        stringResource(id = R.string.gpt_input_hint),
                        style = TextStyle(
                            colorResource(id = R.color.color_b9b9b9),
                            fontSize = 14.sp
                        ),
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = colorResource(id = R.color.color_e6f4fa),
                    unfocusedContainerColor = colorResource(id = R.color.color_e6f4fa),
                ),

                cursorBrush = SolidColor(Color.Gray),
                paddingValues = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable { onClickSend.invoke() }
                    .wrapContentSize()
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(24.dp)
                )
            }
        }
    }
}
package com.youhajun.ui.components.input

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.ui.R

@Composable
fun TopTitleInput(
    @StringRes titleTextRes: Int = R.string.login_email_title,
    @StringRes hintTextRes: Int = R.string.login_email_hint,
    keyboardType: KeyboardType = KeyboardType.Password,
    content: String = "",
    onValueChange: (String) -> Unit = {},
) {

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val visualTrans = when (keyboardType) {
        KeyboardType.Password -> if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = stringResource(id = titleTextRes),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.W400
        )
        MyTaskTextField(
            value = content,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .border(
                    width = 1.dp, color = colorResource(
                        id = R.color.color_b9b9b9
                    ), RoundedCornerShape(12.dp)
                ),
            textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    stringResource(id = hintTextRes),
                    color = colorResource(id = R.color.color_b9b9b9),
                    fontSize = 14.sp,
                )
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = colorResource(id = R.color.color_111111),
                unfocusedContainerColor = colorResource(id = R.color.color_111111),
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType
            ),
            paddingValues = PaddingValues(horizontal = 10.dp, vertical = 7.dp),
            visualTransformation = visualTrans,
            trailingIcon = when (keyboardType) {
                KeyboardType.Password -> ({
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = { passwordVisible = !passwordVisible },
                    ) {
                        Icon(imageVector = image, null, tint = colorResource(id = R.color.color_b9b9b9))
                    }
                })

                else -> null
            }
        )
    }
}
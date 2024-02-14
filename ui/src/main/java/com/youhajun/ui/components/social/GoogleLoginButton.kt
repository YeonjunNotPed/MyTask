package com.youhajun.ui.components.social

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.ui.R

@Preview(showBackground = true)
@Composable
fun GoogleLoginButton(onClick:()-> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
            .fillMaxWidth()
            .height(48.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        colors = ButtonColors(
            containerColor = Color.White,
            contentColor = Color.Unspecified,
            disabledContainerColor = colorResource(id = R.color.color_fee500),
            disabledContentColor = Color.Unspecified,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google_login),
            contentDescription = null
        )
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.login_google_social_login),
            fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.W600,
            textAlign = TextAlign.Center
        )
    }
}
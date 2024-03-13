package com.youhajun.ui.components.chat

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.domain.models.vo.GptRecommendUrlVo

@Composable
fun GptFooterCreateAt(createAt: String) {
    Text(
        text = createAt,
        fontSize = 8.sp,
        fontWeight = FontWeight.W400,
        color = Color.Gray,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
    )
}

@Composable
fun GptFooterRecommendUrl(recommendUrlVoList: List<GptRecommendUrlVo>) {

}
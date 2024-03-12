package com.youhajun.ui.components.purchase

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.domain.model.vo.PurchaseInAppItemVo
import com.youhajun.ui.R

@Composable
fun PurchaseInAppItemComp(
    purchaseInAppItemVo: PurchaseInAppItemVo,
    onClickItem: (PurchaseInAppItemVo) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.color_373737))
            .fillMaxWidth()
            .height(60.dp)
            .border(1.dp, colorResource(id = R.color.color_b9b9b9), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_google_store),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(18.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Row(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(id = R.string.item_count, purchaseInAppItemVo.itemCount),
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                color = Color.White,
                modifier = Modifier.wrapContentSize()
            )
        }

        Button(
            onClick = { onClickItem.invoke(purchaseInAppItemVo) },
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(horizontal = 14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.color_add8e6), contentColor = Color.Black
            ),
            modifier = Modifier
                .height(40.dp)
                .wrapContentWidth()
        ) {
            Text(text = purchaseInAppItemVo.price)
        }

    }
}
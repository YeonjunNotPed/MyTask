package com.youhajun.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
fun PurchaseInAppMultipleItemComp(
    purchaseInAppItemVo: PurchaseInAppItemVo,
    multiplePurchaseCount: Int,
    onClickItem: (PurchaseInAppItemVo) -> Unit,
    onClickMultipleCountControlPlus: () -> Unit,
    onClickMultipleCountControlMinus: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .background(colorResource(id = R.color.color_373737))
            .border(1.dp, colorResource(id = R.color.color_b9b9b9), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .wrapContentSize(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
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
                    containerColor = colorResource(id = R.color.color_add8e6),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .height(40.dp)
                    .wrapContentWidth()
            ) {
                Text(text = stringResource(id = R.string.store_in_app_multiple_item_per_price,purchaseInAppItemVo.price))
            }

        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(
                    id = R.string.store_in_app_multiple_item_purchase_count,
                    multiplePurchaseCount
                ),
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                color = Color.White,
                modifier = Modifier.wrapContentSize()
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.width(80.dp)
            ) {
                Icon(imageVector = Icons.Outlined.AddCircleOutline,
                    null,
                    tint = colorResource(id = R.color.color_b9b9b9),
                    modifier = Modifier.clickable { onClickMultipleCountControlPlus() })
                Icon(imageVector = Icons.Outlined.RemoveCircleOutline,
                    null,
                    tint = colorResource(id = R.color.color_b9b9b9),
                    modifier = Modifier.clickable { onClickMultipleCountControlMinus() })
            }
        }
    }
}
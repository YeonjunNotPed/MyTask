package com.youhajun.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youhajun.ui.R
import com.youhajun.domain.model.vo.PurchaseItemVo

@Preview(showBackground = true)
@Composable
fun prev() {
    PurchaseItem(purchaseItemVo = PurchaseItemVo(0,"", 70, "200"), onClickItem = {

    })
}

@Composable
fun PurchaseItem(purchaseItemVo: PurchaseItemVo, onClickItem: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_google_store),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(45.dp)
                .width(45.dp)
        )

        Spacer(modifier = Modifier.width(5.dp))


        Text(
            text = stringResource(id = R.string.item_count, purchaseItemVo.itemCount),
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier.weight(1f)
        )


        Button(
            onClick = { onClickItem.invoke(purchaseItemVo.idx) },
            shape = RoundedCornerShape(30.dp),
            contentPadding = PaddingValues(horizontal = 18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.purple_200),
                contentColor = Color.White
            ),
            modifier = Modifier.size(width = 80.dp, height = 40.dp)
        ) {
            Text(text = stringResource(id = R.string.item_price, purchaseItemVo.itemPrice),)
        }

    }
}

//@Composable
//fun ListItemDevice(deviceInfo: DeviceInfo, onClickDelete: (Int) -> Unit) {
//    Row(
//        modifier = Modifier
//            .background(Color.LightGray)
//            .fillMaxWidth()
//            .height(80.dp)
//            .padding(8.dp),
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.outline_home_24),
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxHeight()
//                .aspectRatio(1f)
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxHeight()
//                .weight(1f)
//                .padding(horizontal = 12.dp),
//            verticalArrangement = Arrangement.SpaceEvenly
//        ) {
//
//            Text(
//                text = deviceInfo.deviceNumber,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.W500,
//                color = Color.Black
//            )
//
//            Text(
//                text = deviceInfo.address,
//                fontSize = 10.sp,
//                fontWeight = FontWeight.W300,
//                color = Color.Black
//            )
//        }
//
//        Text(
//            text = stringResource(id = R.string.delete),
//            color = Color.Gray,
//            fontSize = 14.sp,
//            modifier = Modifier
//                .align(Alignment.Top)
//                .clickable {
//                    onClickDelete.invoke(deviceInfo.idx)
//                }
//        )
//    }
//}
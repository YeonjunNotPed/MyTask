package com.youhajun.ui.components.purchase

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import com.youhajun.model_ui.types.store.SubsGradeType
import com.youhajun.model_ui.vo.store.PurchaseSubsItemVo
import com.youhajun.ui.R
import com.youhajun.ui.graphics.modifier.conditional
import com.youhajun.ui.graphics.modifier.shimmerBadgeModifier

@Composable
fun PurchaseSubsItemComp(
    isCurrentSubs: Boolean,
    purchaseSubsItemVo: PurchaseSubsItemVo,
    onClickItem: (String, String) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .clickable {
                onClickItem(purchaseSubsItemVo.productId, purchaseSubsItemVo.basePlanId)
            }
            .fillMaxWidth()
            .wrapContentHeight()
            .border(1.dp, colorResource(id = R.color.color_b9b9b9), RoundedCornerShape(12.dp))
            .padding(0.5.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .defaultMinSize(minHeight = 140.dp)
            .background(colorResource(id = R.color.color_373737))
            .padding(horizontal = 10.dp, vertical = 8.dp),
    ) {
        val (image, title, content, price, subsPeriod, badge) = createRefs()

        val gradeIcon = when (purchaseSubsItemVo.subsGrade) {
            SubsGradeType.GOLD -> R.drawable.ic_grade_gold
            SubsGradeType.SILVER -> R.drawable.ic_grade_silver
            SubsGradeType.NONE,
            SubsGradeType.BRONZE -> R.drawable.ic_grade_bronze
        }
        Image(
            painter = painterResource(gradeIcon),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(50.dp)
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        )

        Text(
            text = purchaseSubsItemVo.subsTitle,
            fontSize = 24.sp,
            fontWeight = FontWeight.W800,
            color = Color.White,
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(title) {
                    start.linkTo(image.end, margin = 10.dp)
                    top.linkTo(image.top)
                    bottom.linkTo(image.bottom)
                }
        )

        Text(
            text = stringResource(id = R.string.store_current_subscribe),
            fontSize = 11.sp,
            fontWeight = FontWeight.W600,
            color = Color.Black,
            modifier = Modifier
                .wrapContentSize()
                .conditional(isCurrentSubs) {
                    shimmerBadgeModifier(colorResource(id = R.color.color_add8e6))
                }
                .constrainAs(badge) {
                    start.linkTo(title.start)
                    top.linkTo(title.bottom, margin = 4.dp)
                    visibility = if (isCurrentSubs) Visibility.Visible else Visibility.Gone
                }
        )

        Text(
            text = purchaseSubsItemVo.subsContent,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400,
            color = Color.White,
            lineHeight = 18.sp,
            modifier = Modifier
                .constrainAs(content) {
                    start.linkTo(title.start)
                    end.linkTo(subsPeriod.start, margin = 10.dp)
                    top.linkTo(badge.bottom, margin = 4.dp, goneMargin = 8.dp)
                    width = Dimension.fillToConstraints
                }
        )

        Text(
            text = purchaseSubsItemVo.price,
            fontSize = 18.sp,
            fontWeight = FontWeight.W600,
            color = Color.White,
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(price) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )

        Text(
            text = purchaseSubsItemVo.subsPeriod,
            fontSize = 15.sp,
            fontWeight = FontWeight.W500,
            color = colorResource(id = R.color.color_b9b9b9),
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(subsPeriod) {
                    end.linkTo(parent.end)
                    top.linkTo(price.bottom, margin = 4.dp)
                }
        )
    }
}
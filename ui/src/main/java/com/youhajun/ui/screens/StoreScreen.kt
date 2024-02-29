package com.youhajun.ui.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.domain.model.enums.PurchaseType
import com.youhajun.domain.model.vo.PurchaseInAppItemVo
import com.youhajun.domain.model.vo.PurchaseSubsItemVo
import com.youhajun.ui.R
import com.youhajun.ui.components.PurchaseInAppItemComp
import com.youhajun.ui.components.PurchaseInAppMultipleItemComp
import com.youhajun.ui.components.PurchaseSubsItemComp
import com.youhajun.ui.models.sideEffects.StoreSideEffect
import com.youhajun.ui.models.states.StoreState
import com.youhajun.ui.utils.getActivity
import com.youhajun.ui.viewModels.StoreViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StoreScreen(
    viewModel: StoreViewModel = hiltViewModel(),
    onNavigate: (StoreSideEffect.Navigation) -> Unit = { }
) {

    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is StoreSideEffect.Toast -> Toast.makeText(context, it.text, Toast.LENGTH_SHORT)
                    .show()

                is StoreSideEffect.Navigation -> onNavigate.invoke(it)
                is StoreSideEffect.BillingLaunch -> {
                    context.getActivity()?.let { activity -> it.onBillingLaunch(activity) }
                }
            }
        }
    }

    val titles = listOf(
        stringResource(id = R.string.store_tab_title_purchase),
        stringResource(id = R.string.store_tab_title_charge)
    )
    val pagerState = rememberPagerState(pageCount = {
        2
    })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.color_161616))
    ) {
        TabRow(
            containerColor = colorResource(id = R.color.color_292929),
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    height = 1.dp,
                    color = colorResource(id = R.color.purple_500)
                )
            }) {
            titles.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier.height(52.dp),
                    text = {
                        Text(
                            title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W800,
                            color = Color.White,
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = { pagerState.currentPage },
                )
            }
        }
        HorizontalPager(pagerState) { page ->
            if (page == 0) PurchasePage(state,
                onClickCheckItemHistory = { viewModel.onClickCheckItemHistory() },
                onClickInAppPurchaseItem = { viewModel.onClickInAppPurchaseItem(it.productId, false) },
                onClickInAppMultiplePurchaseItem = { viewModel.onClickInAppPurchaseItem(it.productId, true) },
                onClickSubsPurchaseItem = { viewModel.onClickSubsPurchaseItem(it.productId, it.basePlanId) },
                onClickMultipleItemMinus = { viewModel.onClickMultipleItemCountControl(false) },
                onClickMultipleItemPlus = { viewModel.onClickMultipleItemCountControl(true) })
            else ChargePage()
        }
    }

}

@Composable
fun PurchasePage(
    state: StoreState,
    onClickInAppPurchaseItem: (PurchaseInAppItemVo) -> Unit,
    onClickInAppMultiplePurchaseItem: (PurchaseInAppItemVo) -> Unit,
    onClickSubsPurchaseItem: (PurchaseSubsItemVo) -> Unit,
    onClickMultipleItemPlus: () -> Unit,
    onClickMultipleItemMinus: () -> Unit,
    onClickCheckItemHistory: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.color_161616))
            .padding(horizontal = 24.dp, vertical = 15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.store_purchase_item),
                fontWeight = FontWeight.W800, fontSize = 16.sp,
                color = Color.White
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(id = R.string.store_current_item_count),
                    fontWeight = FontWeight.W400, fontSize = 11.sp,
                    color = colorResource(id = R.color.color_b9b9b9)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable { onClickCheckItemHistory() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google_store),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.size(12.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = state.currentItemCount.toString(),
                        fontWeight = FontWeight.W400,
                        fontSize = 15.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(
                items = state.purchaseInAppItemList,
                key = { it.idx },
            ) {
                if (it.purchaseType == PurchaseType.IN_APP) {
                    PurchaseInAppItemComp(it, onClickInAppPurchaseItem)
                } else {
                    PurchaseInAppMultipleItemComp(
                        it,
                        state.multiplePurchaseCount,
                        onClickInAppMultiplePurchaseItem,
                        onClickMultipleItemPlus,
                        onClickMultipleItemMinus
                    )
                }
            }
            items(
                items = state.purchaseSubsItemList,
                key = { it.idx },
            ) {
                val isCurrentGrade = state.currentGrade == it.subsGrade
                PurchaseSubsItemComp(isCurrentGrade, it, onClickSubsPurchaseItem)
            }
        }
    }

}

@Composable
fun ChargePage() {

}
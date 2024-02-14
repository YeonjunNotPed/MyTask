package com.youhajun.ui.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youhajun.ui.R
import com.youhajun.ui.components.PurchaseItem
import com.youhajun.ui.models.sideEffects.StoreSideEffect
import com.youhajun.ui.models.states.StoreState
import com.youhajun.ui.viewModels.StoreViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StoreScreen(
    viewModel: StoreViewModel = hiltViewModel(),
    onNavigate: (StoreSideEffect.Navigation) -> Unit
) {

    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is StoreSideEffect.Toast -> Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                is StoreSideEffect.Navigation -> onNavigate.invoke(it)
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
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(selectedTabIndex = pagerState.currentPage, indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
            )
        }) {
            titles.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Text(
                            title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W800,
                            color = Color.Black,
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = { pagerState.currentPage },
                )
            }
        }
        HorizontalPager(pagerState) { page ->
            if (page == 0) PurchasePage(state,
                onClickCheckCurrentItem = { viewModel.onClickCheckCurrentItem() },
                onClickPurchaseItem = { viewModel.onClickPurchaseItem(it) })
            else ChargePage()
        }
    }

}

@Composable
fun PurchasePage(
    state: StoreState = StoreState(),
    onClickPurchaseItem: (Int) -> Unit = {},
    onClickCheckCurrentItem: () -> Unit = {}
) {
    val purchaseItemList = remember { mutableStateOf(state.purchaseItemList) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.store_purchase_item),
                    fontWeight = FontWeight.W800, fontSize = 16.sp,
                    color = Color.Black
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(id = R.string.store_current_item_count),
                        fontWeight = FontWeight.W400, fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Box(modifier = Modifier.clickable { onClickCheckCurrentItem() }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = state.currentItemCount.toString(),
                                fontWeight = FontWeight.W400, fontSize = 15.sp,
                                color = Color.Black,
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
        }
        items(
            items = purchaseItemList.value,
            key = { it.idx },
        ) {
            PurchaseItem(it, onClickPurchaseItem)
        }
    }

}

@Composable
fun ChargePage() {

}
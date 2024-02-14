package com.youhajun.ui.models.states

import com.youhajun.domain.model.vo.PurchaseItemVo

data class StoreState(
    val onProgress: Boolean = false,
    val onError: Boolean = false,
    val purchaseItemList: List<PurchaseItemVo> = emptyList(),
    val currentItemCount: Int = 0
)
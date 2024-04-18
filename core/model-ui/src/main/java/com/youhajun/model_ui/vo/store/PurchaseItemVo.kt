package com.youhajun.model_ui.vo.store

import com.youhajun.model_ui.types.store.PurchaseType

interface PurchaseItemVo {
    val idx: Long
    val productId: String
    val purchaseType: PurchaseType
    val price: String
}
package com.youhajun.domain.model.vo

import com.youhajun.domain.model.enums.PurchaseType

interface PurchaseItemVo {
    val idx: Int
    val productId: String
    val purchaseType: PurchaseType
    val price: String
}
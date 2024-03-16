package com.youhajun.domain.models.vo

import com.youhajun.domain.models.enums.PurchaseType

interface PurchaseItemVo {
    val idx: Long
    val productId: String
    val purchaseType: PurchaseType
    val price: String
}
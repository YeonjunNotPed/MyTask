package com.youhajun.data.model.dto.store

import com.google.gson.annotations.SerializedName

data class PurchaseItemInfo(
    @SerializedName("items")
    val items:List<PurchaseItem>,
)


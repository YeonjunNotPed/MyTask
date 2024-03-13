package com.youhajun.data.models.dto.store

import com.google.gson.annotations.SerializedName

data class PurchaseVerifyRequest(
    @SerializedName("purchaseType")
    val purchaseType: String,
    @SerializedName("orderId")
    val orderId:String,
    @SerializedName("productId")
    val productId: List<String>,
    @SerializedName("purchaseToken")
    val purchaseToken: String
)
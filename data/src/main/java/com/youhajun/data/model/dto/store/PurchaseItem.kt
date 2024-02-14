package com.youhajun.data.model.dto.store

import com.google.gson.annotations.SerializedName

data class PurchaseItem(
    @SerializedName("idx")
    val idx:Int,
    @SerializedName("itemCount")
    val itemCount:Int,
    @SerializedName("itemPrice")
    val itemPrice:String,
)


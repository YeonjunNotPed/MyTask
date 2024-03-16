package com.youhajun.data.models.dto.store

import com.google.gson.annotations.SerializedName

data class PurchaseInAppItem(
    @SerializedName("idx")
    private val _idx: Long?,

    @SerializedName("productId")
    private val _productId: String?,

    @SerializedName("purchaseType")
    private val _purchaseType: String?,

    @SerializedName("price")
    private val _price: String?,

    @SerializedName("itemIcon")
    private val _itemIcon: String?,

    @SerializedName("itemCount")
    private val _itemCount: Int?,
) : PurchaseItem {
    override val idx: Long
        get() = _idx ?: -1
    override val productId: String
        get() = _productId ?: ""
    override val purchaseType: String
        get() = _purchaseType ?: ""
    override val price: String
        get() = _price ?: ""
    val itemCount: Int
        get() = _itemCount ?: -1
    val itemIcon: String
        get() = _itemIcon ?: ""
}
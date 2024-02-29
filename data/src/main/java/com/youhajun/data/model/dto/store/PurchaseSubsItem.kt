package com.youhajun.data.model.dto.store

import com.google.gson.annotations.SerializedName

data class PurchaseSubsItem(
    @SerializedName("idx")
    private val _idx: Int?,

    @SerializedName("productId")
    private val _productId: String?,

    @SerializedName("purchaseType")
    private val _purchaseType: String?,

    @SerializedName("price")
    private val _price: String?,

    @SerializedName("subsTitle")
    private val _subsTitle: String?,

    @SerializedName("subsContent")
    private val _subsContent: String?,

    @SerializedName("basePlanId")
    private val _basePlanId: String?,

    @SerializedName("subsPeriod")
    private val _subsPeriod: String?,

    @SerializedName("subsGrade")
    private val _subsGrade: String?,
) : PurchaseItem {
    override val idx: Int
        get() = _idx ?: -1
    override val productId: String
        get() = _productId ?: ""
    override val purchaseType: String
        get() = _purchaseType ?: ""
    override val price: String
        get() = _price ?: ""
    val subsTitle: String
        get() = _subsTitle ?: ""
    val subsContent: String
        get() = _subsContent ?: ""
    val subsPeriod: String
        get() = _subsPeriod ?: ""
    val basePlanId: String
        get() = _basePlanId ?: ""
    val subsGrade: String
        get() = _subsGrade ?: ""
}
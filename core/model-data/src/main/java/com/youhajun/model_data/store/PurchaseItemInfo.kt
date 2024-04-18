package com.youhajun.model_data.store

import com.google.gson.annotations.SerializedName

data class PurchaseItemInfo(
    @SerializedName("currentItemCount")
    private val _currentItemCount: Int?,
    @SerializedName("currentGrade")
    private val _currentGrade: String?,
    @SerializedName("inAppItems")
    private val _inAppItems: List<PurchaseInAppItem>?,
    @SerializedName("subsItems")
    private val _subsItems: List<PurchaseSubsItem>?
) {
    val currentItemCount: Int
        get() = _currentItemCount ?: -1
    val currentGrade: String
        get() = _currentGrade ?: ""
    val inAppItems: List<PurchaseInAppItem>
        get() = _inAppItems ?: emptyList()
    val subsItems: List<PurchaseSubsItem>
        get() = _subsItems ?: emptyList()
}
package com.youhajun.model_ui.vo.store

import com.youhajun.model_data.store.PurchaseSubsItem
import com.youhajun.model_ui.ToModel
import com.youhajun.model_ui.types.store.PurchaseType
import com.youhajun.model_ui.types.store.SubsGradeType

data class PurchaseSubsItemVo(
    override val idx: Long,
    override val productId: String,
    override val purchaseType: PurchaseType,
    override val price: String,
    val subsTitle: String,
    val subsPeriod: String,
    val subsContent: String,
    val basePlanId: String,
    val subsGrade: SubsGradeType
) : PurchaseItemVo {
    companion object : ToModel<PurchaseSubsItemVo, PurchaseSubsItem> {
        override fun PurchaseSubsItem.toModel(): PurchaseSubsItemVo = PurchaseSubsItemVo(
            idx,
            productId,
            PurchaseType.typeOf(purchaseType),
            price,
            subsTitle,
            subsPeriod,
            subsContent,
            basePlanId,
            SubsGradeType.typeOf(subsGrade)
        )
    }
}
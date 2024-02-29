package com.youhajun.domain.model.vo

import com.youhajun.data.model.dto.store.PurchaseSubsItem
import com.youhajun.domain.model.Mapper
import com.youhajun.domain.model.enums.PurchaseType
import com.youhajun.domain.model.enums.SubsGradeType

data class PurchaseSubsItemVo(
    override val idx: Int,
    override val productId: String,
    override val purchaseType: PurchaseType,
    override val price: String,
    val subsTitle: String,
    val subsPeriod: String,
    val subsContent: String,
    val basePlanId: String,
    val subsGrade: SubsGradeType
) : PurchaseItemVo {
    companion object : Mapper.ResponseMapper<PurchaseSubsItem, PurchaseSubsItemVo> {
        override fun mapDtoToModel(type: PurchaseSubsItem): PurchaseSubsItemVo {
            return type.run {
                PurchaseSubsItemVo(
                    idx = idx,
                    productId = productId,
                    purchaseType = PurchaseType.typeOf(purchaseType),
                    price = price,
                    subsTitle = subsTitle,
                    subsPeriod = subsPeriod,
                    basePlanId = basePlanId,
                    subsContent = subsContent,
                    subsGrade = SubsGradeType.typeOf(subsGrade)
                )
            }
        }
    }
}
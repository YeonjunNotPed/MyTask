package com.youhajun.domain.models.vo

import com.youhajun.data.models.dto.store.PurchaseSubsItem
import com.youhajun.domain.models.Mapper
import com.youhajun.domain.models.enums.PurchaseType
import com.youhajun.domain.models.enums.SubsGradeType

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
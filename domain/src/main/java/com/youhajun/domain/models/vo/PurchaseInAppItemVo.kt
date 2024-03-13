package com.youhajun.domain.models.vo

import com.youhajun.data.models.dto.store.PurchaseInAppItem
import com.youhajun.domain.models.Mapper
import com.youhajun.domain.models.enums.PurchaseType

data class PurchaseInAppItemVo(
    override val idx: Int,
    override val productId: String,
    override val purchaseType: PurchaseType,
    override val price: String,
    val itemIcon: String,
    val itemCount: Int,
):PurchaseItemVo {
    companion object : Mapper.ResponseMapper<PurchaseInAppItem, PurchaseInAppItemVo> {
        override fun mapDtoToModel(type: PurchaseInAppItem): PurchaseInAppItemVo {
            return type.run {
                PurchaseInAppItemVo(
                    idx = idx,
                    productId = productId,
                    purchaseType = PurchaseType.typeOf(purchaseType),
                    price = price,
                    itemIcon = itemIcon,
                    itemCount = itemCount,
                )
            }
        }
    }
}
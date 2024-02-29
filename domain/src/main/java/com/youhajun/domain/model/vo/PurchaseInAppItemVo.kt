package com.youhajun.domain.model.vo

import com.youhajun.data.model.dto.store.PurchaseInAppItem
import com.youhajun.domain.model.Mapper
import com.youhajun.domain.model.enums.PurchaseType

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
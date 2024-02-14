package com.youhajun.domain.model.vo

import com.youhajun.data.model.dto.store.PurchaseItem
import com.youhajun.domain.model.Mapper

data class PurchaseItemVo(
    val idx: Int,
    val icon: String,
    val itemCount: Int,
    val itemPrice: String,
) {
    companion object : Mapper.ResponseMapper<PurchaseItem, PurchaseItemVo> {
        override fun mapDtoToModel(type: PurchaseItem): PurchaseItemVo {
            return type.run {
                PurchaseItemVo(
                    idx = idx,
                    icon = "",
                    itemCount = itemCount,
                    itemPrice = itemPrice
                )
            }
        }
    }
}
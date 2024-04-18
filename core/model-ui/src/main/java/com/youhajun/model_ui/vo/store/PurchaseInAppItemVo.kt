package com.youhajun.model_ui.vo.store

import com.youhajun.model_data.store.PurchaseInAppItem
import com.youhajun.model_ui.ToModel
import com.youhajun.model_ui.types.store.PurchaseType

data class PurchaseInAppItemVo(
    override val idx: Long,
    override val productId: String,
    override val purchaseType: PurchaseType,
    override val price: String,
    val itemIcon: String,
    val itemCount: Int,
) : PurchaseItemVo {

    companion object : ToModel<PurchaseInAppItemVo, PurchaseInAppItem> {
        override fun PurchaseInAppItem.toModel(): PurchaseInAppItemVo = PurchaseInAppItemVo(
            idx, productId, PurchaseType.typeOf(purchaseType), price, itemIcon, itemCount
        )
    }
}
package com.youhajun.model_ui.vo.store

import com.youhajun.model_data.store.PurchaseVerifyRequest
import com.youhajun.model_ui.ToDto
import com.youhajun.model_ui.types.store.PurchaseType

data class PurchaseVerifyRequestVo(
    val purchaseType: PurchaseType,
    val orderId: String?,
    val productId: List<String>,
    val purchaseToken: String
) {
    companion object : ToDto<PurchaseVerifyRequestVo, PurchaseVerifyRequest> {
        override fun PurchaseVerifyRequestVo.toDto(): PurchaseVerifyRequest = PurchaseVerifyRequest(
            purchaseType.type, orderId ?: "", productId, purchaseToken
        )
    }
}
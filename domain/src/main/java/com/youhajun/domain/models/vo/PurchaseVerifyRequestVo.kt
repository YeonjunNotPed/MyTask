package com.youhajun.domain.models.vo

import com.youhajun.data.models.dto.store.PurchaseVerifyRequest
import com.youhajun.domain.models.Mapper
import com.youhajun.domain.models.enums.PurchaseType

data class PurchaseVerifyRequestVo(
    val purchaseType: PurchaseType,
    val orderId:String?,
    val productId: List<String>,
    val purchaseToken: String
) {
    companion object : Mapper.RequestMapper<PurchaseVerifyRequest, PurchaseVerifyRequestVo> {
        override fun mapModelToDto(type: PurchaseVerifyRequestVo): PurchaseVerifyRequest {
            return type.run {
                PurchaseVerifyRequest(
                    purchaseType.type,
                    orderId ?: "",
                    productId,
                    purchaseToken
                )
            }
        }
    }
}
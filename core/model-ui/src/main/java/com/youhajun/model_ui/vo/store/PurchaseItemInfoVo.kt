package com.youhajun.model_ui.vo.store

import com.youhajun.model_data.store.PurchaseItemInfo
import com.youhajun.model_ui.ToModel
import com.youhajun.model_ui.types.store.SubsGradeType
import com.youhajun.model_ui.vo.store.PurchaseInAppItemVo.Companion.toModel
import com.youhajun.model_ui.vo.store.PurchaseSubsItemVo.Companion.toModel

data class PurchaseItemInfoVo(
    val currentItemCount: Int,
    val currentGrade: SubsGradeType,
    val inAppItems: List<PurchaseInAppItemVo> = emptyList(),
    val subsItems: List<PurchaseSubsItemVo> = emptyList()
) {
    companion object : ToModel<PurchaseItemInfoVo, PurchaseItemInfo> {
        override fun PurchaseItemInfo.toModel(): PurchaseItemInfoVo = PurchaseItemInfoVo(
            currentItemCount,
            SubsGradeType.typeOf(currentGrade),
            inAppItems.map { it.toModel() },
            subsItems.map { it.toModel() }
        )
    }
}
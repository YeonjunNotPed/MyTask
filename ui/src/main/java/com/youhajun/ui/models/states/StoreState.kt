package com.youhajun.ui.models.states

import com.youhajun.domain.models.enums.SubsGradeType
import com.youhajun.domain.models.vo.PurchaseInAppItemVo
import com.youhajun.domain.models.vo.PurchaseSubsItemVo

data class StoreState(
    val onProgress: Boolean = false,
    val onError: Boolean = false,
    val purchaseInAppItemList: List<PurchaseInAppItemVo> = emptyList(),
    val purchaseSubsItemList: List<PurchaseSubsItemVo> = emptyList(),
    val currentItemCount: Int = 0,
    val currentGrade: SubsGradeType = SubsGradeType.NONE,
    val multiplePurchaseCount:Int = 1,
)
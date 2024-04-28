package com.youhajun.model_ui.states

import com.youhajun.model_ui.types.store.SubsGradeType
import com.youhajun.model_ui.vo.store.PurchaseInAppItemVo
import com.youhajun.model_ui.vo.store.PurchaseSubsItemVo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class StoreState(
    val onProgress: Boolean = false,
    val onError: Boolean = false,
    val purchaseInAppItemList: ImmutableList<PurchaseInAppItemVo> = persistentListOf(),
    val purchaseSubsItemList: ImmutableList<PurchaseSubsItemVo> = persistentListOf(),
    val currentItemCount: Int = 0,
    val currentGrade: SubsGradeType = SubsGradeType.NONE,
    val multiplePurchaseCount:Int = 1,
)
package com.youhajun.domain.usecase.store

import com.youhajun.data.repositories.StoreRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.vo.PurchaseItemInfoVo
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPurchaseItemInfoUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) : UseCase<Unit, Flow<UiState<PurchaseItemInfoVo>>>() {
    override suspend fun invoke(request: Unit): Flow<UiState<PurchaseItemInfoVo>> {
        return storeRepository.getPurchaseItemInfo().map {
            it.mapToUiState { PurchaseItemInfoVo.mapDtoToModel(it) }
        }
    }
}
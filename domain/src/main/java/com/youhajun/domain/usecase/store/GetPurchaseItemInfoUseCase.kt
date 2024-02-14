package com.youhajun.domain.usecase.store

import com.youhajun.data.repositories.StoreRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.model.UiState
import com.youhajun.domain.model.vo.PurchaseItemVo
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPurchaseItemInfoUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) : UseCase<Unit, Flow<UiState<List<PurchaseItemVo>>>>() {
    override suspend fun invoke(request: Unit): Flow<UiState<List<PurchaseItemVo>>> {
        return storeRepository.getPurchaseItemInfo().map {
            it.mapToUiState {
                it.items.map { PurchaseItemVo.mapDtoToModel(it) }
            }
        }
    }
}
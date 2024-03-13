package com.youhajun.domain.usecase.store

import com.youhajun.data.repositories.StoreRepository
import com.youhajun.domain.mapToUiState
import com.youhajun.domain.models.UiState
import com.youhajun.domain.models.vo.PurchaseVerifyRequestVo
import com.youhajun.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostVerifyPurchaseUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) : UseCase<PurchaseVerifyRequestVo, Flow<UiState<Unit>>>() {
    override suspend fun invoke(request: PurchaseVerifyRequestVo): Flow<UiState<Unit>> {
        return storeRepository.postPurchaseVerify(PurchaseVerifyRequestVo.mapModelToDto(request)).map {
            it.mapToUiState { }
        }
    }
}
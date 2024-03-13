package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.data.models.dto.ApiResponse
import com.youhajun.data.models.dto.store.PurchaseItemInfo
import com.youhajun.data.models.dto.store.PurchaseVerifyRequest
import com.youhajun.data.network.safeFlow
import com.youhajun.data.services.StoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRemoteDataSource @Inject constructor(
    private val storeService: StoreService
) {
    suspend fun getPurchaseItemInfo(): Flow<Response<ApiResponse<PurchaseItemInfo>>> = flow {
        emit(storeService.getPurchaseItemInfo())
    }.safeFlow()

    suspend fun postPurchaseVerify(request: PurchaseVerifyRequest): Flow<Response<ApiResponse<Unit>>> = flow {
        emit(storeService.postPurchaseVerify(request))
    }.safeFlow()
}
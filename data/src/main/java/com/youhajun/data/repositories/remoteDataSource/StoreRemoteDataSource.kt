package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.data.model.dto.ApiResponse
import com.youhajun.data.model.dto.store.PurchaseItemInfo
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
    }
//
//    suspend fun getPurchaseItemInfo(): Flow<Resource<PurchaseItemInfo>> = flow {
//        delay(1000L)
//        emit(Resource.Success(Dummy.Store.purchaseItemInfo))
//    }
}
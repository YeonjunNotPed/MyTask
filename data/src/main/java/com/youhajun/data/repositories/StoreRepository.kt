package com.youhajun.data.repositories

import com.youhajun.data.repositories.remoteDataSource.StoreRemoteDataSource
import com.youhajun.model_data.store.PurchaseItemInfo
import com.youhajun.model_data.store.PurchaseVerifyRequest
import com.youhajun.model_data.ApiResult
import javax.inject.Inject

class StoreRepository @Inject constructor(
    private val storeRemoteDataSource: StoreRemoteDataSource,
) {
    suspend fun getPurchaseItemInfo(): ApiResult<PurchaseItemInfo> =
        storeRemoteDataSource.getPurchaseItemInfo()

    suspend fun postPurchaseVerify(request: PurchaseVerifyRequest): ApiResult<Unit> =
        storeRemoteDataSource.postPurchaseVerify(request)
}


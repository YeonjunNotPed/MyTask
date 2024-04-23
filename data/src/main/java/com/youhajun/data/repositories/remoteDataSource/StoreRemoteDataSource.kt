package com.youhajun.data.repositories.remoteDataSource

import com.youhajun.model_data.store.PurchaseItemInfo
import com.youhajun.model_data.store.PurchaseVerifyRequest
import com.youhajun.model_data.ApiResult
import com.youhajun.remote.myTaskApiHandle
import com.youhajun.remote.services.StoreService
import javax.inject.Inject

class StoreRemoteDataSource @Inject constructor(
    private val storeService: StoreService
) {
    suspend fun getPurchaseItemInfo(): ApiResult<PurchaseItemInfo> =
        myTaskApiHandle { storeService.getPurchaseItemInfo() }

    suspend fun postPurchaseVerify(request: PurchaseVerifyRequest): ApiResult<Unit> =
        myTaskApiHandle { storeService.postPurchaseVerify(request) }
}
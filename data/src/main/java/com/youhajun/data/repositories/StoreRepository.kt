package com.youhajun.data.repositories

import com.youhajun.data.Resource
import com.youhajun.data.models.dto.store.PurchaseItemInfo
import com.youhajun.data.models.dto.store.PurchaseVerifyRequest
import com.youhajun.data.repositories.base.BaseRepository
import com.youhajun.data.repositories.localDataSource.StoreLocalDataSource
import com.youhajun.data.repositories.remoteDataSource.StoreRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepository @Inject constructor(
    private val storeRemoteDataSource: StoreRemoteDataSource,
) : BaseRepository() {
    suspend fun getPurchaseItemInfo(): Flow<Resource<PurchaseItemInfo>> =
        storeRemoteDataSource.getPurchaseItemInfo().map { myTaskApiConverter(it) }

    suspend fun postPurchaseVerify(request: PurchaseVerifyRequest): Flow<Resource<Unit>> =
        storeRemoteDataSource.postPurchaseVerify(request).map { myTaskApiConverter(it) }
}


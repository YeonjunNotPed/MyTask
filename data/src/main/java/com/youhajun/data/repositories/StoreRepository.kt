package com.youhajun.data.repositories

import com.youhajun.data.Resource
import com.youhajun.data.model.dto.store.PurchaseItemInfo
import com.youhajun.data.repositories.base.BaseRepository
import com.youhajun.data.repositories.localDataSource.StoreLocalDataSource
import com.youhajun.data.repositories.remoteDataSource.StoreRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreRepository @Inject constructor(
    private val storeRemoteDataSource: StoreRemoteDataSource,
    private val storeLocalDataSource: StoreLocalDataSource
): BaseRepository() {
    suspend fun getPurchaseItemInfo(): Flow<Resource<PurchaseItemInfo>> = storeRemoteDataSource.getPurchaseItemInfo()
        .map { apiConverter(it) }
}


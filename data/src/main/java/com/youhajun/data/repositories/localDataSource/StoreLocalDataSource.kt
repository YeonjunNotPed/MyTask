package com.youhajun.data.repositories.localDataSource

import com.youhajun.data.model.dto.store.PurchaseItemInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreLocalDataSource @Inject constructor() {
    suspend fun getPurchaseItemInfo(): Flow<Response<PurchaseItemInfo>> = TODO()
}
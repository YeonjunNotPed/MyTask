package com.youhajun.data.repositories.localDataSource

import com.youhajun.data.models.dto.store.PurchaseItemInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

class StoreLocalDataSource @Inject constructor() {
    suspend fun getPurchaseItemInfo(): Flow<Response<PurchaseItemInfo>> = TODO()
}
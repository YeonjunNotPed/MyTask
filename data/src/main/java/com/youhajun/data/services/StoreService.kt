package com.youhajun.data.services

import com.youhajun.data.Endpoint
import com.youhajun.data.model.dto.ApiResponse
import com.youhajun.data.model.dto.store.PurchaseItemInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET

interface StoreService {

    @GET(Endpoint.STORE.GET_PURCHASE_ITEM_INFO)
    suspend fun getPurchaseItemInfo(): Response<ApiResponse<PurchaseItemInfo>>
}
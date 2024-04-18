package com.youhajun.remote.services

import com.youhajun.model_data.ApiResponse
import com.youhajun.model_data.store.PurchaseItemInfo
import com.youhajun.model_data.store.PurchaseVerifyRequest
import com.youhajun.remote.Endpoint
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StoreService {

    @GET(Endpoint.Store.GET_PURCHASE_ITEM_INFO)
    suspend fun getPurchaseItemInfo(): Response<ApiResponse<PurchaseItemInfo>>

    @POST(Endpoint.Store.POST_PURCHASE_VERIFY)
    suspend fun postPurchaseVerify(@Body request: PurchaseVerifyRequest): Response<ApiResponse<Unit>>
}
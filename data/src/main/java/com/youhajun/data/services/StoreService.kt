package com.youhajun.data.services

import com.youhajun.data.Endpoint
import com.youhajun.data.model.dto.ApiResponse
import com.youhajun.data.model.dto.store.PurchaseItemInfo
import com.youhajun.data.model.dto.store.PurchaseVerifyRequest
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
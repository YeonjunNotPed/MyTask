package com.youhajun.data.network

import com.youhajun.data.models.MyTaskCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

private const val SAFE_API_CALL_CATCH = "SAFE_API_CALL_CATCH"
fun <T> Flow<Response<T>>.safeResponseFlow(): Flow<Response<T>> = this.catch {
    emit(Response.error(MyTaskCode.NETWORK_SERVER_ERROR_CODE, SAFE_API_CALL_CATCH.toResponseBody()))
}
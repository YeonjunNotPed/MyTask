package com.youhajun.data.network

import com.youhajun.data.Resource
import com.youhajun.data.ResourceErrorVo
import com.youhajun.data.models.MyTaskCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

private const val SAFE_API_CALL_CATCH = "SAFE_API_CALL_CATCH"
fun <T> Flow<Response<T>>.safeResponseFlow(): Flow<Response<T>> = this.catch {
    val responseMessage = it.message ?: SAFE_API_CALL_CATCH
    emit(Response.error(MyTaskCode.SAFE_API_CALL_CODE, responseMessage.toResponseBody()))
}

fun <T> Flow<Resource<T>>.safeResourceFlow(): Flow<Resource<T>> = this.catch {
    val responseMessage = it.message ?: SAFE_API_CALL_CATCH
    emit(Resource.Error(ResourceErrorVo(null,MyTaskCode.SAFE_RESOURCE_CALL_CODE, responseMessage)))
}
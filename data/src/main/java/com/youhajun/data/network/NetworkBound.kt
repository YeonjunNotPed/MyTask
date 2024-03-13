package com.youhajun.data.network

import com.youhajun.data.Resource
import com.youhajun.data.ResourceErrorVo
import com.youhajun.data.models.MyTaskCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class NetworkBound {
    inline fun <ResultType, RequestType> networkBoundResource(
        crossinline query: () -> Flow<ResultType>,
        crossinline fetch: suspend () -> RequestType,
        crossinline saveFetchResult: suspend (RequestType) -> Unit,
        crossinline onFetchFailed: (Throwable) -> Unit = { Unit },
        crossinline shouldFetch: (ResultType) -> Boolean = { true }
    ): Flow<Resource<ResultType>> = flow {
        val data = query().first()

        val flow = if (shouldFetch(data)) {
            try {
                saveFetchResult(fetch())
                query().map { Resource.Success(it) }
            } catch (throwable: Throwable) {
                onFetchFailed(throwable)
                query().map {
                    val errorVo = ResourceErrorVo(it, MyTaskCode.NETWORK_BOUND_ERROR_CODE, throwable.message)
                    Resource.Error(errorVo)
                }
            }
        } else {
            query().map { Resource.Success(it) }
        }

        emitAll(flow)
    }
}
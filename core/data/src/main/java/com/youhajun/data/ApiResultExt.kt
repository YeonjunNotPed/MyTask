package com.youhajun.data

import com.youhajun.model_data.ApiError
import com.youhajun.model_data.ApiException
import com.youhajun.model_data.ApiResult
import com.youhajun.model_data.ApiSuccess

suspend fun <T : Any> ApiResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): ApiResult<T> = apply {
    if (this is ApiSuccess<T>) {
        executable(data)
    }
}

suspend fun <T : Any> ApiResult<T>.onError(
    executable: suspend (data: T?, code: Int, message: String?) -> Unit
): ApiResult<T> = apply {
    if (this is ApiError<T>) {
        executable(data, code, message)
    }
}

suspend fun <T : Any> ApiResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): ApiResult<T> = apply {
    if (this is ApiException<T>) {
        executable(e)
    }
}
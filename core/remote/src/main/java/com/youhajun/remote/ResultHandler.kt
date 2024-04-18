package com.youhajun.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.youhajun.model_data.ApiError
import com.youhajun.model_data.ApiException
import com.youhajun.model_data.ApiResponse
import com.youhajun.model_data.ApiResult
import com.youhajun.model_data.ApiSuccess
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.Reader

suspend fun <T : Any> apiHandle(
    execute: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ApiSuccess(body)
        } else {
            ApiError(code = response.code(), message = response.message())
        }
    } catch (e: HttpException) {
        ApiError(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        ApiException(e)
    }
}

suspend inline fun <reified T : Any> myTaskApiHandle(
    crossinline execute: suspend () -> Response<ApiResponse<T>>
): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        val bodyData = body?.data

        if (response.isSuccessful) {
            if (bodyData == null) {
                ApiError(code = 0, message = response.message())
            } else {
                ApiSuccess(bodyData)
            }
        } else {
            parseErrorBody<T>(response.errorBody()).run {
                ApiError(data, statusCode, message)
            }
        }
    } catch (e: HttpException) {
        ApiError(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        ApiException(e)
    }
}

inline fun <reified T: Any> parseErrorBody(errorBody: ResponseBody?): ApiResponse<T> {
    val gson = Gson()
    val type = object : TypeToken<ApiResponse<T>>() {}.type
    return gson.fromJson(errorBody?.charStream(), type)
}
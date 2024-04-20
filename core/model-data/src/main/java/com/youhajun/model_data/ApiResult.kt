package com.youhajun.model_data

sealed interface ApiResult<T: Any>

class ApiSuccess<T: Any>(val data: T) : ApiResult<T>
open class ApiError<T: Any>(val data:T? = null, val code: Int, val message: String? = "") :
    ApiResult<T>
class ApiException<T: Any>(val e: Throwable) : ApiResult<T>
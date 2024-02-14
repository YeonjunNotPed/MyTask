package com.youhajun.data

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    class Loading<T> : Resource<T>()
    data class Error<T>(val errorVo: ResourceErrorVo<T>) : Resource<T>()
}

data class ResourceErrorVo<T>(
    val data: T? = null, val code: Int? = null, val message: String? = null
) {
    fun <C> convert(transform: (T?) -> C? = { null }): ResourceErrorVo<C> {
        return ResourceErrorVo(transform(data), code, message)
    }
}
package com.youhajun.data.repositories.base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.youhajun.data.Resource
import com.youhajun.data.ResourceErrorVo
import com.youhajun.data.model.MyTaskCode.API_DATA_NULL_ERROR_CODE
import com.youhajun.data.model.MyTaskCode.API_LAUNCHER_CONVERT_ERROR_CODE
import com.youhajun.data.model.dto.ApiResponse
import retrofit2.Response
import java.io.Reader

abstract class BaseRepository {

    protected inline fun <reified DTO> apiConverter(
        response: Response<ApiResponse<DTO>>,
    ): Resource<DTO> {
        try {
            return when (response.isSuccessful) {
                true -> {
                    val apiResponse = response.body() as ApiResponse

                    if (apiResponse.data == null) {
                        if(DTO::class == Unit::class) Resource.Success(Unit as DTO)
                        else {
                            val errorVo = ResourceErrorVo<DTO>(null, API_DATA_NULL_ERROR_CODE, NullPointerException().message)
                            Resource.Error(errorVo)
                        }

                    } else Resource.Success(apiResponse.data)
                }

                false -> {
                    fromGson<DTO>(response.errorBody()?.charStream()).run {
                        val errorVo = ResourceErrorVo(data, statusCode, message)
                        Resource.Error(errorVo)
                    }
                }
            }
        } catch (e: Throwable) {
            val errorVo = ResourceErrorVo<DTO>(null, API_LAUNCHER_CONVERT_ERROR_CODE, e.message)
            return Resource.Error(errorVo)
        }
    }

    protected fun <DTO> fromGson(json: Reader?): ApiResponse<DTO> {
        return Gson().fromJson(json, object : TypeToken<ApiResponse<DTO>>() {}.type)
    }
}
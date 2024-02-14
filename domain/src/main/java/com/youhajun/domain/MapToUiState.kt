package com.youhajun.domain

import com.youhajun.data.Resource
import com.youhajun.domain.model.UiState
import com.youhajun.domain.model.UiStateErrorVo

fun <DTO, MODEL> Resource<DTO>.mapToUiState(transform: (DTO) -> MODEL): UiState<MODEL> {
    return when (this) {
        is Resource.Success -> UiState.Success(transform(data))
        is Resource.Loading -> UiState.Loading()
        is Resource.Error -> {
            val modelErrorVo = errorVo.run {
                val nullableData = data?.let { transform(it) }
                UiStateErrorVo(nullableData, code, message)
            }
            UiState.Error(modelErrorVo)
        }
    }
}
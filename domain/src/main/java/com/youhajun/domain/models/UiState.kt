package com.youhajun.domain.models

sealed class UiState<T> {
    data class Success<T>(val data: T) : UiState<T>()
    class Loading<T> : UiState<T>()
    data class Error<T>(val errorVo: UiStateErrorVo<T>) : UiState<T>()
}

data class UiStateErrorVo<T>(
    val data: T? = null, val code: Int? = null, val message: String? = null
)

suspend fun <T> UiState<T>.inspectUiState(
    onLoading: suspend () -> Unit = {},
    onError: suspend (UiStateErrorVo<T>) -> Unit = { _ -> },
    onSuccess: suspend (T) -> Unit
) {
    when (this) {
        is UiState.Success -> onSuccess(data)
        is UiState.Error -> onError(errorVo)
        is UiState.Loading -> onLoading()
    }
}
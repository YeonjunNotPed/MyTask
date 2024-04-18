package com.youhajun.model_ui

interface ToModel<M, D> {
    fun D.toModel(): M
}

interface ToDto<M, D> {
    fun M.toDto(): D
}

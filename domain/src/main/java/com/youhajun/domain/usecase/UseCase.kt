package com.youhajun.domain.usecase

abstract class UseCase<PARAM,MODEL> {
    abstract suspend operator fun invoke(request:PARAM): MODEL
}
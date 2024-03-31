package com.youhajun.domain.usecase

abstract class UseCase<PARAM,MODEL> {
    abstract suspend operator fun invoke(request:PARAM): MODEL
}

abstract class NonSuspendUseCase<PARAM,MODEL> {
    abstract operator fun invoke(request:PARAM): MODEL
}
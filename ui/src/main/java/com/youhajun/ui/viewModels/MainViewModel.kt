package com.youhajun.ui.viewModels

import androidx.lifecycle.ViewModel
import com.youhajun.domain.usecase.sign.PostLoginUseCase
import com.youhajun.domain.usecase.sign.PostSocialLoginUseCase
import com.youhajun.ui.models.sideEffects.LoginSideEffect
import com.youhajun.ui.models.sideEffects.MainSideEffect
import com.youhajun.ui.models.states.LoginState
import com.youhajun.ui.models.states.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

interface MainIntent {

}

@HiltViewModel
class MainViewModel @Inject constructor(
) : ContainerHost<MainState, MainSideEffect>, ViewModel(), MainIntent {
    override val container: Container<MainState, MainSideEffect> = container(MainState())

}
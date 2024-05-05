package com.youhajun.ui.viewModels

import androidx.lifecycle.ViewModel
import com.youhajun.model_ui.sideEffects.MainSideEffect
import com.youhajun.model_ui.states.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

private interface MainIntent {

}

@HiltViewModel
class MainViewModel @Inject constructor(
) : ContainerHost<MainState, MainSideEffect>, ViewModel(), MainIntent {
    override val container: Container<MainState, MainSideEffect> = container(MainState())

}
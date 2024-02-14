package com.youhajun.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youhajun.domain.model.inspectUiState
import com.youhajun.domain.usecase.store.GetPurchaseItemInfoUseCase
import com.youhajun.ui.models.sideEffects.StoreSideEffect
import com.youhajun.ui.models.states.StoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

interface StoreIntent {
    fun onClickPurchaseItem(idx: Int)
    fun onClickCheckCurrentItem()
    fun onFetchPurchaseItem()
}

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getPurchaseItemInfoUseCase: GetPurchaseItemInfoUseCase
) : ContainerHost<StoreState, StoreSideEffect>, ViewModel(), StoreIntent {

    init {
        onFetchPurchaseItem()
    }

    override val container: Container<StoreState, StoreSideEffect> = container(StoreState())

    override fun onClickPurchaseItem(idx: Int) {

    }

    override fun onClickCheckCurrentItem() {
        TODO("Not yet implemented")
    }


    override fun onFetchPurchaseItem() {
        intent {
            viewModelScope.launch {
                getPurchaseItemInfoUseCase(Unit).collect {
                    it.inspectUiState {
                        reduce {
                            state.copy(purchaseItemList = it)
                        }
                    }
                }
            }
        }
    }
}
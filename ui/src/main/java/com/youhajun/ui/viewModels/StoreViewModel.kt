package com.youhajun.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.Purchase
import com.youhajun.data.onSuccess
import com.youhajun.data.repositories.StoreRepository
import com.youhajun.model_ui.sideEffects.StoreSideEffect
import com.youhajun.model_ui.states.StoreState
import com.youhajun.model_ui.types.store.PurchaseType
import com.youhajun.model_ui.types.store.SubsGradeType
import com.youhajun.model_ui.vo.store.PurchaseItemInfoVo.Companion.toModel
import com.youhajun.model_ui.vo.store.PurchaseVerifyRequestVo
import com.youhajun.model_ui.vo.store.PurchaseVerifyRequestVo.Companion.toDto
import com.youhajun.ui.utils.GoogleBillingUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

private interface StoreIntent {
    fun onClickInAppPurchaseItem(productId: String, isMultiple: Boolean)
    fun onClickSubsPurchaseItem(productId: String, selectedBasePlanId: String)
    fun onClickCheckItemHistory()
    fun onFetchPurchaseItem()
    fun onClickMultipleItemCountControl(isPlus: Boolean)
    fun onClickHeaderBackIcon()
}

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val googleBillingUtil: GoogleBillingUtil,
    private val storeRepository: StoreRepository
) : ContainerHost<StoreState, StoreSideEffect>, ViewModel(), StoreIntent {

    companion object {
        private const val MULTIPLE_MAX_COUNT = 99
        private const val MULTIPLE_MIN_COUNT = 1
        private const val MULTIPLE_UNIT = 1
    }

    override val container: Container<StoreState, StoreSideEffect> = container(StoreState()) {
        onFetchPurchaseItem()
    }
    override fun onClickHeaderBackIcon() {
        intent { postSideEffect(StoreSideEffect.Navigation.NavigateUp) }
    }

    private val googleBillingCallback = object : GoogleBillingUtil.Callback {
        override fun onProductDetailsEmptyOrNull() {
            intent {
                postSideEffect(
                    StoreSideEffect.Toast("상품 정보가 없습니다")
                )
            }
        }

        override fun onPurchaseCancelByUser() {
            intent {
                postSideEffect(
                    StoreSideEffect.Toast("유저 결제 취소")
                )
            }
        }

        override fun onConnectionError() {
            intent {
                postSideEffect(
                    StoreSideEffect.Toast("연결 에러")
                )
            }
        }

        override fun onLaunchBillingError() {
            intent {
                postSideEffect(
                    StoreSideEffect.Toast("구매창 표시 에러")
                )
            }
        }

        override fun onPurchaseError() {
            intent {
                postSideEffect(
                    StoreSideEffect.Toast("구매 에러")
                )
            }
        }

        override fun onVerifyPurchase(purchase: Purchase) {
            onGooglePurchaseVerify(purchase)
        }

        override fun onInAppPurchaseSuccess(purchase: Purchase) {
            handleInAppPurchaseSuccess(purchase)
        }

        override fun onSubsPurchaseSuccess(purchase: Purchase) {
            handleSubsPurchaseSuccess(purchase)
        }
    }

    override fun onClickInAppPurchaseItem(productId: String, isMultiple: Boolean) {
        intent {
            val productIds = if (isMultiple) List(state.multiplePurchaseCount) { productId }
            else listOf(productId)

            postSideEffect(
                StoreSideEffect.BillingLaunch {
                    googleBillingUtil.onLaunchBillingFlow(productIds, it)
                }
            )
        }
    }

    override fun onClickSubsPurchaseItem(productId: String, selectedBasePlanId: String) {
        intent {
            postSideEffect(
                StoreSideEffect.BillingLaunch {
                    googleBillingUtil.onLaunchBillingFlow(
                        listOf(productId),
                        it,
                        selectedBasePlanId,
                    )
                }
            )
        }
    }

    override fun onClickCheckItemHistory() {

    }

    override fun onClickMultipleItemCountControl(isPlus: Boolean) {
        intent {
            val count = getControlledMultipleCount(state.multiplePurchaseCount, isPlus)
            reduce {
                state.copy(multiplePurchaseCount = count)
            }
        }
    }

    override fun onFetchPurchaseItem() {
        viewModelScope.launch {
            storeRepository.getPurchaseItemInfo().onSuccess {
                val info = it.toModel()
                intent {
                    reduce {
                        state.copy(
                            currentItemCount = info.currentItemCount,
                            currentGrade = info.currentGrade,
                            purchaseInAppItemList = info.inAppItems.toImmutableList(),
                            purchaseSubsItemList = info.subsItems.toImmutableList()
                        )
                    }
                    val purchaseList = info.inAppItems + info.subsItems
                    googleBillingUtil.onStartConnect(googleBillingCallback, purchaseList)
                }
            }
        }
    }
    private fun onGooglePurchaseVerify(purchase: Purchase) {
        viewModelScope.launch {
            val requestVo = PurchaseVerifyRequestVo(
                purchaseType = if (purchase.isAutoRenewing) PurchaseType.SUBS else PurchaseType.IN_APP,
                orderId = purchase.orderId,
                productId = purchase.products,
                purchaseToken = purchase.purchaseToken
            )
            storeRepository.postPurchaseVerify(requestVo.toDto()).onSuccess {
                intent {
                    postSideEffect(
                        StoreSideEffect.Toast("구입 확인 성공")
                    )
                    googleBillingUtil.onPurchaseVerifySuccess(purchase)
                }
            }
        }
    }

    private fun getControlledMultipleCount(currentCount: Int, isPlus: Boolean): Int {
        return if (isPlus) {
            (currentCount + MULTIPLE_UNIT).coerceAtMost(MULTIPLE_MAX_COUNT)
        } else {
            (currentCount - MULTIPLE_UNIT).coerceAtLeast(MULTIPLE_MIN_COUNT)
        }
    }

    private fun handleInAppPurchaseSuccess(purchase: Purchase) {
        intent {
            postSideEffect(
                StoreSideEffect.Toast("구입 성공")
            )
            val totalItemCount = state.currentItemCount + state.purchaseInAppItemList
                .filter { purchase.products.contains(it.productId) }
                .sumOf { it.itemCount }
            reduce {
                state.copy(currentItemCount = totalItemCount)
            }
        }
    }

    private fun handleSubsPurchaseSuccess(purchase: Purchase) {
        intent {
            postSideEffect(
                StoreSideEffect.Toast("구독 성공")
            )

            val currentSubs = state.purchaseSubsItemList.find { it.productId == purchase.products.first() }?.subsGrade

            reduce {
                state.copy(currentGrade = currentSubs ?: SubsGradeType.NONE)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        googleBillingUtil.cleanUp()
    }
}
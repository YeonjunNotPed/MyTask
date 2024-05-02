package com.youhajun.ui.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.android.billingclient.api.BillingFlowParams.SubscriptionUpdateParams
import com.android.billingclient.api.BillingFlowParams.SubscriptionUpdateParams.ReplacementMode
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetails.SubscriptionOfferDetails
import com.android.billingclient.api.ProductDetailsResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.acknowledgePurchase
import com.android.billingclient.api.consumePurchase
import com.android.billingclient.api.queryProductDetails
import com.youhajun.common.IoDispatcher
import com.youhajun.common.MainImmediateDispatcher
import com.youhajun.model_ui.types.store.PurchaseType
import com.youhajun.model_ui.vo.store.PurchaseItemVo
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class GoogleBillingUtil @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainImmediateDispatcher private val mainImmediateDispatcher: CoroutineDispatcher
) {
    interface Callback {
        fun onConnectionError()
        fun onLaunchBillingError()
        fun onPurchaseError()
        fun onPurchaseCancelByUser()
        fun onProductDetailsEmptyOrNull()
        fun onVerifyPurchase(purchase: Purchase)
        fun onInAppPurchaseSuccess(purchase: Purchase)
        fun onSubsPurchaseSuccess(purchase: Purchase)
    }

    companion object {
        private const val TAG = "billing"
        private const val MAX_RETRY_CONNECT_COUNT = 3
    }

    private var retryCount = 0
    private var callback: Callback? = null
    private var currentPurchaseToken: String? = null
    private var productDetailsMap: HashMap<String, ProductDetails> = hashMapOf()

    private lateinit var inAppProductQueryList: List<QueryProductDetailsParams.Product>
    private lateinit var subsProductQueryList: List<QueryProductDetailsParams.Product>

    private val purchasesUpdatedListener = PurchasesUpdatedListener(::purchaseListener)
    private val purchaseResponseListener = PurchasesResponseListener(::purchaseListener)

    private val scope = CoroutineScope(Job() + mainImmediateDispatcher)

    private val billingClient: BillingClient by lazy {
        BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    fun cleanUp() {
        scope.cancel()
        billingClient.endConnection()
    }

    fun onStartConnect(callback: Callback, purchaseItemList: List<PurchaseItemVo>) {
        this.callback = callback
        initProductQueryList(purchaseItemList)
        startConnection()
    }

    fun onLaunchBillingFlow(
        productIds: List<String>,
        activity: Activity,
        basePlanId: String? = null,
    ) {
        Log.d(TAG, "onLaunchBillingFlow $productIds $basePlanId")
        if (productIds.contains("") || productDetailsMap.isEmpty() || !billingClient.isReady) return

        runCatching {
            val params = if (basePlanId.isNullOrEmpty()) {
                getBillingFlowParams(productIds, null, null)
            } else {
                getBillingFlowParams(productIds, basePlanId, currentPurchaseToken)
            }

            launchBillingFlow(activity, params)
        }.onFailure { e ->
            callback?.onLaunchBillingError()
            Log.e(TAG, "onLaunchBillingFlow failed: ${e.message}")
        }
    }

    fun onPurchaseVerifySuccess(purchase: Purchase) {
        Log.d(TAG, "onPurchaseVerifySuccess : ${purchase.isAutoRenewing}")
        scope.launch {
            if (purchase.isAutoRenewing) approveSubPurchase(purchase)
            approveInAppPurchase(purchase)
        }
    }

    private fun initProductQueryList(purchaseItemList: List<PurchaseItemVo>) {
        Log.d(TAG, "initProductQueryListInApp : ${purchaseItemList.filterNot { it.purchaseType == PurchaseType.SUBS }.distinctBy { it.productId }}")
        Log.d(TAG,"initProductQueryListSubs : ${purchaseItemList.filter { it.purchaseType == PurchaseType.SUBS }.distinctBy { it.productId }}")
        inAppProductQueryList = purchaseItemList.filterNot { it.purchaseType == PurchaseType.SUBS }
            .distinctBy { it.productId }
            .map { convertToProductQuery(it) }
        subsProductQueryList = purchaseItemList.filter { it.purchaseType == PurchaseType.SUBS }
            .distinctBy { it.productId }
            .map { convertToProductQuery(it) }
    }

    private fun convertToProductQuery(item: PurchaseItemVo): QueryProductDetailsParams.Product {
        val productType = when (item.purchaseType) {
            PurchaseType.IN_APP_MULTIPLE,
            PurchaseType.IN_APP -> ProductType.INAPP
            PurchaseType.SUBS -> ProductType.SUBS
        }

        return QueryProductDetailsParams
            .Product
            .newBuilder()
            .setProductId(item.productId)
            .setProductType(productType)
            .build()
    }

    private fun getBillingFlowParams(
        productIds: List<String>,
        basePlanId: String?,
        oldPurchaseToken: String?
    ): BillingFlowParams {
        Log.d(TAG, "getBillingFlowParams")
        val productDetails = productIds.mapNotNull { productDetailsMap[it] }
        val productParams = productDetails.map { buildProductDetailsParams(it, basePlanId) }
        return buildBillingFlowParams(productParams, oldPurchaseToken)
    }

    private fun buildProductDetailsParams(
        productDetails: ProductDetails,
        basePlanId: String?
    ): ProductDetailsParams {
        Log.d(TAG, "buildProductDetailsParams")
        val builder = ProductDetailsParams.newBuilder().setProductDetails(productDetails)
        if (!basePlanId.isNullOrEmpty()) {
            val offerToken = getOfferToken(productDetails.subscriptionOfferDetails, basePlanId)
            Log.d(TAG, "buildProductDetailsParams Token : $offerToken")
            builder.setOfferToken(offerToken)
        }
        return builder.build()
    }

    private fun buildBillingFlowParams(
        productDetailsParams: List<ProductDetailsParams>,
        oldPurchaseToken: String?
    ): BillingFlowParams {
        val builder = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParams)
        if (!oldPurchaseToken.isNullOrEmpty()) {
            builder.setSubscriptionUpdateParams(buildSubsUpdateParams(oldPurchaseToken))
        }
        return builder.build()
    }

    private fun buildSubsUpdateParams(oldPurchaseToken: String): SubscriptionUpdateParams {
        Log.d(TAG, "buildSubsUpdateParams Token : $oldPurchaseToken")
        return SubscriptionUpdateParams.newBuilder()
            .setOldPurchaseToken(oldPurchaseToken)
            .setSubscriptionReplacementMode(ReplacementMode.CHARGE_FULL_PRICE)
            .build()
    }

    private fun startConnection() {
        Log.d(TAG, "connection start")
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "connection success")
                    onCheckPurchaseSynchronize()
                    scope.launch {
                        queryProductDetails(PurchaseType.IN_APP)
                        queryProductDetails(PurchaseType.SUBS)
                    }
                } else callback?.onConnectionError()
            }

            override fun onBillingServiceDisconnected() {
                if (retryCount < MAX_RETRY_CONNECT_COUNT) {
                    retryCount++
                    startConnection()
                } else callback?.onConnectionError()
            }
        })
    }

    private fun onCheckPurchaseSynchronize() {
        val params = QueryPurchasesParams.newBuilder().setProductType(ProductType.INAPP).build()
        billingClient.queryPurchasesAsync(params, purchaseResponseListener)
    }

    private suspend fun queryProductDetails(purchaseType: PurchaseType) {
        val params = buildQueryProductDetailsParams(purchaseType)

        val productDetailsResult = withContext(ioDispatcher) {
            billingClient.queryProductDetails(params)
        }

        handleProductDetailsResult(productDetailsResult)
    }

    private fun buildQueryProductDetailsParams(purchaseType: PurchaseType): QueryProductDetailsParams {
        val list = if (purchaseType == PurchaseType.SUBS) subsProductQueryList else inAppProductQueryList
        return QueryProductDetailsParams
            .newBuilder()
            .setProductList(list)
            .build()
    }

    private fun handleProductDetailsResult(productDetailsResult: ProductDetailsResult) {
        Log.d(TAG,"${productDetailsResult.billingResult.debugMessage}, ${productDetailsResult.productDetailsList?.size}개")
        if (productDetailsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            productDetailsResult.productDetailsList?.let {
                productDetailsMap.putAll(it.associateBy { it.productId })
            }
        } else callback?.onProductDetailsEmptyOrNull()
    }

    private fun purchaseListener(billingResult: BillingResult, purchases: List<Purchase>?) {
        Log.d(TAG,"purchaseListener")
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach {
                    handlePurchase(it)
                } ?: callback?.onPurchaseError()
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {
                callback?.onPurchaseCancelByUser()
            }

            else -> callback?.onPurchaseError()
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        Log.d(TAG,"purchaseListener")
        when (purchase.purchaseState) {
            Purchase.PurchaseState.PURCHASED -> {
                Log.d(TAG, "processPurchasesPurchase: $purchase")
                when {
                    !purchase.isAcknowledged -> callback?.onVerifyPurchase(purchase)
                    purchase.isAutoRenewing -> currentPurchaseToken = purchase.purchaseToken
                }
            }

            Purchase.PurchaseState.PENDING -> {
                Log.d(TAG, "processPurchasesPending: $purchase")
            }

            else -> callback?.onPurchaseError()
        }
    }

    private suspend fun approveInAppPurchase(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        val result = withContext(ioDispatcher) {
            billingClient.consumePurchase(consumeParams)
        }
        handlePurchaseFinish(false, result.billingResult.responseCode, purchase)
    }

    private suspend fun approveSubPurchase(purchase: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        val result = withContext(ioDispatcher) {
            billingClient.acknowledgePurchase(acknowledgePurchaseParams)
        }
        handlePurchaseFinish(true, result.responseCode, purchase)
    }

    private fun handlePurchaseFinish(isSubs: Boolean, responseCode: Int, purchase: Purchase) {
        Log.d(TAG, "purchaseSuccess : $responseCode")
        when (responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (isSubs) callback?.onSubsPurchaseSuccess(purchase)
                else callback?.onInAppPurchaseSuccess(purchase)
            }

            else -> callback?.onPurchaseError()
        }
    }

    private fun launchBillingFlow(activity: Activity, params: BillingFlowParams) {
        val launchResult = billingClient.launchBillingFlow(activity, params)
        when (launchResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                Log.d(TAG, "onSuccessBillingLaunch : ${launchResult.debugMessage}")
            }

            else -> callback?.onLaunchBillingError()
        }
    }

    private fun getOfferToken(list: List<SubscriptionOfferDetails>?, basePlanId: String): String {
        return list?.let {
            it.find { it.basePlanId == basePlanId }?.offerToken ?: it.first().offerToken
        } ?: ""
    }
}
package com.zbekz.tashkentmetro.viewmodel.subscription

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.zbekz.tashkentmetro.utils.LocalData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    application: Application,
    private val billingClient: BillingClient // Injected BillingClient
) : AndroidViewModel(application), PurchasesUpdatedListener {

    enum class SubscriptionType { MONTHLY, YEARLY }

    private val _selectedSubscription = MutableStateFlow<SubscriptionType?>(null)
    val selectedSubscription: StateFlow<SubscriptionType?> = _selectedSubscription

    init {
        connectBillingClient()
    }

    private fun connectBillingClient() {
        if (billingClient.isReady) return  // Agar allaqachon ulangan bo'lsa qayta ulanmang

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d("tekshirish", "onBillingSetupFinished: ")
                    // Ulanish muvaffaqiyatli tugallanganidan keyin sotib olish funksiyasini chaqiring
                    initiatePurchase()
                } else {
                    Log.e("Billing", "Billing setup failed: ${billingResult.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
                // Ulanish uzilgan, qayta ulanishga harakat qiling
                connectBillingClient()
            }
        })
    }

    fun selectSubscription(type: SubscriptionType) {
        _selectedSubscription.value = type
    }

    fun initiatePurchase() {
        if (!billingClient.isReady) {
            // Agar hali BillingClient tayyor bo'lmasa, avval ulaning
            connectBillingClient()
            return
        }

        viewModelScope.launch {
            val productIds = listOf(LocalData.monthlyId, LocalData.yearlyId)
            val products = productIds.map { productId ->
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(productId)
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            }

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(products)
                .build()

            billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                    val selectedProductId = when (_selectedSubscription.value) {
                        SubscriptionType.MONTHLY -> LocalData.monthlyId
                        SubscriptionType.YEARLY -> LocalData.yearlyId
                        else -> return@queryProductDetailsAsync
                    }

                    val productDetails = productDetailsList.find { it.productId == selectedProductId }
                    productDetails?.let {
                        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(it)
                            .build()

                        val billingFlowParams = BillingFlowParams.newBuilder()
                            .setProductDetailsParamsList(listOf(productDetailsParams))
                            .build()

                        billingClient.launchBillingFlow(getApplication(), billingFlowParams)
                    }
                } else {
                    Log.e("Billing", "Failed to retrieve product details: ${billingResult.debugMessage}")
                }
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            purchases.forEach { purchase ->
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    acknowledgePurchase(purchase)
                }
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        if (!purchase.isAcknowledged) {
            val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

            billingClient.acknowledgePurchase(acknowledgeParams) { result ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Handle successful acknowledgment
                }
            }
        }
    }
}

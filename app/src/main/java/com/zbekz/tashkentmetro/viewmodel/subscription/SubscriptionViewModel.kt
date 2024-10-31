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
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Billing client is ready
                }
            }

            override fun onBillingServiceDisconnected() {
                // Handle disconnection
            }
        })
    }

    fun selectSubscription(type: SubscriptionType) {
        _selectedSubscription.value = type
    }

    fun initiatePurchase() {
        viewModelScope.launch {
            val productIds = listOf("monthly_subscription", "yearly_subscription") // replace with your actual product IDs
            val products = productIds.map { productId ->
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(productId)
                    .setProductType(BillingClient.ProductType.SUBS) // for subscriptions
                    .build()
            }

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(products)
                .build()

            billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                    val selectedProductId = when (_selectedSubscription.value) {
                        SubscriptionType.MONTHLY -> "monthly_subscription"
                        SubscriptionType.YEARLY -> "yearly_subscription"
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

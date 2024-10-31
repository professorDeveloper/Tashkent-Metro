package com.zbekz.tashkentmetro.di

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BillingModule {

    @Provides
    fun providePurchasesUpdatedListener(): PurchasesUpdatedListener {
        return PurchasesUpdatedListener { billingResult, purchases ->
            // Handle purchase updates
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                // Handle successful purchases
                for (purchase in purchases) {
                    // Process each purchase
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle user cancellation
            } else {
                // Handle other error responses
            }
        }
    }

    @Provides
    fun provideBillingClient(
        @ApplicationContext context: Context,
        purchasesUpdatedListener: PurchasesUpdatedListener
    ): BillingClient {
        return BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }
}

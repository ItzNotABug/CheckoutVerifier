@file:Suppress("unused")

package com.lazygeniouz.checkoutverifier

import com.lazygeniouz.checkoutverifier.bundle.PurchaseBundle
import com.lazygeniouz.checkoutverifier.helper.CheckoutHelper
import com.lazygeniouz.checkoutverifier.results.CompletionResult
import com.lazygeniouz.checkoutverifier.results.ErrorResult
import com.lazygeniouz.checkoutverifier.results.Result
import org.jetbrains.annotations.NotNull

/**
 * CheckoutVerifier main class to verify your purchase via Google Play Billing Library,
 * helps in protection from patching apps like Lucky Patcher.
 *
 * Constructor for CheckoutVerifier
 * @param purchaseBundle = A [PurchaseBundle] which includes the transaction info.
 */
class CheckoutVerifier(@NotNull private val purchaseBundle: PurchaseBundle) {

    /**
     * An [ErrorResult] if an exception was caught,
     *
     * A [CompletionResult] otherwise.
     *
     * @return [Result]
     */
    suspend fun authenticate(): Result {
        if (isEverythingOk)
            return CheckoutHelper(purchaseBundle).start()
        else throw IllegalArgumentException("Either of the Passed arguments in PurchaseBundle (Server Url, Json-Response or Signature) are Empty or Not valid!")
    }

    private val isEverythingOk: Boolean
        get() = purchaseBundle.verifyingUrl.trim().isNotEmpty()
                && purchaseBundle.verifyingUrl.startsWith("http")
                && purchaseBundle.jsonResponse.trim().isNotEmpty()
                && purchaseBundle.signature.trim().isNotEmpty()
}

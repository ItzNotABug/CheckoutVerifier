@file:Suppress("unused")

package com.lazygeniouz.checkoutverifier

import com.lazygeniouz.checkoutverifier.bundle.PurchaseBundle
import com.lazygeniouz.checkoutverifier.helper.CheckoutHelper
import com.lazygeniouz.checkoutverifier.results.Result

class CheckoutVerifier {

    private lateinit var purchaseBundle: PurchaseBundle

    constructor()

    /**
     * Constructor for CheckoutVerifier
     * @param bundle = A Purchase Bundle which includes the transaction info.
     */
    constructor(bundle: PurchaseBundle) {
        this.purchaseBundle = bundle
    }

    /** @param bundle = A Purchase Bundle which includes the transaction info. */
    fun setUrl(bundle: PurchaseBundle): CheckoutVerifier {
        this.purchaseBundle = bundle
        return this
    }

    /**
     * `start()` is a suspend function `authenticate()` now
     * @return SuccessResult if everything goes right,
     * an ErrorResult if an exception is caught
     * @see Result
     */
    suspend fun authenticate(): Result {
        if (isEverythingOk)
            return CheckoutHelper(purchaseBundle).start()
        else throw IllegalArgumentException("Either of the Passed arguments in PurchaseBundle (Server Url, Json-Response or Signature) are Empty or Not valid!")
    }

    private val isEverythingOk: Boolean
        get() = purchaseBundle.verifyingUrl.trim()
            .isNotEmpty() && purchaseBundle.verifyingUrl.startsWith("http")
                && purchaseBundle.jsonResponse.trim()
            .isNotEmpty() && purchaseBundle.signature.trim().isNotEmpty()

}

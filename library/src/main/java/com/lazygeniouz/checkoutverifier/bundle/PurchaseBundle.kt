package com.lazygeniouz.checkoutverifier.bundle

import org.jetbrains.annotations.NotNull

/**
 * PurchaseBundle takes the necessary arguments &
 * passes it on to CheckoutVerifier for validating the authenticity purchase.
 *
 * @param verifyingUrl = Your server url where you have your PHP Script to verify.
 * @param jsonResponse = A response in json format received from Google Play after a purchase.
 * @param signature = A signature key received from Google Play after a purchase to validate the purchase.
 */
data class PurchaseBundle(
    @NotNull val verifyingUrl: String,
    @NotNull val jsonResponse: String,
    @NotNull val signature: String
)
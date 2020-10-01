package com.lazygeniouz.checkoutverifier.bundle

/**
 * PurchaseBundle takes the necessary arguments &
 * passes it on to CheckoutVerifier for validating the authenticity purchase.
 * 
 * @param verifyingUrl = Your server url where you have your PHP Script to verify against.
 * @param jsonResponse = A response in json format received from Google Play after a purchase.
 * @param signature = A signature key received from Google Play after a purchase to validate the purchase.
 */
class PurchaseBundle(
    val verifyingUrl: String,
    val jsonResponse: String,
    val signature: String
)
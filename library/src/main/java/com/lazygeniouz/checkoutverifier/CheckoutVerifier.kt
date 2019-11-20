@file:Suppress("unused")
package com.lazygeniouz.checkoutverifier

class CheckoutVerifier {

    private var verifyingUrl = ""
    private var jsonResponse = ""
    private var signature = ""
    private lateinit var listener: VerifierListener

    constructor()

    /**
     * Constructor for CheckoutVerifier
     * @param url = Server Url.
     * @param jsonResponse = Json Response received after successful purchase.
     * @param signature = Signature received after successful purchase.
     * @param listener = Listener for CheckoutVerifier callbacks.
     */
    constructor(url: String, jsonResponse: String, signature: String, listener: VerifierListener) {
        this.verifyingUrl = url
        this.jsonResponse = jsonResponse
        this.signature = signature
        this.listener = listener
    }

    /** @param url = Server Url. */
    fun setUrl(url: String): CheckoutVerifier {
        this.verifyingUrl = url
        return this
    }

    /** @param jsonResponse = Json Response received after successful purchase. */
    fun setResponseJson(jsonResponse: String): CheckoutVerifier {
        this.jsonResponse = jsonResponse
        return this
    }

    /** @param signature = Signature received after successful purchase. */
    fun setSignature(signature: String): CheckoutVerifier {
        this.signature = signature
        return this
    }

    /** @param listener = Listener for CheckoutVerifier callbacks. */
    fun setListener(listener: VerifierListener): CheckoutVerifier {
        this.listener = listener
        return this
    }

    /** Start the verifying process. */
    fun start(): CheckoutVerifier {
        if (isEverythingOk) CheckoutVerifierTask(verifyingUrl, jsonResponse, signature, listener).execute()
        else throw IllegalArgumentException("Either of the Passed arguments (Server Url, Json-Response or Signature) are Empty or Not valid!")
        return this
    }

    private val isEverythingOk: Boolean
        get() = verifyingUrl.trim().isNotEmpty() && verifyingUrl.startsWith("http")
                && jsonResponse.trim().isNotEmpty() && signature.trim().isNotEmpty()

}

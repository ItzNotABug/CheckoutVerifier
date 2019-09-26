@file:Suppress("unused")
package com.lazygeniouz.checkoutverifier

class CheckoutVerifier {

    private var verifyingUrl = ""
    private var responseJson = ""
    private var signature = ""
    private lateinit var listener: VerifierListener

    constructor()

    constructor(url: String, jsonResponse: String, signature: String, mListener: VerifierListener) {
        this.verifyingUrl = url
        this.responseJson = jsonResponse
        this.signature = signature
        this.listener = mListener
    }

    fun setUrl(url: String): CheckoutVerifier {
        this.verifyingUrl = url
        return this
    }

    fun setResponseJson(responseJson: String): CheckoutVerifier {
        this.responseJson = responseJson
        return this
    }

    fun setSignature(signature: String): CheckoutVerifier {
        this.signature = signature
        return this
    }

    fun setListener(listener: VerifierListener): CheckoutVerifier {
        this.listener = listener
        return this
    }

    fun start(): CheckoutVerifier {
        if (isEverythingOk) CheckoutVerifierTask(verifyingUrl, responseJson, signature, listener).execute()
        else throw IllegalArgumentException("Either of the Passed arguments (Server Url, Json-Response or Signature) are Empty or Not valid!")
        return this
    }

    private val isEverythingOk: Boolean
        get() = verifyingUrl.trim().isNotEmpty() && verifyingUrl.startsWith("http")
                && responseJson.trim().isNotEmpty() && signature.trim().isNotEmpty()

}

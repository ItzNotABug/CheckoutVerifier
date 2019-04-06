package com.lazygeniouz.checkoutverifier

import org.jetbrains.annotations.Nullable

interface VerifyingListener {
    //Called when CheckoutVerifierTask's onPreExecute() is fired!
    fun onVerificationStarted()

    //Called when the result is returned from the server!
    fun onVerificationCompleted(@Nullable isVerified: Boolean?)

    //Called when the Server was probably unreachable or something else went wrong!
    fun onExceptionCaught(exception: Exception)
}

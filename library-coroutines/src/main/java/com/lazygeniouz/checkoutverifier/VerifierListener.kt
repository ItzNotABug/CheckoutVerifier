package com.lazygeniouz.checkoutverifier

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

interface VerifierListener {

    //Called when CheckoutVerifierTask's onPreExecute() is fired.
    //Helpful for showing progressBar or a loading screen.
    fun onVerificationStarted()

    //Called when the result is returned from the server.
    //Null if an Exception was caught, result as false / true otherwise.
    fun onVerificationCompleted(@Nullable isVerified: Boolean?)

    //Called when the Server was probably unreachable or something else went wrong.
    fun onExceptionCaught(@NotNull exception: Exception)
}

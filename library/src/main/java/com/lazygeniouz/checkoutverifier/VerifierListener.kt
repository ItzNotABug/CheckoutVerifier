package com.lazygeniouz.checkoutverifier

import java.lang.Exception

interface VerifyingListener {
    fun onVerificationStarted()
    fun onVerificationCompleted(isVerified: Boolean)
    fun onExceptionCaught(exception: Exception)
}

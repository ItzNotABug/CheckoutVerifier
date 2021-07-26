package com.lazygeniouz.checkoutverifier.results

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

/**
 * Base class for handling the Results returned from CheckoutVerifier
 */
sealed class Result

/**
 * Returned when a request to the server was **Successful**
 * @property isVerified : True if purchase is authentic, False otherwise.
 */
data class CompletionResult(@Nullable val isVerified: Boolean) : Result()

/**
 * Returned when a request to the server was **Unsuccessful**
 * @property exception : The exception caught when the request was unsuccessful.
 */
data class ErrorResult(@NotNull val exception: Exception) : Result()
package com.lazygeniouz.checkoutverifier.results

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

open class Result

// Returned when the server request was successful
data class CompletionResult(@Nullable val isVerified: Boolean) : Result()

// Returned when there was an Exception when connecting with the server
data class ErrorResult(@NotNull val exception: Exception) : Result()
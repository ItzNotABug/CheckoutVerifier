package com.lazygeniouz.checkoutverifier.helper

import com.lazygeniouz.checkoutverifier.bundle.PurchaseBundle
import com.lazygeniouz.checkoutverifier.results.CompletionResult
import com.lazygeniouz.checkoutverifier.results.ErrorResult
import com.lazygeniouz.checkoutverifier.results.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.NotNull
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import java.util.*
import javax.net.ssl.HttpsURLConnection

/**
 * A Helper class to authenticate the Purchase
 * @param purchaseBundle : A PurchaseBundle holds necessary data to validate the purchase.
 */
internal class CheckoutHelper(@NotNull private val purchaseBundle: PurchaseBundle) {

    private lateinit var exception: Exception

    suspend fun start(): Result = withContext(Dispatchers.IO) { getResult() }

    private fun getResult(): Result {
        var isPurchaseVerified = ""
        val url = URL(getFormattedUrl())
        var urlConnection: HttpsURLConnection? = null
        try {
            urlConnection = url.openConnection() as HttpsURLConnection
            val inputStream = urlConnection.inputStream
            val inRead = InputStreamReader(inputStream)
            isPurchaseVerified = inRead.convertToString()
        }

        /**
         * falling back to a more generic type [Exception]
         * rather than an explicit [IOException]
         */
        catch (exception: Exception) {
            exception.printStackTrace()
            this.exception = exception
        } finally { urlConnection?.disconnect() }

        return if (!::exception.isInitialized) CompletionResult(isPurchaseVerified === "verified")
        else ErrorResult(exception)
    }

    private fun getFormattedUrl(): String = "${purchaseBundle.verifyingUrl}?jsonResponse=${
        purchaseBundle.jsonResponse.toEncodedUrl()
    }&signature=${purchaseBundle.signature.toEncodedUrl()}"

    private fun String.toEncodedUrl(): String = URLEncoder.encode(this, "UTF-8")

    private fun InputStreamReader.convertToString(): String {
        val scanner = Scanner(this).useDelimiter("\\A")
        return if (scanner.hasNext()) scanner.next() else ""
    }
}
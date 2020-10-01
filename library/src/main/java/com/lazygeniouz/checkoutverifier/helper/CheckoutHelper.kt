package com.lazygeniouz.checkoutverifier.helper

import com.lazygeniouz.checkoutverifier.bundle.PurchaseBundle
import com.lazygeniouz.checkoutverifier.results.CompletionResult
import com.lazygeniouz.checkoutverifier.results.ErrorResult
import com.lazygeniouz.checkoutverifier.results.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
internal class CheckoutHelper(private val purchaseBundle: PurchaseBundle) {

    private lateinit var exception: Exception
    private var isExceptionCaught: Boolean = false

    suspend fun start(): Result = withContext(Dispatchers.IO) { getResult() }

    private fun getResult(): Result {
        var isPurchaseVerified = ""
        val url: URL
        var urlConnection: HttpsURLConnection? = null
        try {
            url = URL(formattedUrl)
            urlConnection = url.openConnection() as HttpsURLConnection
            val inputStream = urlConnection.inputStream
            val inRead = InputStreamReader(inputStream)
            isPurchaseVerified = inRead.convertToString()

        } catch (exception: IOException) {
            exception.printStackTrace()
            isExceptionCaught = true
            this.exception = exception
        } finally {
            urlConnection?.disconnect()
        }

        return if (!isExceptionCaught) CompletionResult(isPurchaseVerified === "verified")
        else ErrorResult(exception)
    }

    private val formattedUrl: String
        get() = "${purchaseBundle.verifyingUrl}?jsonResponse=${
            URLEncoder.encode(
                purchaseBundle.jsonResponse,
                "UTF-8"
            )
        }&signature=${URLEncoder.encode(purchaseBundle.signature, "UTF-8")}"

    private fun InputStreamReader.convertToString(): String {
        val scanner = Scanner(this).useDelimiter("\\A")
        return if (scanner.hasNext()) scanner.next() else ""
    }
}
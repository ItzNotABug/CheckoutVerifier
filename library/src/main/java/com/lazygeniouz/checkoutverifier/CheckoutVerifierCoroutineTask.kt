package com.lazygeniouz.checkoutverifier

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.coroutines.CoroutineContext

internal class CheckoutVerifierCoroutineTask(
    private val verifyingUrl: String,
    private val responseBody: String,
    private val signature: String,
    private val listener: VerifierListener
) : CoroutineScope {

    private val job = Job()
    private var isExceptionCaught: Boolean = false
    override val coroutineContext: CoroutineContext
        get() = IO + job


    fun start() = launch {
        listener.onVerificationStarted()
        val result = getResult()
        listener.onVerificationCompleted(result)
        cancelJob()
    }

    private suspend fun getResult(): Boolean? = withContext(IO) {
        var isPurchaseVerified = ""
        val url: URL
        var urlConnection: HttpsURLConnection? = null
        try {
            url = URL(formattedUrl)
            urlConnection = url.openConnection() as HttpsURLConnection
            val inputStream = urlConnection.inputStream
            val inRead = InputStreamReader(inputStream)
            isPurchaseVerified = inRead.convertToString()

        } catch (exception: Exception) {
            exception.printStackTrace()
            isExceptionCaught = true
            listener.onExceptionCaught(exception)
        } finally {
            urlConnection?.disconnect()
        }
        return@withContext if (!isExceptionCaught) isPurchaseVerified == "verified"
        else null
    }

    private fun cancelJob() {
        job.cancel()
    }

    private val formattedUrl: String
        get() = "$verifyingUrl?jsonResponse=${URLEncoder.encode(
            responseBody,
            "UTF-8"
        )}&signature=${URLEncoder.encode(signature, "UTF-8")}"

    private fun InputStreamReader.convertToString(): String {
        val scanner = Scanner(this).useDelimiter("\\A")
        return if (scanner.hasNext()) scanner.next() else ""
    }
}
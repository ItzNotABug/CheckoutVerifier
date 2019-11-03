package com.lazygeniouz.checkoutverifier

import android.os.AsyncTask
import org.jetbrains.annotations.Nullable
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import java.util.*
import javax.net.ssl.HttpsURLConnection

@Deprecated("Use CheckoutVerifierCoroutineTask")
internal class CheckoutVerifierTask(
    private val verifyingUrl: String,
    private val responseBody: String,
    private val signature: String,
    private val listener: VerifierListener
) : AsyncTask<String, Boolean, Boolean>() {

    private var isExceptionCaught: Boolean = false

    override fun onPreExecute() {
        super.onPreExecute()
        listener.onVerificationStarted()
    }

    override fun doInBackground(vararg strings: String): Boolean? {
        var isPurchaseVerified = ""
        val url: URL
        var urlConnection: HttpsURLConnection? = null
        try {
            val serverUrl = verifyingUrl + "?jsonResponse=" + URLEncoder.encode(responseBody, "UTF-8") + "&signature=" + URLEncoder.encode(signature, "UTF-8")

            url = URL(serverUrl)
            urlConnection = url.openConnection() as HttpsURLConnection
            val inputStream = urlConnection.inputStream
            val inRead = InputStreamReader(inputStream)
            isPurchaseVerified = convertStreamToString(inRead)

        } catch (exception: Exception) {
            exception.printStackTrace()
            isExceptionCaught = true
            listener.onExceptionCaught(exception)
        } finally {
            urlConnection?.disconnect()
        }

        return if (!isExceptionCaught) isPurchaseVerified == "verified"
        else null
    }

    override fun onPostExecute(@Nullable result: Boolean?) {
        listener.onVerificationCompleted(result)
    }

    private fun convertStreamToString(inputStreamReader: InputStreamReader): String {
        val scanner = Scanner(inputStreamReader).useDelimiter("\\A")
        return if (scanner.hasNext()) scanner.next() else ""
    }
}

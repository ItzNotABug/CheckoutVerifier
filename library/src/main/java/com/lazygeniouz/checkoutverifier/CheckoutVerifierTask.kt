package com.lazygeniouz.checkoutverifier

import android.os.AsyncTask
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import java.util.*
import javax.net.ssl.HttpsURLConnection

internal class CheckoutVerifierTask(
    private val verifyingUrl: String,
    private val responseBody: String,
    private val signature: String,
    private val listener: VerifyingListener?
) : AsyncTask<String, Boolean, Boolean>() {


    override fun onPreExecute() {
        super.onPreExecute()
        listener?.onVerificationStarted()
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

        } catch (e: IOException) {
            e.printStackTrace()
            listener?.onExceptionCaught(e)
        } finally {
            urlConnection?.disconnect()
        }

        return isPurchaseVerified == "verified"
    }

    override fun onPostExecute(result: Boolean?) {
        listener?.onVerificationCompleted(result!!)
    }

    private fun convertStreamToString(`is`: InputStreamReader): String {
        val s = Scanner(`is`).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }
}

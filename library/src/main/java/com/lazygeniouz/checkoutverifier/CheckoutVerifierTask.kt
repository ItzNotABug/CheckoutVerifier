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
        var retrieveFromServer = ""
        val url: URL
        var urlConnection: HttpsURLConnection? = null
        try {
            val urlStr = verifyingUrl + "?data=" + URLEncoder.encode(responseBody, "UTF-8") + "&signature=" + URLEncoder.encode(signature, "UTF-8")

            url = URL(urlStr)
            urlConnection = url.openConnection() as HttpsURLConnection
            val `in` = urlConnection.inputStream
            val inRead = InputStreamReader(`in`)
            retrieveFromServer = convertStreamToString(inRead)

        } catch (e: IOException) {
            e.printStackTrace()
            listener?.onExceptionCaught(e)
        } finally {
            urlConnection?.disconnect()
        }

        return retrieveFromServer == "verified"
    }

    override fun onPostExecute(result: Boolean?) {
        listener?.onVerificationCompleted(result!!)
    }

    private fun convertStreamToString(`is`: InputStreamReader): String {
        val s = Scanner(`is`).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }
}

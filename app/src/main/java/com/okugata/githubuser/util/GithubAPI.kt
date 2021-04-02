package com.okugata.githubuser.util

import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header

private const val TOKEN = "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"

fun getGithubAPI(url: String, callback: (error: Throwable?, response: String) -> Unit){
    val client = AsyncHttpClient()

    client.addHeader("Authorization", "token $TOKEN")
    client.addHeader("User-Agent", "request")
    client.get(url, object : AsyncHttpResponseHandler() {
        override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
            val result = String(responseBody)
            callback(null, result)
        }
        override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
            val result = String(responseBody)
            callback(error, result)
        }
    })
}
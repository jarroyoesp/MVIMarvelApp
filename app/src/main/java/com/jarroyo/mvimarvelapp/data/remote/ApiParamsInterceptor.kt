package com.jarroyo.mvimarvelapp.data.remote

import com.jarroyo.mvimarvelapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest


class ApiParamsInterceptor : Interceptor {

    companion object {
        val TAG = ApiParamsInterceptor::class.java.simpleName

        private const val PARAM_API_KEY = "apikey"
        private const val PARAM_HASH = "hash"
        private const val PARAM_TS = "ts"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val timestamp = System.currentTimeMillis()
        val url = request.url.newBuilder()
            .addQueryParameter(PARAM_API_KEY, BuildConfig.API_KEY)
            .addQueryParameter(PARAM_HASH, generateHash(timestamp))
            .addQueryParameter(PARAM_TS, timestamp.toString())
            .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }

    private fun generateHash(timestamp: Long): String {
        val hash = "${timestamp}${BuildConfig.API_PRIVATE_KEY}${BuildConfig.API_KEY}"
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(hash.toByteArray())).toString(16).padStart(32, '0')
    }
}
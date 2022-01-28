package com.jarroyo.mvimarvelapp.data.remote

import com.jarroyo.mvimarvelapp.BuildConfig
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import okhttp3.Interceptor
import okhttp3.Response

class ApiParamsInterceptor : Interceptor {

    companion object {

        private const val PARAM_API_KEY = "apikey"
        private const val PARAM_HASH = "hash"
        private const val PARAM_TS = "ts"
        private const val SIGNUM = 1
        private const val RADIX = 16
        private const val LENGTH = 32
        private const val PADCHAR = '0'
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
        return BigInteger(SIGNUM, md.digest(hash.toByteArray())).toString(RADIX).padStart(LENGTH, PADCHAR)
    }
}

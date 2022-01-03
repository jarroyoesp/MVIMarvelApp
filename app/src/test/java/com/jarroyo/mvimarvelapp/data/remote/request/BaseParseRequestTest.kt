package com.jarroyo.mvimarvelapp.data.remote.request

import com.jarroyo.mvimarvelapp.data.remote.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

abstract class BaseParseRequestTest {
    abstract val baseUrl: String

    /***********************************************************************************************
     * Prepare Test methods
     */
    internal fun prepareRetrofitService(filePath: String): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(provideMoshiBuilder()))
            .client(getOkHttpMock(filePath))
            .build()
            .create(ApiService::class.java)
    }

    private fun getOkHttpMock(filePath: String): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(MockTestResponseInterceptor(filePath))
            .build()
    }

    private fun provideMoshiBuilder(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    /**
     * MOCK Response Interceptor
     */
    private class MockTestResponseInterceptor(private val filePath: String) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val responseString = this::class.java.classLoader!!.getResource(filePath).readText(Charsets.UTF_8)

            return chain.proceed(chain.request())
                .newBuilder()
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(responseString.toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
                .addHeader("content-type", "application/json")
                .build()

        }
    }

}
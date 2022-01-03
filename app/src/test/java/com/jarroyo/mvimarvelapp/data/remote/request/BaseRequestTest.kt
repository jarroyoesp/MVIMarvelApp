package com.jarroyo.mvimarvelapp.data.remote.request

import com.jarroyo.mvimarvelapp.BuildConfig
import com.jarroyo.mvimarvelapp.data.remote.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

abstract class BaseRequestTest {
    val baseUrl = BuildConfig.BASE_URL

    /**
     * Prepare Test methods
     */
    internal fun prepareRetrofitService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(provideMoshiBuilder()))
            .client(getOkHttpMock())
            .build()
            .create(ApiService::class.java)
    }

    private fun getOkHttpMock(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    private fun provideMoshiBuilder(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}
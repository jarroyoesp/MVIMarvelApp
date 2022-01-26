package com.jarroyo.mvimarvelapp.di

import com.jarroyo.mvimarvelapp.BuildConfig
import com.jarroyo.mvimarvelapp.data.remote.ApiParamsInterceptor
import com.jarroyo.mvimarvelapp.data.remote.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Provides
    fun provideMoshiBuilder(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    fun provideRetrofit(moshi: Moshi): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BuildConfig.BASE_URL)
    }

    @Provides
    fun provideApiParamsInterceptor() = ApiParamsInterceptor()

    @Singleton
    @Provides
    fun provideOkhttpClient(apiParamsInterceptor: ApiParamsInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiParamsInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit.Builder, okHttpClient: OkHttpClient): ApiService {
        return retrofit
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}

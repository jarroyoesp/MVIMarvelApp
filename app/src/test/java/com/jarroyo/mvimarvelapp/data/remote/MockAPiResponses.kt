package com.jarroyo.mvimarvelapp.data.remote

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import retrofit2.Response.error

object MockAPiResponses {
    fun <T>getErrorResponse(): Response<T> = error<T>(404,
        "responseString".toByteArray().toResponseBody("application/json".toMediaTypeOrNull())
    )

}
package com.jarroyo.mvimarvelapp.data.remote.request

import com.jarroyo.mvimarvelapp.data.remote.ApiService
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UrlTest : BaseRequestTest() {

    private val urlCharacterList =
        "https://gateway.marvel.com/v1/public/${ApiService.URL_PATH_CHARACTERS}?${ApiService.QUERY_OFFSET}=0"
    private val urlSearchCharacterList =
        "https://gateway.marvel.com/v1/public/${ApiService.URL_PATH_CHARACTERS}?${ApiService.QUERY_NAME_STARTS_WITH}=name"

    @Test
    fun retrofitCheckGetCharacterList() {
        runBlocking {
            val request = prepareRetrofitService().getCharacterList(0).raw().request
            val urlRetrofit = request.url.toString()
            assertEquals(urlCharacterList, urlRetrofit)
        }
    }

    @Test
    fun retrofitCheckSearchCharacterList() {
        runBlocking {
            val request = prepareRetrofitService().searchCharacterList("name").raw().request
            val urlRetrofit = request.url.toString()
            assertEquals(urlSearchCharacterList, urlRetrofit)
        }
    }

}
package com.jarroyo.mvimarvelapp.data.remote.request

import com.jarroyo.mvimarvelapp.BuildConfig
import com.jarroyo.mvimarvelapp.data.remote.model.APIListResponse
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

class ParseDataTest : BaseParseRequestTest() {
    override val baseUrl: String = BuildConfig.BASE_URL

    @Test
    fun retrofitParseResponseGetBreedList() {
        runBlocking {
            // WHEN
            var response: Response<APIListResponse>? = null
            response = prepareRetrofitService("character_list.json").getCharacterList(0)
            assertEquals(1011334L, response.body()?.apiData!!.results[0].id)
            assertEquals("3-D Man", response.body()?.apiData!!.results[0].name)
        }
    }
}

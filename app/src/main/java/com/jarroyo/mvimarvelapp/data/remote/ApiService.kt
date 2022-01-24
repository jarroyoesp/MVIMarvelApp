package com.jarroyo.mvimarvelapp.data.remote

import com.jarroyo.mvimarvelapp.data.remote.model.APIListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    companion object {
        const val URL_PATH_CHARACTERS = "characters"
        const val QUERY_OFFSET = "offset"
        const val QUERY_NAME_STARTS_WITH = "nameStartsWith"
    }

    // https://gateway.marvel.com:443/v1/public/characters?apikey=APIKEY
    @GET(URL_PATH_CHARACTERS)
    suspend fun getCharacterList(
        @Query(QUERY_OFFSET) offset: Int
    ): Response<APIListResponse>

    @GET(URL_PATH_CHARACTERS)
    suspend fun searchCharacterList(
        @Query(QUERY_NAME_STARTS_WITH) name: String
    ): Response<APIListResponse>
}

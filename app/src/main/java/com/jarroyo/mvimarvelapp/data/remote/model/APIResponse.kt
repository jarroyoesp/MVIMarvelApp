package com.jarroyo.mvimarvelapp.data.remote.model

import com.squareup.moshi.Json

class APICharacterResponse

data class APIListResponse(
    val attributionHTML: String = "",
    val attributionText: String = "",
    val code: Int = 0,
    val copyright: String = "",
    @Json(name = "data")
    val apiData: Data? = null,
    val etag: String = "",
    val status: String = ""
)

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<ResultApi>,
    val total: Int
)

data class ResultApi(
    val comics: Comics,
    val description: String,
    val events: Events,
    val id: Long,
    val modified: String,
    val name: String,
    val resourceURI: String,
    val series: Series,
    val stories: Stories,
    val thumbnail: Thumbnail,
    val urls: List<Url>
)

data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemX>,
    val returned: Int
)

data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemXX>,
    val returned: Int
)

data class Stories(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemXXX>,
    val returned: Int
)

data class Thumbnail(
    val extension: String,
    val path: String
)

data class Url(
    val type: String,
    val url: String
)

data class Item(
    val name: String,
    val resourceURI: String
)

data class ItemX(
    val name: String,
    val resourceURI: String
)

data class ItemXX(
    val name: String,
    val resourceURI: String
)

data class ItemXXX(
    val name: String,
    val resourceURI: String,
    val type: String
)

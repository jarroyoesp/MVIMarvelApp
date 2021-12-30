package com.jarroyo.mvimarvelapp.domain.model

import android.os.Parcelable
import com.jarroyo.mvimarvelapp.data.remote.model.APIListResponse
import com.jarroyo.mvimarvelapp.data.remote.model.ResultApi
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiModel(
    val id: Long,
    val name: String?,
    val description: String?,
    val imageHomeUrl: String?,
    val comicList: List<ItemUIModel>? = null,
    val seriesList: List<ItemUIModel>? = null,
    val storiesList: List<ItemUIModel>? = null,
) : Parcelable

@Parcelize
data class ItemUIModel(
    val resourceURI: String,
    val title: String
): Parcelable

fun APIListResponse.toDomainModel(): List<UiModel>? {
    return this.data?.results?.map {
        UiModel(id = it.id,
            name = it.name,
            description = it.description,
            imageHomeUrl = getHomeImage(it),
            comicList = getComics(it),
            seriesList = getSeries(it),
            storiesList = getStories(it),
        )
    }
}

private fun getHomeImage(result: ResultApi): String {
    return "${result.thumbnail.path}/landscape_xlarge.${result.thumbnail.extension}"
}

private fun getComics(result: ResultApi): List<ItemUIModel> {
    return result.comics.items.map {
        ItemUIModel(it.resourceURI, it.name)
    }
}

private fun getSeries(result: ResultApi): List<ItemUIModel> {
    return result.series.items.map {
        ItemUIModel(it.resourceURI, it.name)
    }
}

private fun getStories(result: ResultApi): List<ItemUIModel> {
    return result.stories.items.map {
        ItemUIModel(it.resourceURI, it.name)
    }
}

/*fun UiModel.toEntity(): CharacterEntity {
    return CharacterEntity(id = this.id,
        name = this.name,
        description = this.description,
        image = this.imageHomeUrl )
}

fun CharacterEntity.toDomain(): UiModel{
    return this.let {
        CharacterUIModel(
            id = this.id,
            name = this.name,
            description = this.description,
            imageHomeUrl = this.image
        )
    }
}

fun List<CharacterEntity>?.toDomain(): List<UiModel> {
    return this?.map {
        it.toDomain()
    }?: emptyList()
}*/

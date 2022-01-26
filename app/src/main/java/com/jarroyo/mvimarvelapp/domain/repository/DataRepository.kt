package com.jarroyo.mvimarvelapp.domain.repository

import com.jarroyo.mvimarvelapp.domain.model.UiModel

interface DataRepository {

    suspend fun getList(page: Int): Result<List<UiModel>?>
    suspend fun search(name: String): Result<List<UiModel>?>

    /**
     * Favorite Character
     */
    suspend fun saveFavorite(uiModel: UiModel): Result<Boolean>
    suspend fun removeFavorite(uiModel: UiModel): Result<Boolean>
    suspend fun getFavorite(): Result<List<UiModel>?>
    suspend fun isFavorite(uiModel: UiModel): Boolean
}

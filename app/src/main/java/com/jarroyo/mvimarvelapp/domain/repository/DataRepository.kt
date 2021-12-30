package com.jarroyo.mvimarvelapp.domain.repository

import com.jarroyo.mvimarvelapp.domain.model.UiModel


interface DataRepository {

    suspend fun getList(page: Int): Result<List<UiModel>?>
    suspend fun search(name: String): Result<List<UiModel>?>

}